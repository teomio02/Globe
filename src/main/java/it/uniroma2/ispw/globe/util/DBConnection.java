package it.uniroma2.ispw.globe.util;

import it.uniroma2.ispw.globe.exception.DBConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance = null;
    private Connection conn = null;

    private String username="root";
    private String password="";

    private DBConnection(){}

    public Connection getConnection() throws DBConnectionException {
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")){
            if (input == null) {
                throw new DBConnectionException("Database connection configuration not found");
            } else {
                Properties properties = new Properties();
                properties.load(input);

                String dbUrl = properties.getProperty("db.url");
                String dbUsr = properties.getProperty("db.user");
                String dbPwd = properties.getProperty("db.password");

                conn = DriverManager.getConnection(dbUrl, dbUsr, dbPwd);
            }
        } catch (IOException | SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
        return conn;
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null)
            instance = new DBConnection();
        return instance;
    }

    public void closeConnection(Statement st, ResultSet rs){
        try {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            if(conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void closeEverything(Statement st, ResultSet rs, boolean wantToCloseConn) {
//        try {
//            if (st != null) st.close();
//            if (rs != null) rs.close();
//            if(wantToCloseConn)
//                DBConnection.getInstance().closeConnection();
//        } catch (SQLException e) {
//            LoggerManager.logSevereException(ERROR_CLOSING_DB, e);
//        }
//    }
}
