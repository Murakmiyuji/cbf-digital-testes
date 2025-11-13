package br.com.arenacontrole;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestesUnitariosManuela {

    private Campeonato campeonato;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();
    }

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

    @Test
    @DisplayName("CT13: Desempate por Vitórias (1º Critério)")
    void testCT13_DesempatePorVitorias() {
        // Pré-condição: cadastrar times
        Campeonato campeonato = new Campeonato();
        campeonato.cadastrarTime("Time C", "C"); // terá V=2 e 6 pts
        campeonato.cadastrarTime("Time D", "D"); // terá V=1 e 6 pts
        campeonato.cadastrarTime("O1", "O1");
        campeonato.cadastrarTime("O2", "O2");
        campeonato.cadastrarTime("O3", "O3");
        campeonato.cadastrarTime("O4", "O4");

        // Construir pontos para empatar em PG, mas diferenciar por Vitórias:
        // Time C: 2 vitórias (vs O1, O2) e 1 derrota (vs O3) -> 6 pts, V=2
        campeonato.registrarResultado("Time C", "O1", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time C", "O2", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time C", "O3", 0, 1, 0, 0, 0, 0);

        // Time D: 1 vitória (vs O1) e 3 empates (vs O2, O3, O4) -> 6 pts, V=1
        campeonato.registrarResultado("Time D", "O1", 1, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time D", "O2", 0, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time D", "O3", 0, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time D", "O4", 0, 0, 0, 0, 0, 0);

        // Ação: ordenar a tabela
        List<Time> ordenada = campeonato.ordenarTabela();

        // Resultado Esperado: Time C (mesmos pontos, mais vitórias) acima de Time D
        int idxC = indexOf(ordenada, "Time C");
        int idxD = indexOf(ordenada, "Time D");

        assertTrue(idxC >= 0, "Time C deve existir na tabela");
        assertTrue(idxD >= 0, "Time D deve existir na tabela");
        assertTrue(idxC < idxD, "Time C (V=2) deve aparecer acima do Time D (V=1) quando pontos são iguais");
    }

    @Test
    @DisplayName("CT14: Desempate por Saldo de Gols (2º Critério)")
    void testCT14_DesempatePorSaldoDeGols() {
        // Pré-condição: cadastrar times
        Campeonato campeonato = new Campeonato();
        campeonato.cadastrarTime("Time E", "E"); // SG = 4
        campeonato.cadastrarTime("Time F", "F"); // SG = 2
        campeonato.cadastrarTime("O1", "O1");
        campeonato.cadastrarTime("O2", "O2");
        campeonato.cadastrarTime("O3", "O3");
        campeonato.cadastrarTime("O4", "O4");

        // Construir cenário com MESMOS pontos e MESMAS vitórias, mas SG diferente:
        // Time E: 1 vitória (4x0) + 1 empate (0x0) -> PG=4, V=1, SG=+4
        campeonato.registrarResultado("Time E", "O1", 4, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time E", "O2", 0, 0, 0, 0, 0, 0);

        // Time F: 1 vitória (2x0) + 1 empate (0x0) -> PG=4, V=1, SG=+2
        campeonato.registrarResultado("Time F", "O3", 2, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time F", "O4", 0, 0, 0, 0, 0, 0);

        // Ação: ordenar a tabela
        List<Time> ordenada = campeonato.ordenarTabela();

        // Resultado Esperado: Time E (SG=4) acima de Time F (SG=2) com pontos e vitórias iguais
        int idxE = indexOf(ordenada, "Time E");
        int idxF = indexOf(ordenada, "Time F");

        assertTrue(idxE >= 0, "Time E deve existir na tabela");
        assertTrue(idxF >= 0, "Time F deve existir na tabela");
        assertTrue(idxE < idxF, "Time E (SG=4) deve aparecer acima do Time F (SG=2) com PG e V iguais");
    }
    @Test
    @DisplayName("CT15: Desempate por Gols Pró (3º Critério)")
    void testCT15_DesempatePorGolsPro() {
        // Pré-condição: cadastrar times
        Campeonato campeonato = new Campeonato();
        campeonato.cadastrarTime("Time G", "G"); // GP = 10
        campeonato.cadastrarTime("Time H", "H"); // GP = 8
        campeonato.cadastrarTime("O1", "O1");
        campeonato.cadastrarTime("O2", "O2");
        campeonato.cadastrarTime("O3", "O3");
        campeonato.cadastrarTime("O4", "O4");

        // Construir cenário com MESMOS pontos (4) e MESMAS vitórias (1),
        // MESMO saldo (SG=+2), mas GP diferente (G=10, H=8):
        // Time G: vitória 6x4 (+3 pts, SG +2) + empate 4x4 (+1 pt, SG 0) => PG=4, V=1, SG=+2, GP=10
        campeonato.registrarResultado("Time G", "O1", 6, 4, 0, 0, 0, 0);
        campeonato.registrarResultado("Time G", "O2", 4, 4, 0, 0, 0, 0);

        // Time H: vitória 5x3 (+3 pts, SG +2) + empate 3x3 (+1 pt, SG 0) => PG=4, V=1, SG=+2, GP=8
        campeonato.registrarResultado("Time H", "O3", 5, 3, 0, 0, 0, 0);
        campeonato.registrarResultado("Time H", "O4", 3, 3, 0, 0, 0, 0);

        // Ação: ordenar a tabela
        List<Time> ordenada = campeonato.ordenarTabela();

        // Resultado Esperado: Time G (GP=10) acima de Time H (GP=8) com PG, V e SG iguais
        int idxG = indexOf(ordenada, "Time G");
        int idxH = indexOf(ordenada, "Time H");

        assertTrue(idxG >= 0, "Time G deve existir na tabela");
        assertTrue(idxH >= 0, "Time H deve existir na tabela");
        assertTrue(idxG < idxH, "Time G (GP=10) deve aparecer acima do Time H (GP=8) com PG, V e SG iguais");
    }

    @Test
    @DisplayName("CT16: Desempate por Cartões (4º e 5º Critérios)")
    void testCT16_DesempatePorCartoes() {
        // Pré-condição: cadastrar times
        Campeonato campeonato = new Campeonato();
        campeonato.cadastrarTime("Time J", "J"); // CV = 0
        campeonato.cadastrarTime("Time I", "I"); // CV = 1
        campeonato.cadastrarTime("O1", "O1");
        campeonato.cadastrarTime("O2", "O2");
        campeonato.cadastrarTime("O3", "O3");
        campeonato.cadastrarTime("O4", "O4");

        // Construir cenário com MESMOS PG, V, SG e GP, e MESMOS CA,
        // mas CV diferente (J=0, I=1) para forçar o 4º critério (Menor CV)
        // Time J: vitória 2x0 (+3) + empate 0x0 (+1) => PG=4, V=1, SG=+2, GP=2, CA=0, CV=0
        campeonato.registrarResultado("Time J", "O1", 2, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time J", "O2", 0, 0, 0, 0, 0, 0);

        // Time I: vitória 2x0 (+3) + empate 0x0 (+1) => PG=4, V=1, SG=+2, GP=2, CA=0, CV=1
        // Adicionamos 1 cartão vermelho ao I no empate para diferenciar por CV
        campeonato.registrarResultado("Time I", "O3", 2, 0, 0, 0, 0, 0);
        campeonato.registrarResultado("Time I", "O4", 0, 0, 0, 1, 0, 0); // cvM=1 para Time I

        // Ação: ordenar a tabela
        List<Time> ordenada = campeonato.ordenarTabela();

        // Resultado Esperado: Time J (CV=0) acima de Time I (CV=1),
        // mantendo PG, V, SG, GP e CA iguais
        int idxJ = indexOf(ordenada, "Time J");
        int idxI = indexOf(ordenada, "Time I");

        assertTrue(idxJ >= 0, "Time J deve existir na tabela");
        assertTrue(idxI >= 0, "Time I deve existir na tabela");
        assertTrue(idxJ < idxI, "Time J (CV=0) deve aparecer acima do Time I (CV=1)");
    }

    @Test
    @DisplayName("CT17: Edição de Resultado (Vitória para Derrota)")
    void testCT17_EditarResultadoVitoriaParaDerrota() {
        // Pré-condição: cadastrar times A e B
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");

        // Registrar partida inicial: A 1 x 0 B
        // Time A fica com PG=3, V=1, D=0, SG=+1
        campeonato.registrarResultado("A", "B", 1, 0, 0, 0, 0, 0);

        // Ação: editar para A 0 x 1 B (vitória vira derrota)
        campeonato.editarResultado("A", "B", 0, 1, 0, 0, 0, 0);

        // Resultado esperado para o Time A:
        // PG:0, V:0, D:1, SG:-1
        assertEquals(0, campeonato.obterAtributo("A", "PG"));
        assertEquals(0, campeonato.obterAtributo("A", "V"));
        assertEquals(1, campeonato.obterAtributo("A", "D"));
        assertEquals(-1, campeonato.obterAtributo("A", "SG"));
    }

    @Test
    @DisplayName("CT18: Edição de Resultado com Cartões")
    void testCT18_EditarResultadoComCartoes() {
        // Pré-condições: cadastrar times C e D
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");

        // Registrar partida inicial: C 2 x 2 D, com 1 cartão amarelo para C
        // => C fica com PG=1, CA=1
        campeonato.registrarResultado("C", "D", 2, 2, 1, 0, 0, 0);

        // Ação: editar resultado para C 2 x 2 D, agora com 3 cartões amarelos para C
        campeonato.editarResultado("C", "D", 2, 2, 3, 0, 0, 0);

        // Resultado esperado:
        // Time C com CA=3 e PG mantido em 1
        assertEquals(3, campeonato.obterAtributo("C", "CA"));
        assertEquals(1, campeonato.obterAtributo("C", "PG"));
    }


}
