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

    @Test
    @DisplayName("CT12: Ordenação por Pontos (Prioridade Máxima)")
    void testCT12_OrdenacaoPorPontos() {
        // Pré-condição: cadastrar times e configurar pontos via partidas
        Campeonato campeonato = new Campeonato();
        campeonato.cadastrarTime("Time A", "A"); // A terá 3 pontos
        campeonato.cadastrarTime("Time B", "B"); // B terá 1 ponto
        campeonato.cadastrarTime("Time X", "X"); // adversário de A
        campeonato.cadastrarTime("Time Y", "Y"); // adversário de B

        // Ação para gerar os pontos:
        // Time A vence Time X -> +3 pontos para A
        campeonato.registrarResultado("Time A", "Time X", 1, 0, 0, 0, 0, 0);
        // Time B empata com Time Y -> +1 ponto para B
        campeonato.registrarResultado("Time B", "Time Y", 0, 0, 0, 0, 0, 0);

        // Ação: ordenar a tabela
        List<Time> ordenada = campeonato.ordenarTabela();

        // Resultado Esperado: Time A (PG=3) vem antes do Time B (PG=1)
        int idxA = indexOf(ordenada, "Time A");
        int idxB = indexOf(ordenada, "Time B");

        assertTrue(idxA >= 0, "Time A deve existir na tabela");
        assertTrue(idxB >= 0, "Time B deve existir na tabela");
        assertTrue(idxA < idxB, "Time A (3 pts) deve aparecer acima do Time B (1 pt)");
    }
    private int indexOf(List<Time> lista, String nome) {
        int i = 0;
        for (Time t : lista) {
            if (t.getNome().equalsIgnoreCase(nome)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
