package unideb.hu.SFMProject;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Server;

import static javafx.application.Application.launch;

public class Application extends javafx.application.Application{

    private static Scene scene;
    private JPADAO jpadao = new JPADAO();


    public static void main(String[] args) throws SQLException {


        launch();

        Utils bUtils = new Utils(new JPADAO());
        bUtils.runBUtils();
        bUtils.runPUtils();


    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        startDatabase();
        scene = new Scene(loadFXML("FXMLLoginScene"));
        stage.setScene(scene);
        stage.show();
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
