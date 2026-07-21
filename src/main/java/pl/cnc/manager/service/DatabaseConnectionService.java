package pl.cnc.manager.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionService(String url, String user, String password) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Database URL cannot be null or empty");
        }
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
