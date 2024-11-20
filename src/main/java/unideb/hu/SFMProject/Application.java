package unideb.hu.SFMProject;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.h2.tools.Server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Application extends javafx.application.Application{

    private static Scene scene;
    private JPADAO jpadao = new JPADAO();

    public static void main(String[] args) throws SQLException {
        launch();


    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }
    @Override
    public void start(Stage stage) {
        try {
            startDatabase();
            Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScene.fxml"));
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Bezárás és erőforrások felszabadítása
        if (scene != null) {
            jpadao.close();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}