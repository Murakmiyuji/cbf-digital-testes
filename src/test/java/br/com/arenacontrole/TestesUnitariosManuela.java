package br.com.arenacontrole;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestesUnitariosManuela {

    @Test
    @DisplayName("CT11: Exibição da Tabela Simples (RF05)")
    void testCT11_ExibicaoTabelaSimples() {
        // Pré-condição: times cadastrados e uma partida A 3x0 B registrada
        Campeonato campeonato = new Campeonato();
        campeonato.cadastrarTime("Time A", "TA");
        campeonato.cadastrarTime("Time B", "TB");
        campeonato.registrarResultado("Time A", "Time B", 3, 0, 0, 0, 0, 0);

        // Ação: exibir a tabela de classificação
        List<Classificacao> tabela = campeonato.exibirTabelaClassificacao();

        // Resultado Esperado: estrutura com 2 linhas e colunas válidas
        assertNotNull(tabela);
        assertEquals(2, tabela.size());

        Classificacao primeiro = tabela.get(0); // Time A deve liderar
        Classificacao segundo  = tabela.get(1); // Time B em seguida

        // Time A (vitória 3x0)
        assertEquals("Time A", primeiro.getNome());
        assertEquals(3,  primeiro.getPG());
        assertEquals(1,  primeiro.getJ());
        assertEquals(1,  primeiro.getV());
        assertEquals(0,  primeiro.getE());
        assertEquals(0,  primeiro.getD());
        assertEquals(3,  primeiro.getGP());
        assertEquals(0,  primeiro.getGC());
        assertEquals(3,  primeiro.getSG());
        assertEquals(0,  primeiro.getCA());
        assertEquals(0,  primeiro.getCV());

        // Time B (derrota 0x3)
        assertEquals("Time B", segundo.getNome());
        assertEquals(0,   segundo.getPG());
        assertEquals(1,   segundo.getJ());
        assertEquals(0,   segundo.getV());
        assertEquals(0,   segundo.getE());
        assertEquals(1,   segundo.getD());
        assertEquals(0,   segundo.getGP());
        assertEquals(3,   segundo.getGC());
        assertEquals(-3,  segundo.getSG());
        assertEquals(0,   segundo.getCA());
        assertEquals(0,   segundo.getCV());
    }
}
