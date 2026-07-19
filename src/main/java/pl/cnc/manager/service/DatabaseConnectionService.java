package pl.cnc.manager.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public DatabaseConnectionService(String url, String user, String password) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Database URL cannot be null or empty");
        }
        this.URL = url;
        this.USER = user;
        this.PASSWORD = password;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
