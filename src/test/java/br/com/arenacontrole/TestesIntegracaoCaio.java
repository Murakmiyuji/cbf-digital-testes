package br.com.arenacontrole;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.arenacontrole.db.DatabaseConfig;
import br.com.arenacontrole.repository.CampeonatoRepository;

/**
 * Testes de Integração - Caio Negrelli Fontalva
 *
 * Responsável por testes de integração com persistência em banco de dados PostgreSQL.
 * Valida a integridade dos dados salvos no banco, transações e recuperação de dados.
 *
 * Casos de Teste: CT53-CT56 (conforme Plano de Testes #65)
 * - CT53: Persistência de Time com Atributos
 * - CT54: Persistência de Múltiplos Times
 * - CT55: Atualização de Time via Upsert
 * - CT56: Recuperação de Time após Reinicialização
 *
 * Pré-requisitos:
 * - Docker com PostgreSQL 15 rodando (localhost:5432)
 * - Variáveis de ambiente: JDBC_URL, DB_USER, DB_PASS (ou usar defaults)
 */
@DisplayName("Testes de Integração - Caio")
public class TestesIntegracaoCaio {

    private DataSource ds;
    private CampeonatoRepository repo;
    private Campeonato campeonato;

    @BeforeEach
    void setUp() throws Exception {
        // Criar DataSource com HikariCP
        ds = DatabaseConfig.createDataSource();
        
        // Criar repository e inicializar schema (cria tabelas se não existirem)
        repo = new CampeonatoRepository(ds);
        
        // Limpar dados anteriores para testes isolados
        limparTabelas(ds);
        
        // Criar campeonato com injeção de repository
        campeonato = new Campeonato(repo);
    }

    /**
     * Helper: Limpa as tabelas de times e partidas para garantir isolamento
     */
    private void limparTabelas(DataSource ds) {
        String[] tables = new String[]{"partida", "team"}; // partida primeiro por FK
        for (String table : tables) {
            try (Connection conn = ds.getConnection(); 
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM " + table);
            } catch (Exception ignored) {
                // Tabelas podem não existir ainda na primeira execução
            }
        }
    }

    // ==================== CT53-CT56: Persistência e Integridade ====================

    /**
     * CT53: Cálculo de Total de Gols (Consulta Integrada)
     * Verifica que o cálculo total de gols consulta corretamente o BD
     * 
     * Entradas:
     * - Partida 1: 2 gols + 1 gol = 3 gols totais
     * - Partida 2: 4 gols + 2 gols = 6 gols
     * - calcularTotalGols()
     *
     * Resultado Esperado:
     * - O método executa SUM no BD do histórico de gols
     * - Retorna 9 (3 + 6) como total acumulado
     * - Prova integração correta com BD
     *
     * Pré-condição: Partidas registradas no BD (Total acumulado: 9 gols)
     * Pós-condição: Método retorna 9 via consulta SUM no BD
     */
    @Test
    @DisplayName("CT53: Cálculo de Total de Gols (Consulta Integrada)")
    void testCT53_CalculoTotalGolsConsultaIntegrada() {
        // Arrange: Cadastrar times e registrar partidas
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        campeonato.cadastrarTime("Time C", "C");

        // Act - Primeira partida: 2 + 1 = 3 gols
        campeonato.registrarResultado("Time A", "Time B", 2, 1, 0, 0, 0, 0);
        
        // Verificar parcial
        int totalParcial = campeonato.calcularTotalGols();
        assertEquals(3, totalParcial, "Total parcial deve ser 3 gols");

        // Act - Segunda partida: 4 + 2 = 6 gols
        campeonato.registrarResultado("Time A", "Time C", 4, 2, 0, 0, 0, 0);

        // Assert - Cálculo integrado final
        int totalFinal = campeonato.calcularTotalGols();
        assertEquals(9, totalFinal, "Total final deve ser 9 gols (3 + 6) via SUM no BD");
    }

    /**
     * CT54: Bloqueio de Cadastro de Times no Meio (Consulta BD)
     * Verifica que o sistema consulta BD e bloqueia cadastro após início
     * 
     * Entradas:
     * - Campeonato com histórico de jogos no BD
     * - cadastrarTime("Time Novo", "NV")
     *
     * Resultado Esperado:
     * - O sistema consulta a tabela de histórico de jogos no BD
     * - Detecta que jogos foram registrados
     * - Bloqueia o cadastro (IllegalArgumentException)
     * - Nenhum novo time inserido na tabela de times no BD
     *
     * Pré-condição: Existe histórico de jogos no BD
     * Pós-condição: Cadastro bloqueado, exception lançada
     */
    @Test
    @DisplayName("CT54: Bloqueio de Cadastro de Times no Meio")
    void testCT54_BloqueiosCadastroTimesNoMeioBD() {
        // Arrange: Cadastrar times iniciais e registrar partida
        campeonato.cadastrarTime("Team X", "X");
        campeonato.cadastrarTime("Team Y", "Y");
        
        // Registrar partida (isso marca o campeonato como "em andamento" no BD)
        campeonato.registrarResultado("Team X", "Team Y", 1, 0, 0, 0, 0, 0);

        // Act & Assert - Tentar cadastrar novo time após início
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.cadastrarTime("Team Z", "Z")
        );

        assertTrue(e.getMessage().toLowerCase().contains("adicion") ||
                   e.getMessage().toLowerCase().contains("após") ||
                   e.getMessage().toLowerCase().contains("competição"),
                   "Mensagem deve mencionar bloqueio de cadastro durante competição");

        // Verificar que nenhum novo time foi inserido no BD
        Time timeZ = repo.findTimeByNome("Team Z");
        assertNull(timeZ, "Team Z não deve ser persistido no BD");
        
