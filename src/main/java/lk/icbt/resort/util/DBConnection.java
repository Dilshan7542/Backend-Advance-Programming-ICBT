package lk.icbt.resort.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple JDBC connection provider.
 * For CIS6003 this is enough; in real apps use a pooled DataSource.
 */
public final class DBConnection {

    private DBConnection() {}

    static {
        try {
            String driver = AppConfig.getOrDefault("db.driver", "com.mysql.cj.jdbc.Driver");
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = AppConfig.get("db.url");
        String user = AppConfig.get("db.username");
        String pass = AppConfig.get("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
}
