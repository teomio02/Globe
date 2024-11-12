package it.uniroma2.ispw.globe.controller.applicationcontroller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private String username="root";
    private String password="";

    public Connection getConnection() throws SQLException {
        String myUrl="jdbc:mysql://localhost:3306/globeDB?useSSL=false&allowPublicKeyRetrieval=true";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            throw new SQLException();
        }

        return DriverManager.getConnection(myUrl,this.username,this.password);
    }
}
