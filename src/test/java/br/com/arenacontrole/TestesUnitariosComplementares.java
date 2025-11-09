package br.com.arenacontrole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Testes Unitários Complementares
 * RF05: Exibição da Tabela de Classificação
 * RF06: Ordenação da Tabela (Critérios de Desempate)
 * RF08: Remoção de Times
 * RF09: Bloqueio de Placar Negativo
 * RF10: Bloqueio de Times Duplicados
 * RF11: Validação de Cadastro
 * RF13: Cálculo de Total de Gols
 * RF15: Bloqueio de Times Ímpares
 */
@DisplayName("Testes Unitários Complementares")
public class TestesUnitariosComplementares {

    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
    }

    // ==================== RF05 e RF06: Tabela e Ordenação ====================

    @Test
    @DisplayName("CT12: Ordenação por Pontos (Prioridade Máxima)")
    void testCT12_OrdenacaoPorPontos() {
        // Criar times e registrar resultados
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        campeonato.cadastrarTime("Time C", "C");
        
        // Time A ganha de C (3 pts)
        campeonato.registrarResultado("Time A", "Time C", 1, 0, 0, 0, 0, 0);
        // Time B empata com C (1 pt)
        campeonato.registrarResultado("Time B", "Time C", 0, 0, 0, 0, 0, 0);
        
        List<Time> tabela = campeonato.obterTabelaClassificacao();
        
        // Resultado Esperado: Time A em 1º (3 pts), Time B em 2º (1 pt), Time C em 3º (0 pts)
        assertEquals("Time A", tabela.get(0).getNome());
        assertEquals("Time B", tabela.get(1).getNome());
        assertEquals("Time C", tabela.get(2).getNome());
    }

    @Test
    @DisplayName("CT14: Desempate por Saldo de Gols (2º Critério)")
    void testCT14_DesempatePorSaldoDeGols() {
        // Criar times com mesmos pontos e vitórias
        campeonato.cadastrarTime("Time E", "E");
        campeonato.cadastrarTime("Time F", "F");
        campeonato.cadastrarTime("Time X", "X");
        campeonato.cadastrarTime("Time Y", "Y");
        
        // Time E vence por 4 gols de diferença
        campeonato.registrarResultado("Time E", "Time X", 5, 1, 0, 0, 0, 0);
        // Time F vence por 2 gols de diferença
        campeonato.registrarResultado("Time F", "Time Y", 3, 1, 0, 0, 0, 0);
        
        List<Time> tabela = campeonato.obterTabelaClassificacao();
        
        // Resultado Esperado: Time E (SG=+4) acima de Time F (SG=+2)
        assertEquals(4, tabela.get(0).getSaldoGols());
        assertEquals(2, tabela.get(1).getSaldoGols());
    }

    // ==================== RF08: Remoção de Times ====================

    @Test
    @DisplayName("CT19: Remoção de Time com Sucesso")
    void testCT19_RemocaoTimeComSucesso() {
        // Cadastrar time sem jogos
        campeonato.cadastrarTime("Time G", "G");
        assertEquals(1, campeonato.getNumeroTimes());
        
        // Remover time
        boolean resultado = campeonato.removerTime("Time G");
        
        // Resultado Esperado: Sucesso
        assertTrue(resultado);
        assertEquals(0, campeonato.getNumeroTimes());
        assertNull(campeonato.buscarTime("Time G"));
    }

    @Test
    @DisplayName("CT20: Bloqueio de Remoção de Time")
    void testCT20_BloqueioRemocaoTime() {
        // Cadastrar times e registrar partida
        campeonato.cadastrarTime("Time H", "H");
        campeonato.cadastrarTime("Time I", "I");
        campeonato.registrarResultado("Time H", "Time I", 1, 0, 0, 0, 0, 0);
        
        // Tentar remover time com histórico
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            campeonato.removerTime("Time H");
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("Não é possível remover time com partidas registradas"));
    }

    // ==================== RF09: Bloqueio de Placar Negativo ====================

    @Test
    @DisplayName("CT21: Bloqueio de Placar Negativo (Gols Pró)")
    void testCT21_BloqueioDePlacarNegativo() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        
        // Tentar registrar resultado com gols negativos
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time A", "Time B", -1, 3, 0, 0, 0, 0);
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("negativos"));
    }

    @Test
    @DisplayName("CT22: Bloqueio de Placar Negativo (Cartões Amarelos)")
    void testCT22_BloqueioCartaoNegativo() {
        campeonato.cadastrarTime("Time C", "C");
        campeonato.cadastrarTime("Time D", "D");
        
        // Tentar registrar resultado com cartões negativos
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time C", "Time D", 1, 1, -2, 0, 0, 0);
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("negativos"));
    }

    // ==================== RF10: Bloqueio de Times Duplicados ====================

    @Test
    @DisplayName("CT23: Bloqueio de Times Duplicados (Registro)")
    void testCT23_BloqueioTimesDuplicados() {
        campeonato.cadastrarTime("Time E", "E");
        
        // Tentar registrar partida do time contra ele mesmo
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time E", "Time E", 2, 2, 0, 0, 0, 0);
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("não pode jogar contra si mesmo"));
    }

    // ==================== RF11: Validação de Cadastro ====================

    @Test
    @DisplayName("CT24: Validação de Cadastro de Times (Nome em Branco)")
    void testCT24_ValidacaoCadastroNomeEmBranco() {
        // Tentar cadastrar time com nome vazio
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("", "SEM");
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("obrigatório"));
    }

    @Test
    @DisplayName("CT28: Validação de Cadastro de Times (Abreviatura com Espaço)")
    void testCT28_ValidacaoAbreviaturaComEspaco() {
        // Tentar cadastrar time com abreviatura com espaço
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("Time Validação", "TV A");
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("não pode conter espaços"));
    }

    // ==================== RF13: Cálculo de Total de Gols ====================

    @Test
    @DisplayName("CT31: Cálculo de Total de Gols (Partida Única)")
    void testCT31_CalculoTotalGolsPartidaUnica() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        
        // Registrar partida A 2x1 B (Total: 3 gols)
        campeonato.registrarResultado("A", "B", 2, 1, 0, 0, 0, 0);
        
        // Resultado Esperado: Total = 3 gols
        assertEquals(3, campeonato.calcularTotalGols());
    }

    @Test
    @DisplayName("CT32: Cálculo de Total de Gols (Acúmulo)")
    void testCT32_CalculoTotalGolsAcumulo() {
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");
        campeonato.cadastrarTime("E", "E");
        campeonato.cadastrarTime("F", "F");
        
        // Registrar C 2x1 D (3 gols)
        campeonato.registrarResultado("C", "D", 2, 1, 0, 0, 0, 0);
        // Registrar E 3x3 F (6 gols)
        campeonato.registrarResultado("E", "F", 3, 3, 0, 0, 0, 0);
        
        // Resultado Esperado: Total = 9 gols (3 + 6)
        assertEquals(9, campeonato.calcularTotalGols());
    }

    // ==================== RF15: Bloqueio de Times Ímpares ====================

    @Test
    @DisplayName("CT35: Bloqueio de Times Ímpares (Valor Limite Inferior)")
    void testCT35_BloqueioTimesImpares3Times() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        campeonato.cadastrarTime("Time C", "C");
        
        // Tentar gerar tabela com 3 times (ímpar)
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            campeonato.gerarTabelaJogos();
        });
        
        // Resultado Esperado: Erro
        assertTrue(exception.getMessage().contains("deve ser par"));
    }

    @Test
    @DisplayName("CT37: Permissão de Times Pares")
    void testCT37_PermissaoTimesPares() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        campeonato.cadastrarTime("Time C", "C");
        campeonato.cadastrarTime("Time D", "D");
        
        // Gerar tabela com 4 times (par)
        boolean resultado = campeonato.gerarTabelaJogos();
        
        // Resultado Esperado: Sucesso
        assertTrue(resultado);
        assertTrue(campeonato.isTabelaGerada());
    }
}

