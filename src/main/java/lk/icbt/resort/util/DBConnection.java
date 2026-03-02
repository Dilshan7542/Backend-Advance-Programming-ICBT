package lk.icbt.resort.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static DBConnection dbConnection;
    private Connection connection;
    private DBConnection()  {
        try {
            String url = AppConfig.get("db.url");
            String user = AppConfig.get("db.username");
            String pwd = AppConfig.get("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(url,user,pwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static DBConnection getInstance()  {
        return dbConnection==null ? dbConnection=new DBConnection() :dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
