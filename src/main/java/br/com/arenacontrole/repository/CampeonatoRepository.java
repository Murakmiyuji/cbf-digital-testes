package br.com.arenacontrole.repository;

import br.com.arenacontrole.Time;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Repositório JDBC mínimo para persistir Time.
 */
public class CampeonatoRepository {

    private final DataSource ds;

    public CampeonatoRepository(DataSource ds) {
        this.ds = ds;
        initSchema();
    }

    private void initSchema() {
        String sqlTeam = "CREATE TABLE IF NOT EXISTS team (" +
                "id SERIAL PRIMARY KEY," +
                "nome TEXT UNIQUE NOT NULL," +
                "abreviacao TEXT UNIQUE NOT NULL," +
                "pontos INT NOT NULL," +
                "jogos INT NOT NULL," +
                "vitorias INT NOT NULL," +
                "empates INT NOT NULL," +
                "derrotas INT NOT NULL," +
                "gols_pro INT NOT NULL," +
                "gols_contra INT NOT NULL," +
                "cartoes_amarelos INT NOT NULL," +
                "cartoes_vermelhos INT NOT NULL" +
                ")";

        String sqlPartida = "CREATE TABLE IF NOT EXISTS partida (" +
                "id SERIAL PRIMARY KEY," +
                "mandante TEXT NOT NULL," +
                "visitante TEXT NOT NULL," +
                "gols_mandante INT NOT NULL," +
                "gols_visitante INT NOT NULL," +
                "ca_mandante INT NOT NULL," +
                "cv_mandante INT NOT NULL," +
                "ca_visitante INT NOT NULL," +
                "cv_visitante INT NOT NULL," +
                "data TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection c = ds.getConnection(); Statement s = c.createStatement()) {
            s.execute(sqlTeam);
            s.execute(sqlPartida);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar schema", e);
        }
    }

    public void saveOrUpdateTime(Time t) {
        String upsert = "INSERT INTO team (nome, abreviacao, pontos, jogos, vitorias, empates, derrotas, gols_pro, gols_contra, cartoes_amarelos, cartoes_vermelhos) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (nome) DO UPDATE SET " +
                "abreviacao = EXCLUDED.abreviacao, pontos = EXCLUDED.pontos, jogos = EXCLUDED.jogos, vitorias = EXCLUDED.vitorias, empates = EXCLUDED.empates, derrotas = EXCLUDED.derrotas, gols_pro = EXCLUDED.gols_pro, gols_contra = EXCLUDED.gols_contra, cartoes_amarelos = EXCLUDED.cartoes_amarelos, cartoes_vermelhos = EXCLUDED.cartoes_vermelhos";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(upsert)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getAbreviacao());
            ps.setInt(3, t.getPontos());
            ps.setInt(4, t.getJogos());
            ps.setInt(5, t.getVitorias());
            ps.setInt(6, t.getEmpates());
            ps.setInt(7, t.getDerrotas());
            ps.setInt(8, t.getGolsPro());
            ps.setInt(9, t.getGolsContra());
            ps.setInt(10, t.getCartoesAmarelos());
            ps.setInt(11, t.getCartoesVermelhos());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Time findTimeByNome(String nome) {
        String sql = "SELECT nome, abreviacao, pontos, jogos, vitorias, empates, derrotas, gols_pro, gols_contra, cartoes_amarelos, cartoes_vermelhos FROM team WHERE LOWER(nome) = LOWER(?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Time t = new Time(rs.getString("nome"), rs.getString("abreviacao"));
                    t.setPontos(rs.getInt("pontos"));
                    t.setJogos(rs.getInt("jogos"));
                    t.setVitorias(rs.getInt("vitorias"));
                    t.setEmpates(rs.getInt("empates"));
                    t.setDerrotas(rs.getInt("derrotas"));
                    t.setGolsPro(rs.getInt("gols_pro"));
                    t.setGolsContra(rs.getInt("gols_contra"));
                    t.setCartoesAmarelos(rs.getInt("cartoes_amarelos"));
                    t.setCartoesVermelhos(rs.getInt("cartoes_vermelhos"));
                    return t;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void savePartida(String mandante, String visitante, int golsMand, int golsVisit, int caMand, int cvMand, int caVisit, int cvVisit) {
        String sql = "INSERT INTO partida (mandante, visitante, gols_mandante, gols_visitante, ca_mandante, cv_mandante, ca_visitante, cv_visitante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, mandante);
            ps.setString(2, visitante);
            ps.setInt(3, golsMand);
            ps.setInt(4, golsVisit);
            ps.setInt(5, caMand);
            ps.setInt(6, cvMand);
            ps.setInt(7, caVisit);
            ps.setInt(8, cvVisit);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}