package org.trabalhos.infraestructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConfig {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:ORCLCDB";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "Oracle_123";

    static {
        try {
            // This explicitly loads the JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}