package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.guicontroller.ManageItineraryGUIController;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.UserBean;
import it.uniroma2.ispw.globe.model.dao.UserDao;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryUserDao;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.ModelFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import okhttp3.EventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GlobeApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL());
        InputStream url = getClass().getResourceAsStream("file:src/main/resources/it/uniroma2/ispw/globe/logo.png");

        if (url != null) {
            Image icon = new Image(url);
            stage.getIcons().add(icon);
        }

        // meccanismo provvisorio prima dell'implementazione del login (aggiunta di una nuova sessione)
        UserBean userBean = new UserBean("TeoMio","000000");
        User user = new ModelFactory().createUser(userBean);
        InMemoryUserDao.getInstance().addUser(user);
        String sessionId = SessionManager.getInstance().addSession(user);
        ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
        fxmlLoader.setController(controller);
        //*************************************

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("GLOBE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}