package br.com.arenacontrole;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes Unitários - Caio Negrelli Fontalva
 *
 * Responsável por testes unitários cobrindo:
 * - CT31-CT32: Cálculo de Total de Gols (RF13)
 * - CT33-CT34: Bloqueio de Cadastro no Meio (RF14)
 * - CT35-CT37: Bloqueio de Times Ímpares (RF15)
 * - CT38-CT40: Mensagem de Tabela Completa (RF16)
 *
 * Casos de Teste: CT31-CT40 (conforme Plano de Testes #65)
 */
@DisplayName("Testes Unitários - Caio")
public class TestesUnitariosCaio {

    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
    }

    // ==================== CT31-CT32: Cálculo de Total de Gols (RF13) ====================

    /**
     * CT31: Cálculo de Total de Gols (Partida Única)
     * Pré-condição: Nenhuma partida registrada (Total Gols = 0)
     * Entrada: registrarResultado("A", "B", 2, 1, 0, 0, 0, 0) + calcularTotalGols()
     * Resultado Esperado: O sistema deve retornar 3 (total geral de gols: 2+1)
     */
    @Test
    @DisplayName("CT31: Cálculo de Total de Gols (Partida Única)")
    void testCT31_CalculoTotalGolsPartidaUnica() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        
        // Verificar que antes não há gols
        assertEquals(0, campeonato.calcularTotalGols(), "Total de gols deve ser 0 inicialmente");
        
        // Registrar partida: A 2 x 1 B
        campeonato.registrarResultado("A", "B", 2, 1, 0, 0, 0, 0);
        
        // Verificar total de gols
        assertEquals(3, campeonato.calcularTotalGols(), "Total de gols deve ser 3 (2+1)");
    }

    /**
     * CT32: Cálculo de Total de Gols (Acúmulo)
     * Pré-condição: Total de Gols anterior = 3
     * Entrada: registrarResultado("C", "D", 3, 3, 0, 0, 0, 0) + calcularTotalGols()
     * Resultado Esperado: O sistema deve retornar 9 (3 anterior + 6 novos = 9)
     */
    @Test
    @DisplayName("CT32: Cálculo de Total de Gols (Acúmulo)")
    void testCT32_CalculoTotalGolsAcumulo() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");
        
        // Primeira partida: A 2 x 1 B (total = 3)
        campeonato.registrarResultado("A", "B", 2, 1, 0, 0, 0, 0);
        assertEquals(3, campeonato.calcularTotalGols(), "Total deve ser 3 após primeira partida");
        
        // Segunda partida: C 3 x 3 D (total = 6 + 3 anterior = 9)
        campeonato.registrarResultado("C", "D", 3, 3, 0, 0, 0, 0);
        assertEquals(9, campeonato.calcularTotalGols(), "Total deve ser 9 (3 + 6)");
    }

    // ==================== CT33-CT34: Bloqueio de Cadastro no Meio (RF14) ====================

    /**
     * CT33: Bloqueio de Cadastro de Times no Meio
     * Pré-condição: O campeonato está em andamento (Pelo menos 1 partida registrada)
     * Entrada: cadastrarTime("Time Z", "Z")
     * Resultado Esperado: O sistema deve impedir o cadastro e exibir mensagem de erro
     */
    @Test
    @DisplayName("CT33: Bloqueio de Cadastro no Meio do Campeonato")
    void testCT33_BloqueiosCadastroTimesNoMeio() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        
        // Registrar uma partida (campeonato iniciado)
        campeonato.registrarResultado("A", "B", 1, 0, 0, 0, 0, 0);
        
        // Tentar cadastrar novo time deve bloquear
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("Time Z", "Z");
        }, "Deve bloquear cadastro após início da competição");
        
        assertEquals(2, campeonato.getNumeroTimes(), "Devem permanecer apenas 2 times cadastrados");
    }

    /**
     * CT34: Permitir Cadastro de Times (Sem Partidas)
     * Pré-condição: O campeonato ainda não começou (Nenhuma partida registrada)
     * Entrada: cadastrarTime("Time Y", "Y")
     * Resultado Esperado: O sistema deve permitir o cadastro com sucesso
     */
    @Test
    @DisplayName("CT34: Permitir Cadastro de Times (Sem Partidas)")
    void testCT34_PermitirCadastroDeTimes() {
        // Campeonato vazio, nenhuma partida registrada
        campeonato.cadastrarTime("Time X", "X");
        campeonato.cadastrarTime("Time Y", "Y");
        
        assertEquals(2, campeonato.getNumeroTimes(), "Deve permitir cadastro de 2 times");
        assertNotNull(campeonato.buscarTime("Time X"), "Time X deve existir");
        assertNotNull(campeonato.buscarTime("Time Y"), "Time Y deve existir");
    }

    // ==================== CT35-CT37: Bloqueio de Times Ímpares (RF15) ====================

    /**
     * CT35: Bloqueio de Times Ímpares (Valor Limite Inferior - 3 times)
     * Pré-condição: 3 Times cadastrados (ímpar)
     * Entrada: gerarTabelaJogos()
     * Resultado Esperado: O sistema deve impedir a geração e exibir erro
     */
    @Test
    @DisplayName("CT35: Bloqueio de Times Ímpares (3 Times)")
    void testCT35_BloqueioTimesImparesValorBaixo() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        campeonato.cadastrarTime("C", "C");
        
        // Tentar gerar tabela com número ímpar deve bloquear
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.gerarTabelaJogos();
        }, "Deve bloquear geração de tabela com número ímpar de times");
    }

    /**
     * CT36: Bloqueio de Times Ímpares (Valor Alto - 21 times)
     * Pré-condição: 21 Times cadastrados (ímpar)
     * Entrada: gerarTabelaJogos()
     * Resultado Esperado: O sistema deve impedir a geração e exibir erro
     */
    @Test
    @DisplayName("CT36: Bloqueio de Times Ímpares (21 Times)")
    void testCT36_BloqueioTimesImparesValorAlto() {
        // Cadastrar 21 times (ímpar)
        for (int i = 1; i <= 21; i++) {
            campeonato.cadastrarTime("Time " + i, "T" + i);
        }
        
        assertEquals(21, campeonato.getNumeroTimes(), "Deve ter 21 times cadastrados");
        
        // Tentar gerar tabela com número ímpar deve bloquear
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.gerarTabelaJogos();
        }, "Deve bloquear geração de tabela com 21 times (ímpar)");
    }

    /**
     * CT37: Permissão de Times Pares (4 times)
     * Pré-condição: 4 Times cadastrados (par)
     * Entrada: gerarTabelaJogos()
     * Resultado Esperado: O sistema deve gerar a tabela de jogos com sucesso
     */
    @Test
    @DisplayName("CT37: Permissão de Times Pares (4 Times)")
    void testCT37_PermissaoTimesPares() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");
        
        // Gerar tabela com número par deve funcionar
        assertDoesNotThrow(() -> {
            campeonato.gerarTabelaJogos();
        }, "Deve permitir geração de tabela com número par de times");
    }

    // ==================== CT38-CT40: Mensagem de Tabela Completa (RF16) ====================

    /**
     * CT38: Mensagem de Tabela Completa
     * Pré-condição: 4 Times, Tabela gerada, Todos os 6 jogos registrados (round-robin)
     * Entrada: registrarResultado (último jogo)
     * Resultado Esperado: Sistema marca status = "Finalizado"
     */
    @Test
    @DisplayName("CT38: Mensagem de Tabela Completa")
    void testCT38_MensagemTabelaCompleta() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");
        
        campeonato.gerarTabelaJogos();
        
        // Registrar 5 primeiras partidas (faltando 1 de 6 possíveis em 4 times round-robin)
        campeonato.registrarResultado("A", "B", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("A", "C", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("A", "D", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("B", "C", 0, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("B", "D", 1, 1, 0, 0, 0, 0);
        
        // Antes da última, status ainda é "Em Andamento"
        assertEquals("Em Andamento", campeonato.obterStatus(), 
                    "Status deve ser 'Em Andamento' antes de completar");
        
        // Registrar última partida (6ª)
        campeonato.registrarResultado("C", "D", 2, 1, 0, 0, 0, 0);
        
        // Verificar que status mude para "Finalizado"
        assertEquals("Finalizado", campeonato.obterStatus(), 
                    "Status deve ser 'Finalizado' após completar todas as partidas");
    }

    /**
     * CT39: Mensagem de Tabela Incompleta
     * Pré-condição: 4 Times, Tabela gerada, 5 jogos registrados (faltando 1)
     * Entrada: Verificação de status
     * Resultado Esperado: Sistema exibe status = "Em Andamento", NÃO "Finalizado"
     */
    @Test
    @DisplayName("CT39: Mensagem de Tabela Incompleta")
    void testCT39_MensagemTabelaIncompleta() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");
        
        campeonato.gerarTabelaJogos();
        
        // Registrar apenas 5 partidas (faltando 1)
        campeonato.registrarResultado("A", "B", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("A", "C", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("A", "D", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("B", "C", 0, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("B", "D", 1, 1, 0, 0, 0, 0);
        
        // Status deve ser "Em Andamento" (ainda faltam jogos)
        assertEquals("Em Andamento", campeonato.obterStatus(), 
                    "Status deve ser 'Em Andamento' quando há jogos pendentes");
    }

    /**
     * CT40: Bloqueio de Cadastro de Times (Campeonato Finalizado)
     * Pré-condição: Campeonato finalizado (status "Finalizado")
     * Entrada: cadastrarTime("Time W", "W")
     * Resultado Esperado: Sistema deve bloquear e exibir erro
     */
    @Test
    @DisplayName("CT40: Bloqueio de Cadastro (Campeonato Finalizado)")
    void testCT40_BloqueiosCadastroCampeonatoFinalizado() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");
        
        campeonato.gerarTabelaJogos();
        campeonato.registrarResultado("A", "B", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("B", "A", 2, 1, 0, 0, 0, 0);
        
        // Marcar como finalizado
        campeonato.setStatus("Finalizado");
        
        // Tentar cadastrar novo time deve bloquear
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("Time W", "W");
        }, "Deve bloquear cadastro quando campeonato está finalizado");
    }
}

