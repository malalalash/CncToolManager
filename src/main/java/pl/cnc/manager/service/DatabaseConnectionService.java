package pl.cnc.manager.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionService {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionService.class);

    private final HikariDataSource dataSource;

    public DatabaseConnectionService(String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        this.dataSource = new HikariDataSource(config);
        log.info("Connection pool initialized (maxPoolSize={})", config.getMaximumPoolSize());
    }
    public Connection connect() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        dataSource.close();
        log.info("Connection pool closed.");
    }
}
