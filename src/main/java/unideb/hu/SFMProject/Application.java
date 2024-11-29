package unideb.hu.SFMProject;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Server;

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
            stage.setTitle("Palackmester App");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (scene != null) {
            jpadao.close();
        }
    }
}