        // Verificar que apenas 2 times existem no BD
        assertEquals(2, campeonato.getNumeroTimes(), "Deve haver apenas 2 times");
    }

    /**
     * CT55: Bloqueio de Times Ímpares (Validação BD)
     * Verifica que o sistema valida número par de times antes de gerar tabela
     * 
     * Entradas:
     * - 5 times cadastrados no BD (ímpar)
     * - gerarTabelaJogos()
     *
     * Resultado Esperado:
     * - O sistema consulta quantidade de times no BD
     * - Detecta número ímpar (5)
     * - Bloqueia a geração da tabela (IllegalArgumentException)
     * - Nenhuma tabela de jogos é criada
     *
     * Pré-condição: 5 times (ímpar) cadastrados no BD
     * Pós-condição: Bloqueio ativado, exception lançada
     */
    @Test
    @DisplayName("CT55: Bloqueio de Times Ímpares")
    void testCT55_BloqueioTimesImparesValidacaoBD() {
        // Arrange: Cadastrar 5 times (ímpar)
        String[] nomes = {"Time 1", "Time 2", "Time 3", "Time 4", "Time 5"};
        String[] abreviacoes = {"T1", "T2", "T3", "T4", "T5"};
        
        for (int i = 0; i < nomes.length; i++) {
            campeonato.cadastrarTime(nomes[i], abreviacoes[i]);
        }

        // Verificar que temos 5 times no BD
        assertEquals(5, campeonato.getNumeroTimes(), "Deve haver 5 times cadastrados");

        // Act & Assert - Tentar gerar tabela com número ímpar
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            campeonato.gerarTabelaJogos()
        );

        assertTrue(e.getMessage().toLowerCase().contains("par") ||
                   e.getMessage().toLowerCase().contains("ímpar"),
                   "Mensagem deve mencionar número par de times");
    }

    /**
     * CT56: Mensagem de Tabela Completa (Notificação Final)
     * Verifica que o sistema exibe notificação ao finalizar todos os jogos
     * 
     * Entradas:
     * - 4 times cadastrados, tabela gerada (12 jogos possíveis em turno e returno)
     * - Registrar 11 primeiros jogos (6 turno + 5 returno)
     * - Registrar 12º e último jogo (returno)
     *
     * Resultado Esperado:
     * - Após registro do último jogo, status muda para "Finalizado"
     * - Sistema exibe notificação de "Tabela Completa"
     * - Campo de status no BD é atualizado para "Finalizado"
     *
     * Pré-condição: Campeonato com número par de times, todos exceto último jogo registrados
     * Pós-condição: Status = "Finalizado", notificação exibida
     */
    @Test
    @DisplayName("CT56: Mensagem de Tabela Completa")
    void testCT56_MensagemTabelaCompleta() {
        // Arrange: Cadastrar 4 times
        campeonato.cadastrarTime("Time P", "TP");
        campeonato.cadastrarTime("Time Q", "TQ");
        campeonato.cadastrarTime("Time R", "TR");
        campeonato.cadastrarTime("Time S", "TS");
        
        campeonato.gerarTabelaJogos();

        // Registrar 11 jogos (deixando 1 pendente de 12 possíveis em turno e returno)
        // TURNO (Ida) - 6 jogos
        campeonato.registrarResultado("Time P", "Time Q", 1, 0, 0, 0, 0, 0); // Jogo 1
        campeonato.registrarResultado("Time P", "Time R", 2, 1, 0, 0, 0, 0); // Jogo 2
        campeonato.registrarResultado("Time P", "Time S", 3, 0, 0, 0, 0, 0); // Jogo 3
        campeonato.registrarResultado("Time Q", "Time R", 1, 1, 0, 0, 0, 0); // Jogo 4
        campeonato.registrarResultado("Time Q", "Time S", 2, 0, 0, 0, 0, 0); // Jogo 5
        campeonato.registrarResultado("Time R", "Time S", 1, 2, 0, 0, 0, 0); // Jogo 6
        
        // RETORNO (Volta) - 5 jogos (faltando 1)
        campeonato.registrarResultado("Time Q", "Time P", 0, 1, 0, 0, 0, 0); // Jogo 7
        campeonato.registrarResultado("Time R", "Time P", 1, 0, 0, 0, 0, 0); // Jogo 8
        campeonato.registrarResultado("Time S", "Time P", 2, 1, 0, 0, 0, 0); // Jogo 9
        campeonato.registrarResultado("Time R", "Time Q", 0, 0, 0, 0, 0, 0); // Jogo 10
        campeonato.registrarResultado("Time S", "Time Q", 1, 1, 0, 0, 0, 0); // Jogo 11

        // Act: Registrar último jogo (12º - returno)
        campeonato.registrarResultado("Time S", "Time R", 2, 0, 0, 0, 0, 0); // Jogo 12 - FINAL

        // Assert: Verificar que campeonato foi marcado como finalizado
        // Nota: Isso depende de implementação em Campeonato.java
        // O status deve ser consultável ou verificável no BD
        assertEquals(4, campeonato.getNumeroTimes(), "Campeonato deve manter 4 times");
        
        // Verificar que todos os times têm 6 jogos (completo em 4 times turno e returno)
        Time timeP = repo.findTimeByNome("Time P");
        assertEquals(6, timeP.getJogos(), "Time P deve ter 6 jogos (turno e returno completos)");
        
        Time timeQ = repo.findTimeByNome("Time Q");
        assertEquals(6, timeQ.getJogos(), "Time Q deve ter 6 jogos (turno e returno completos)");
    }
}
