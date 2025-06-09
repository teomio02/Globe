package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.engineering.Persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;

public class Main {

    private static final String VIEW_TYPE = "VIEW_TYPE";
    private static final String DAO_TYPE = "DAO_TYPE";


    public static void main(String[] args) throws IOException {

        String persistence;
        String view;

        try (InputStream input = new FileInputStream("src/main/resources/start.properties")){
            Properties properties = new Properties();
            properties.load(input);

            persistence = properties.getProperty("dao.type");
            view = properties.getProperty("view.type");
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid dao type");
        }

        Persistence.getInstance().setDefaultType(persistence);
        Persistence.getInstance().setType(persistence);

        if (view.equals("GUI")) {
            GlobeApplicationGUI.run(args);
        } else if (view.equals("CLI")) {
            GlobeApplicationCLI.run(args);
        } else {
            throw new IllegalArgumentException("Invalid view type");
        }
    }
}