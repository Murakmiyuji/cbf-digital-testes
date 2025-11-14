// language: java
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
 * Testes de integração (TDD) para persistência conforme Plano de Testes (CT41-CT44).
 * Observação: exige DatabaseConfig.createDataSource() e CampeonatoRepository com findTimeByNome(...).
 */
@DisplayName("Testes Integração - Samuel)")
public class TestesIntegracaoSamuel {

    private DataSource ds;
    private CampeonatoRepository repo;
    private Campeonato campeonato;

    @BeforeEach
    void setUp() throws Exception {
        // cria DataSource via configuração central (espera-se que DatabaseConfig exista)
        ds = DatabaseConfig.createDataSource();
        repo = new CampeonatoRepository(ds);

        // limpa tabelas para ambiente limpo
        limparTabelas(ds);

        // instancia campeonato com repositório (espera-se que Campeonato persista via repository)
        campeonato = new Campeonato(repo);
    }

    // helper: tenta limpar tabelas comuns (silencia erros de tabela inexistente)
    private void limparTabelas(DataSource ds) {
        String[] tables = new String[] { "team", "partida", "match", "match_history", "historico_partidas" };
        for (String t : tables) {
            try (Connection c = ds.getConnection();
                 Statement s = c.createStatement()) {
                s.executeUpdate("DELETE FROM " + t);
            } catch (SQLException ignored) {
                // tabela pode não existir ainda; ignorar
            }
        }
    }

    // helper: tenta localizar um registro de partida em tabelas/colunas prováveis
    private boolean partidaPersistida(DataSource ds, String mand, String visit, int golsMand, int golsVisit) {
        // tentativas de consulta em várias possíveis estruturas de tabela
        String[][] attempts = new String[][] {
            // table, mandanteCol, visitanteCol, golsMandCol, golsVisitCol
            { "partida", "mandante", "visitante", "gm", "gv" },
            { "partida", "mandante", "visitante", "gols_mandante", "gols_visitante" },
            { "match", "mandante", "visitante", "gols_mandante", "gols_visitante" },
            { "match_history", "mandante", "visitante", "gols_mandante", "gols_visitante" },
            { "historico_partidas", "mandante", "visitante", "gols_mandante", "gols_visitante" }
        };

        for (String[] a : attempts) {
            String table = a[0];
            String sql = String.format("SELECT 1 FROM %s WHERE LOWER(%s)=? AND LOWER(%s)=? AND %s=? AND %s=? LIMIT 1",
                    table, a[1], a[2], a[3], a[4]);
            try (Connection c = ds.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, mand.toLowerCase());
                ps.setString(2, visit.toLowerCase());
                ps.setInt(3, golsMand);
                ps.setInt(4, golsVisit);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return true;
                }
            } catch (SQLException ignored) {
                // se tabela/coluna não existir, tenta próxima possibilidade
            }
        }
        return false;
    }

    @Test
    @DisplayName("CT41: Cadastro de Times com Persistência")
    void testCT41_CadastroDeTimesComPersistencia() {
        // passo 1: cadastrar
        boolean ok = campeonato.cadastrarTime("Time Z", "Z");
        assertTrue(ok, "cadastrarTime deve retornar true para cadastro bem-sucedido");

        // passo 2: buscar no repositório/BD
        br.com.arenacontrole.Time t = repo.findTimeByNome("Time Z");
        assertNotNull(t, "Time Z deve ser retornado pelo repositório (persistência)");
        assertEquals("Time Z", t.getNome());
        assertEquals("Z", t.getAbreviacao());
    }

    @Test
    @DisplayName("CT42: Persistência dos Atributos Iniciais")
    void testCT42_PersistenciaAtributosIniciais() {
        campeonato.cadastrarTime("Time Alpha", "A");

        br.com.arenacontrole.Time t = repo.findTimeByNome("Time Alpha");
        assertNotNull(t, "Time Alpha deve existir no BD");

        // verificar atributos iniciais zerados
        assertEquals(0, t.getPontos(), "Pontos iniciais devem ser 0");
        assertEquals(0, t.getVitorias(), "Vitórias iniciais devem ser 0");
        assertEquals(0, t.getEmpates(), "Empates iniciais devem ser 0");
        assertEquals(0, t.getDerrotas(), "Derrotas iniciais devem ser 0");
        assertEquals(0, t.getGolsPro(), "Gols pró iniciais devem ser 0");
        assertEquals(0, t.getGolsContra(), "Gols contra iniciais devem ser 0");
        assertEquals(0, t.getCartoesAmarelos(), "CA iniciais devem ser 0");
        assertEquals(0, t.getCartoesVermelhos(), "CV iniciais devem ser 0");
    }

    @Test
    @DisplayName("CT43: Registro e Persistência da Partida")
    void testCT43_RegistroEPersistenciaDaPartida() {
        campeonato.cadastrarTime("A", "A");
        campeonato.cadastrarTime("B", "B");

        // registrar partida
        campeonato.registrarResultado("A", "B", 3, 2, 1, 0, 0, 0);

        // verificar que atributos dos times foram atualizados e persistidos
        br.com.arenacontrole.Time ta = repo.findTimeByNome("A");
        br.com.arenacontrole.Time tb = repo.findTimeByNome("B");

        assertNotNull(ta, "Time A deve existir no BD");
        assertNotNull(tb, "Time B deve existir no BD");

        assertEquals(3, ta.getGolsPro(), "A deve ter 3 gols pró após partida");
        assertEquals(2, tb.getGolsPro(), "B deve ter 2 gols pró após partida");
        assertEquals(1, ta.getCartoesAmarelos(), "A deve ter 1 cartão amarelo registrado");

        // tentativa de verificar histórico de partidas persistido no BD (varias estruturas possíveis)
        boolean encontrado = partidaPersistida(ds, "A", "B", 3, 2);
        assertTrue(encontrado, "Deve existir um registro de partida persistido no BD (tabela 'partida' ou similar). Se este assertion falhar, implemente a persistência de partidas no repositório.");
    }

    @Test
    @DisplayName("CT44: Fluxo: Registro - Cálculo - BD (Vitória +3)")
    void testCT44_FluxoRegistroCalculoBD() {
        campeonato.cadastrarTime("C", "C");
        campeonato.cadastrarTime("D", "D");

        campeonato.registrarResultado("C", "D", 4, 0, 0, 0, 0, 0);

        br.com.arenacontrole.Time tc = repo.findTimeByNome("C");
        assertNotNull(tc, "Time C deve existir no BD");
        assertEquals(3, tc.getPontos(), "Vitória deve gerar 3 pontos persistidos no BD para Time C");
    }
}