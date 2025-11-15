package br.com.arenacontrole;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes Unitários - Caio Negrelli Fontalva
 *
 * Responsável por testes unitários cobrindo validações avançadas,
 * cenários edge case e funcionalidades complementares.
 *
 * Casos de Teste: CT31-CT40 (conforme Plano de Testes #65)
 * - CT31-CT34: Validação de entrada e tratamento de exceções
 * - CT35-CT38: Cenários de limite e edge cases
 * - CT39-CT40: Verificação de atributos e atualização de dados
 */
@DisplayName("Testes Unitários - Caio")
public class TestesUnitariosCaio {

    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
    }

    // ==================== CT31-CT34: Validação de Entrada ====================

    /**
     * CT31: Bloqueio de Nome Vazio
     * Entrada: cadastrarTime("", "ABC")
     * Resultado Esperado: IllegalArgumentException com mensagem de validação
     * Pós-condição: Time não cadastrado, times.size() permanece igual
     */
    @Test
    @DisplayName("CT31: Bloqueio de Nome Vazio no Cadastro")
    void testCT31_BloqueioNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("", "ABC");
        }, "Deve bloquear cadastro com nome vazio");
        
        assertEquals(0, campeonato.getNumeroTimes(), "Nenhum time deve ser cadastrado");
    }

    /**
     * CT32: Bloqueio de Nome com Espaços em Branco
     * Entrada: cadastrarTime("   ", "ABC")
     * Resultado Esperado: IllegalArgumentException
     * Pós-condição: Time não cadastrado
     */
    @Test
    @DisplayName("CT32: Bloqueio de Nome com Apenas Espaços")
    void testCT32_BloqueioNomeApenaEspacos() {
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("   ", "XYZ");
        }, "Deve bloquear cadastro com nome contendo apenas espaços");
        
        assertEquals(0, campeonato.getNumeroTimes());
    }

    /**
     * CT33: Bloqueio de Nome Nulo
     * Entrada: cadastrarTime(null, "ABC")
     * Resultado Esperado: IllegalArgumentException ou NullPointerException tratado
     * Pós-condição: Time não cadastrado
     */
    @Test
    @DisplayName("CT33: Bloqueio de Nome Nulo")
    void testCT33_BloqueioNomeNulo() {
        assertThrows(Exception.class, () -> {
            campeonato.cadastrarTime(null, "ABC");
        }, "Deve bloquear cadastro com nome nulo");
        
        assertEquals(0, campeonato.getNumeroTimes());
    }

    /**
     * CT34: Bloqueio de Cadastro com Nome Duplicado (Case-Insensitive)
     * Entrada: cadastrarTime("Time A", "A") duas vezes
     * Resultado Esperado: Segunda chamada lança IllegalArgumentException
     * Pós-condição: Apenas 1 time cadastrado
     */
    @Test
    @DisplayName("CT34: Bloqueio de Nome Duplicado (Case-Insensitive)")
    void testCT34_BloqueioNomeDuplicadoCaseInsensitive() {
        campeonato.cadastrarTime("Time A", "TA");
        
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.cadastrarTime("time a", "TX");
        }, "Deve bloquear nome duplicado independente de maiúsculas/minúsculas");
        
        assertEquals(1, campeonato.getNumeroTimes(), "Apenas um time deve estar cadastrado");
    }

    // ==================== CT35-CT38: Cenários Edge Case ====================

    /**
     * CT35: Registro de Resultado com Gols Extremos (0x0)
     * Entrada: registrarResultado("A", "B", 0, 0, 0, 0, 0, 0)
     * Resultado Esperado: Ambos recebem 1 ponto cada (empate)
     * Pós-condição: Ambos com 1 ponto, 1 jogo, 0V 1E 0D
     */
    @Test
    @DisplayName("CT35: Registro de Placar 0x0 (Empate Puro)")
    void testCT35_RegistroPlacar0x0() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        
        campeonato.registrarResultado("Time A", "Time B", 0, 0, 0, 0, 0, 0);
        
        Time timeA = campeonato.buscarTime("Time A");
        Time timeB = campeonato.buscarTime("Time B");
        
        assertEquals(1, timeA.getPontos(), "Time A deve ter 1 ponto");
        assertEquals(1, timeB.getPontos(), "Time B deve ter 1 ponto");
        assertEquals(1, timeA.getJogos(), "Time A deve ter 1 jogo");
        assertEquals(1, timeB.getJogos(), "Time B deve ter 1 jogo");
        assertEquals(0, timeA.getVitorias(), "Time A não deve ter vitórias");
        assertEquals(1, timeA.getEmpates(), "Time A deve ter 1 empate");
        assertEquals(0, timeA.getDerrotas(), "Time A não deve ter derrotas");
    }

    /**
     * CT36: Bloqueio de Partida entre um Time e Ele Mesmo
     * Entrada: registrarResultado("Time A", "Time A", 1, 0, 0, 0, 0, 0)
     * Resultado Esperado: IllegalArgumentException
     * Pós-condição: Atributos de Time A não alterados
     */
    @Test
    @DisplayName("CT36: Bloqueio de Partida entre Mesmo Time")
    void testCT36_BloqueioPartidaMesmoTime() {
        campeonato.cadastrarTime("Time A", "A");
        Time timeA = campeonato.buscarTime("Time A");
        int pontos_antes = timeA.getPontos();
        int jogos_antes = timeA.getJogos();
        
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time A", "Time A", 1, 0, 0, 0, 0, 0);
        }, "Deve bloquear partida entre o mesmo time");
        
        timeA = campeonato.buscarTime("Time A");
        assertEquals(pontos_antes, timeA.getPontos(), "Pontos devem permanecer igual");
        assertEquals(jogos_antes, timeA.getJogos(), "Jogos devem permanecer igual");
    }

    /**
     * CT37: Bloqueio de Resultado com Gols Negativos no Time B
     * Entrada: registrarResultado("A", "B", 2, -1, 0, 0, 0, 0)
     * Resultado Esperado: IllegalArgumentException
     * Pós-condição: Nenhuma alteração nos atributos
     */
    @Test
    @DisplayName("CT37: Bloqueio de Gols Negativos no Time B")
    void testCT37_BloqueioGolsNegativosTimeB() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time A", "Time B", 2, -1, 0, 0, 0, 0);
        }, "Deve bloquear gols negativos");
        
        Time timeA = campeonato.buscarTime("Time A");
        Time timeB = campeonato.buscarTime("Time B");
        assertEquals(0, timeA.getJogos(), "Time A não deve ter jogos registrados");
        assertEquals(0, timeB.getJogos(), "Time B não deve ter jogos registrados");
    }

    /**
     * CT38: Bloqueio de Cartão Amarelo Negativo
     * Entrada: registrarResultado("A", "B", 1, 0, -1, 0, 0, 0)
     * Resultado Esperado: IllegalArgumentException
     * Pós-condição: Sem alteração de dados
     */
    @Test
    @DisplayName("CT38: Bloqueio de Cartão Amarelo Negativo")
    void testCT38_BloqueioCartaoAmarelonegativo() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        
        assertThrows(IllegalArgumentException.class, () -> {
            campeonato.registrarResultado("Time A", "Time B", 1, 0, -1, 0, 0, 0);
        }, "Deve bloquear cartão amarelo negativo");
        
        Time timeA = campeonato.buscarTime("Time A");
        assertEquals(0, timeA.getJogos(), "Time A não deve ter jogos registrados");
    }

    // ==================== CT39-CT40: Verificação de Atributos ====================

    /**
     * CT39: Obtenção de Atributo Válido (pontos)
     * Entrada: obterAtributo("Time A", "pontos") após vitória
     * Resultado Esperado: 3
     * Pós-condição: Sem alteração de estado
     */
    @Test
    @DisplayName("CT39: Obtenção de Atributo Válido (Pontos)")
    void testCT39_ObterAtributoPontos() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        campeonato.registrarResultado("Time A", "Time B", 2, 0, 0, 0, 0, 0);
        
        int pontos = campeonato.obterAtributo("Time A", "pontos");
        assertEquals(3, pontos, "Time A deve ter 3 pontos após vitória");
    }

    /**
     * CT40: Obtenção de Atributo Válido (gols pró)
     * Entrada: obterAtributo("Time A", "golsPro") após registrar 3 gols
     * Resultado Esperado: 3
     * Pós-condição: Sem alteração de estado
     */
    @Test
    @DisplayName("CT40: Obtenção de Atributo Válido (Gols Pró)")
    void testCT40_ObterAtributoGolsPro() {
        campeonato.cadastrarTime("Time A", "A");
        campeonato.cadastrarTime("Time B", "B");
        campeonato.registrarResultado("Time A", "Time B", 3, 1, 0, 0, 0, 0);
        
        int golsPro = campeonato.obterAtributo("Time A", "golsPro");
        assertEquals(3, golsPro, "Time A deve ter 3 gols pró");
    }
}

