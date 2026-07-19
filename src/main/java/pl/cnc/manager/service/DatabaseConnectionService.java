package pl.cnc.manager.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public DatabaseConnectionService() {
        this(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
    }

    public DatabaseConnectionService(String url, String user, String password) {
        this.URL = url;
        this.USER = user;
        this.PASSWORD = password;
    }

    public Connection connect() throws SQLException {
        if (URL == null || URL.isBlank()) {
            throw new SQLException("DB_URL environment variable is not set");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
