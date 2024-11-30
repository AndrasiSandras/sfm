package unideb.hu.SFMProject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Server;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main application class for launching the Palackmester App.
 */
public class Application extends javafx.application.Application {

    private static final String APP_TITLE = "Palackmester App";
    private static final String LOGIN_SCENE_FXML = "/view/FXMLLoginScene.fxml";

    private static Scene appScene;
    private final JPADAO databaseAccess = new JPADAO();

    public static void main(String[] args) {
        launch();
    }

    /**
     * Initializes and starts the H2 database server.
     *
     * @throws SQLException if the database fails to start.
     */
    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }

    /**
     * JavaFX entry point. Sets up the primary stage and displays the login scene.
     *
     * @param stage the primary stage for the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            startDatabase();
            Parent root = loadFXML(LOGIN_SCENE_FXML);
            setupPrimaryStage(stage, root);
        } catch (IOException | SQLException e) {
            handleStartupError(e);
        }
    }

    /**
     * Stops the application and releases resources.
     */
    @Override
    public void stop() {
        if (appScene != null) {
            databaseAccess.close();
        }
    }

    /**
     * Loads an FXML file and returns its root node.
     *
     * @param fxmlPath the path to the FXML file.
     * @return the root node of the loaded FXML file.
     * @throws IOException if the FXML file cannot be loaded.
     */
    private Parent loadFXML(String fxmlPath) throws IOException {
        return FXMLLoader.load(getClass().getResource(fxmlPath));
    }

    /**
     * Configures and displays the primary stage.
     *
     * @param stage the primary stage.
     * @param root  the root node of the initial scene.
     */
    private void setupPrimaryStage(Stage stage, Parent root) {
        appScene = new Scene(root);
        stage.setTitle(APP_TITLE);
        stage.setScene(appScene);
        stage.show();
    }

    /**
     * Handles errors that occur during application startup.
     *
     * @param e the exception to handle.
     */
    private void handleStartupError(Exception e) {
        System.err.println("Application startup failed: " + e.getMessage());
        e.printStackTrace(); // Can be replaced with a logging framework.
    }
}