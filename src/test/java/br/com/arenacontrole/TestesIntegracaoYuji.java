package br.com.arenacontrole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;

import br.com.arenacontrole.db.DatabaseConfig;
import br.com.arenacontrole.repository.CampeonatoRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Integração - Yuji Faruk Murakami Feles
 * Baseado no Plano de Testes CBF Digital v1.0
 *
 * Casos de Teste: CT49-CT52 (conforme Plano de Testes #65)
 * - CT49: Bloqueio de Placar Negativo (Gols Pró) com persistência
 * - CT50: Bloqueio de Times Duplicados em Partida com persistência
 * - CT51: Validação de Cadastro de Times com persistência
 * - CT52: Múltiplas partidas com persistência acumulativa
 *
 * Cobre os seguintes requisitos funcionais de integração:
 * - RF09: Bloqueio de Placar Negativo (com persistência)
 * - RF10: Bloqueio de Times Duplicados em Partida (com persistência)
 * - RF11: Validação de Cadastro de Times (com persistência)
 * - RF12: Múltiplas operações de persistência
 */
@DisplayName("Testes de Integração - Yuji")
public class TestesIntegracaoYuji {

    private DataSource ds;
    private CampeonatoRepository repo;
    private Campeonato campeonato;

    @BeforeEach
    void setUp() throws Exception {
        // Cria DataSource via configuração central
        ds = DatabaseConfig.createDataSource();
        repo = new CampeonatoRepository(ds);

        // Limpa tabelas para ambiente limpo
        limparTabelas(ds);

        // Instancia campeonato com repositório (persiste via repository)
        campeonato = new Campeonato(repo);
    }

    // Helper: tenta limpar tabelas comuns (silencia erros de tabela inexistente)
    private void limparTabelas(DataSource ds) {
        String[] tables = new String[]{"partida", "team"};
        for (String t : tables) {
            try (Connection c = ds.getConnection();
                 Statement s = c.createStatement()) {
                s.executeUpdate("DELETE FROM " + t);
            } catch (SQLException ignored) {
                // tabela pode não existir ainda; ignorar
            }
        }
    }

    // ==================== CT49-CT52: Testes de Integração ====================

    /**
     * CT49: Bloqueio de Placar Negativo com Persistência
     * Verifica que o bloqueio funciona E que nada é persistido no BD
     *
     * Entradas: registrarResultado("Time A", "Time B", -1, 3, 0, 0, 0, 0)
     * Resultado Esperado: 
     * - O sistema bloqueia o registro (IllegalArgumentException)
     * - Atributos permanecem inalterados no BD
     * - Nenhuma partida é registrada no histórico
     *
     * Pré-condição: Dois times cadastrados
     * Pós-condição: BD inalterado, exception lançada
     */
    @Test
    @DisplayName("CT49: Bloqueio de Placar Negativo com Persistência")
    void testCT49_BloqueioPlacarNegativoPersistencia() {
        // Arrange
        campeonato.cadastrarTime("Time A", "TA");
        campeonato.cadastrarTime("Time B", "TB");

        Time timeAAntes = repo.findTimeByNome("Time A");
        int pontosAntes = timeAAntes.getPontos();
        int jogosAntes = timeAAntes.getJogos();

        // Act & Assert - Tenta registrar com gols negativos
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time A", "Time B", -1, 3, 0, 0, 0, 0)
        );

        assertTrue(e.getMessage().toLowerCase().contains("negativ") ||
                   e.getMessage().toLowerCase().contains("não negativo"),
                   "Mensagem de erro deve mencionar valor negativo");

        // Verifica que atributos não foram alterados no BD
        Time timeADepois = repo.findTimeByNome("Time A");
        assertEquals(pontosAntes, timeADepois.getPontos(), "Pontos não devem ter mudado");
        assertEquals(jogosAntes, timeADepois.getJogos(), "Jogos não devem ter mudado");
    }

    /**
     * CT50: Bloqueio de Times Duplicados em Partida com Persistência
     * Verifica que o bloqueio de "um time contra si mesmo" funciona
     *
     * Entradas: registrarResultado("Time C", "Time C", 2, 2, 0, 0, 0, 0)
     * Resultado Esperado:
     * - O sistema bloqueia o registro (IllegalArgumentException)
     * - Atributos permanecem inalterados no BD
     * - Nenhuma partida é registrada
     *
     * Pré-condição: Um time cadastrado
     * Pós-condição: BD inalterado, exception lançada
     */
    @Test
    @DisplayName("CT50: Bloqueio de Times Duplicados em Partida")
    void testCT50_BloqueioTimesDuplicadosPersistencia() {
        // Arrange
        campeonato.cadastrarTime("Time C", "TC");

        Time timeCAntes = repo.findTimeByNome("Time C");
        int jogosAntes = timeCAntes.getJogos();

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time C", "Time C", 2, 2, 0, 0, 0, 0)
        );

        assertTrue(e.getMessage().toLowerCase().contains("si mesmo") ||
                   e.getMessage().toLowerCase().contains("mesmo time") ||
                   e.getMessage().toLowerCase().contains("diferentes"),
                   "Mensagem de erro deve mencionar times diferentes");

        // Verifica que BD não foi alterado
        Time timeCDepois = repo.findTimeByNome("Time C");
        assertEquals(jogosAntes, timeCDepois.getJogos(), "Jogos não devem ter mudado");
        assertEquals(0, timeCDepois.getPontos(), "Pontos devem permanecer 0");
    }

    /**
     * CT51: Validação de Cadastro de Times com Persistência
     * Verifica que o bloqueio de cadastro inválido funciona E nada é persistido
     *
     * Entradas: cadastrarTime("", "VZO") ou cadastrarTime("   ", "SEM")
     * Resultado Esperado:
     * - O sistema impede o cadastro (IllegalArgumentException)
     * - Nenhum time é criado no BD
     * - Mensagem de erro é exibida
     *
     * Pré-condição: BD limpo
     * Pós-condição: Nenhum time criado, exception lançada
     */
    @Test
    @DisplayName("CT51: Validação de Cadastro de Times com Persistência")
    void testCT51_ValidacaoCadastroNomeVazioPersistencia() {
        // Act & Assert - Tenta cadastrar com nome vazio
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.cadastrarTime("", "VZO")
        );

        assertTrue(e.getMessage().toLowerCase().contains("obrigat") ||
                   e.getMessage().toLowerCase().contains("vazio") ||
                   e.getMessage().toLowerCase().contains("nome"),
                   "Mensagem de erro deve mencionar campo obrigatório");

        // Verifica que nenhum time foi criado no BD com nome vazio
        Time timeNaoBD = repo.findTimeByNome("");
        assertNull(timeNaoBD, "Time com nome vazio não deve ser persistido");

        // Verifica com nome apenas espaços
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () ->
            campeonato.cadastrarTime("   ", "SEM")
        );

        assertTrue(e2.getMessage().toLowerCase().contains("obrigat") ||
                   e2.getMessage().toLowerCase().contains("vazio"));

        // Verifica que nenhum time foi criado
        assertEquals(0, campeonato.getNumeroTimes(), "Nenhum time deve ser criado");
    }

    /**
     * CT52: Bloqueio de Placar Parcial com Persistência
     * Verifica que o bloqueio de placar com campo vazio funciona
     *
     * Entradas: registrarResultado("Time K", "Time L", 1, null, 0, 0, 0, 0)
     * Resultado Esperado:
     * - O sistema bloqueia o registro (IllegalArgumentException)
     * - Atributos permanecem inalterados no BD
     * - Nenhuma partida é registrada no histórico
     *
     * Pré-condição: Dois times cadastrados
     * Pós-condição: BD inalterado, exception lançada, nenhuma partida inserida
     */
    @Test
    @DisplayName("CT52: Bloqueio de Placar Parcial com Persistência")
    void testCT52_BloqueioPlacarParcialPersistencia() {
        // Arrange
        campeonato.cadastrarTime("Time K", "TK");
        campeonato.cadastrarTime("Time L", "TL");

        Time timeKAntes = repo.findTimeByNome("Time K");
        int jogosAntes = timeKAntes.getJogos();

        // Act & Assert - Tenta registrar com placar incompleto (null)
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.registrarResultado("Time K", "Time L", 1, null, 0, 0, 0, 0)
        );

        assertTrue(e.getMessage().toLowerCase().contains("placar") ||
                   e.getMessage().toLowerCase().contains("numérico") ||
                   e.getMessage().toLowerCase().contains("preenchid"),
                   "Mensagem de erro deve mencionar campo de placar");

        // Verifica que atributos não foram alterados no BD
        Time timeKDepois = repo.findTimeByNome("Time K");
        assertEquals(jogosAntes, timeKDepois.getJogos(), "Jogos não devem ter mudado");
        assertEquals(0, timeKDepois.getPontos(), "Pontos devem permanecer 0");
    }
}
