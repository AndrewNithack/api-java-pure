package org.trabalhos.infraestructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConfig {
    private static final String ORACLE_DB_USER = "ADMIN";
    private static final String ORACLE_DB_PASSWORD = "p@CotucaProjeto2023";
    private static final String ORACLE_CONN_STR = "(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.sa-vinhedo-1.oraclecloud.com))(connect_data=(service_name=g3c8dc0f6a7cb65_aeropecas_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))";

    private static final String URL = "jdbc:oracle:thin:@" + ORACLE_CONN_STR;
    private static final String USERNAME = ORACLE_DB_USER;
    private static final String PASSWORD = ORACLE_DB_PASSWORD;

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar o driver Oracle JDBC", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
