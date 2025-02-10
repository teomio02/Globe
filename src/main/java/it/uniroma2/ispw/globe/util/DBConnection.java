package it.uniroma2.ispw.globe.util;

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

    public Connection getConnection() throws SQLException {
        String myUrl="jdbc:mysql://localhost:3306/globeDB?useSSL=false&allowPublicKeyRetrieval=true";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            throw new SQLException();
        }

        return DriverManager.getConnection(myUrl,this.username,this.password);
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
