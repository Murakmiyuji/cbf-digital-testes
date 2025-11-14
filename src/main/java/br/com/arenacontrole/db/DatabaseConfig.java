package br.com.arenacontrole.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {
    public static DataSource createDataSource() {
        HikariConfig cfg = new HikariConfig();
        String url = System.getenv().getOrDefault("JDBC_URL", "jdbc:postgresql://localhost:5432/cbf_testes");
        String user = System.getenv().getOrDefault("DB_USER", "postgres");
        String pass = System.getenv().getOrDefault("DB_PASS", "postgres");

        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setMaximumPoolSize(5);
        cfg.setPoolName("cbf-hikari-pool");
        return new HikariDataSource(cfg);
    }
}