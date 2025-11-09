package br.com.arenacontrole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários - Responsável: Samuel
 * RF01: Cadastro de Times
 * RF02: Definição de Atributos dos Times
 * RF03: Registro de Resultados de Partidas
 * RF04: Atualização Automática de Atributos (Cálculo 3-1-0)
 */
@DisplayName("Testes Unitários - Samuel")
public class TestesUnitariosSamuel {

    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
    }

    // ==================== RF01: Cadastro de Times ====================

    @Test
    @DisplayName("CT01: Cadastro Básico com Nome e Abreviação")
    void testCT01_CadastroBasicoComNomeEAbreviacao() {
        // Entradas
        boolean resultado = campeonato.cadastrarTime("São Paulo", "SAO");
        
        // Resultado Esperado: Sucesso
        assertTrue(resultado);
        assertEquals(1, campeonato.getNumeroTimes());
        
        Time time = campeonato.buscarTime("São Paulo");
        assertNotNull(time);
        assertEquals("São Paulo", time.getNome());
        assertEquals("SAO", time.getAbreviacao());
    }

    // ==================== RF02: Definição de Atributos dos Times ====================

    @Test
    @DisplayName("CT02: Verificação de Inicialização de Atributos")
    void testCT02_VerificacaoInicializacaoAtributos() {
        // Pré-condição: Cadastrar time Vasco
        campeonato.cadastrarTime("Vasco", "VAS");
        Time vasco = campeonato.buscarTime("Vasco");
        
        // Resultado Esperado: Todos atributos zerados
        assertEquals(0, vasco.getPontos());
        assertEquals(0, vasco.getJogos());
        assertEquals(0, vasco.getVitorias());
        assertEquals(0, vasco.getEmpates());
        assertEquals(0, vasco.getDerrotas());
        assertEquals(0, vasco.getGolsPro());
        assertEquals(0, vasco.getGolsContra());
        assertEquals(0, vasco.getSaldoGols());
        assertEquals(0, vasco.getCartoesAmarelos());
        assertEquals(0, vasco.getCartoesVermelhos());
    }

    @Test
    @DisplayName("CT03: Verificação de Manutenção de Atributos Não Afetados")
    void testCT03_VerificacaoManutencaoAtributosNaoAfetados() {
        // Pré-condição: Cadastrar Palmeiras e Corinthians
        campeonato.cadastrarTime("Palmeiras", "PAL");
        campeonato.cadastrarTime("Corinthians", "COR");
        
        // Registrar empate 1x1
        campeonato.registrarResultado("Palmeiras", "Corinthians", 1, 1, 0, 0, 0, 0);
        
        Time palmeiras = campeonato.buscarTime("Palmeiras");
        
        // Resultado Esperado: Vitórias e Derrotas devem ser 0
        assertEquals(0, palmeiras.getVitorias());
        assertEquals(0, palmeiras.getDerrotas());
        assertEquals(1, palmeiras.getEmpates());
        assertEquals(1, palmeiras.getPontos());
    }

    // ==================== RF03: Registro de Resultados de Partidas ====================

    @Test
    @DisplayName("CT04: Registro de Placar com Cartões e Gols")
    void testCT04_RegistroPlacarComCartoesEGols() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("São Paulo", "SAO");
        campeonato.cadastrarTime("Santos", "SAN");
        
        // Registrar São Paulo 3x1 Santos (CA: 2x1, CV: 0x0)
        campeonato.registrarResultado("São Paulo", "Santos", 3, 1, 2, 0, 1, 0);
        
        Time saoPaulo = campeonato.buscarTime("São Paulo");
        Time santos = campeonato.buscarTime("Santos");
        
        // Resultado Esperado: Jogo registrado corretamente
        assertEquals(3, saoPaulo.getPontos());
        assertEquals(3, saoPaulo.getGolsPro());
        assertEquals(1, saoPaulo.getGolsContra());
        assertEquals(2, saoPaulo.getCartoesAmarelos());
        
        assertEquals(0, santos.getPontos());
        assertEquals(1, santos.getGolsPro());
        assertEquals(3, santos.getGolsContra());
    }

    @Test
    @DisplayName("CT05: Registro de Placar Extremo (Goleada)")
    void testCT05_RegistroPlacarExtremo() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("Flamengo", "FLA");
        campeonato.cadastrarTime("Botafogo", "BOT");
        
        // Registrar Flamengo 10x0 Botafogo
        campeonato.registrarResultado("Flamengo", "Botafogo", 10, 0, 0, 0, 0, 0);
        
        Time flamengo = campeonato.buscarTime("Flamengo");
        
        // Resultado Esperado: Sistema aceita 10x0
        assertEquals(3, flamengo.getPontos());
        assertEquals(10, flamengo.getGolsPro());
        assertEquals(0, flamengo.getGolsContra());
        assertEquals(10, flamengo.getSaldoGols());
    }

    // ==================== RF04: Atualização Automática de Atributos ====================

    @Test
    @DisplayName("CT06: Cálculo Automático de Pontos (Vitória - 3 pts)")
    void testCT06_CalculoAutomaticoPontosVitoria() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("Grêmio", "GRE");
        campeonato.cadastrarTime("Inter", "INT");
        
        // Registrar Grêmio 2x0 Inter
        campeonato.registrarResultado("Grêmio", "Inter", 2, 0, 0, 0, 0, 0);
        
        Time gremio = campeonato.buscarTime("Grêmio");
        Time inter = campeonato.buscarTime("Inter");
        
        // Resultado Esperado: Grêmio 3 pts, Inter 0 pts
        assertEquals(3, gremio.getPontos());
        assertEquals(0, inter.getPontos());
    }

    @Test
    @DisplayName("CT07: Cálculo Automático de Pontos (Empate - 1 pt)")
    void testCT07_CalculoAutomaticoPontosEmpate() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("Cruzeiro", "CRU");
        campeonato.cadastrarTime("Atlético-MG", "ATL");
        
        // Registrar Cruzeiro 1x1 Atlético-MG
        campeonato.registrarResultado("Cruzeiro", "Atlético-MG", 1, 1, 0, 0, 0, 0);
        
        Time cruzeiro = campeonato.buscarTime("Cruzeiro");
        Time atletico = campeonato.buscarTime("Atlético-MG");
        
        // Resultado Esperado: Ambos com 1 pt
        assertEquals(1, cruzeiro.getPontos());
        assertEquals(1, atletico.getPontos());
    }

    @Test
    @DisplayName("CT08: Cálculo Automático de Gols e Saldo")
    void testCT08_CalculoAutomaticoGolsESaldo() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("Fluminense", "FLU");
        campeonato.cadastrarTime("Vasco", "VAS");
        
        // Registrar Fluminense 5x2 Vasco
        campeonato.registrarResultado("Fluminense", "Vasco", 5, 2, 0, 0, 0, 0);
        
        Time fluminense = campeonato.buscarTime("Fluminense");
        Time vasco = campeonato.buscarTime("Vasco");
        
        // Resultado Esperado: SG Fluminense = +3, SG Vasco = -3
        assertEquals(3, fluminense.getSaldoGols());
        assertEquals(-3, vasco.getSaldoGols());
    }

    @Test
    @DisplayName("CT09: Incremento Automático de Jogos Disputados")
    void testCT09_IncrementoAutomaticoJogosDisputados() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("Bahia", "BAH");
        campeonato.cadastrarTime("Vitória", "VIT");
        
        // Registrar Bahia 0x0 Vitória
        campeonato.registrarResultado("Bahia", "Vitória", 0, 0, 0, 0, 0, 0);
        
        Time bahia = campeonato.buscarTime("Bahia");
        Time vitoria = campeonato.buscarTime("Vitória");
        
        // Resultado Esperado: Ambos com 1 jogo disputado
        assertEquals(1, bahia.getJogos());
        assertEquals(1, vitoria.getJogos());
    }

    @Test
    @DisplayName("CT10: Incremento Automático de V/E/D")
    void testCT10_IncrementoAutomaticoVED() {
        // Pré-condição: Cadastrar times
        campeonato.cadastrarTime("Ceará", "CEA");
        campeonato.cadastrarTime("Fortaleza", "FOR");
        
        // Registrar Ceará 4x1 Fortaleza
        campeonato.registrarResultado("Ceará", "Fortaleza", 4, 1, 0, 0, 0, 0);
        
        Time ceara = campeonato.buscarTime("Ceará");
        Time fortaleza = campeonato.buscarTime("Fortaleza");
        
        // Resultado Esperado: Ceará 1 vitória, Fortaleza 1 derrota
        assertEquals(1, ceara.getVitorias());
        assertEquals(1, fortaleza.getDerrotas());
    }
}

