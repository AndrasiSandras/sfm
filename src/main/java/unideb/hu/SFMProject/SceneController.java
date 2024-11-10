package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // General method to load a scene
    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLLoginScene.fxml");
    }

    @FXML
    void goBackToLogin(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLClientLoginScene.fxml");
    }

    @FXML
    void staffLogin(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLStaffLoginScene.fxml");
    }

    @FXML
    void clientLogin(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLClientLoginScene.fxml");
    }

    @FXML
    void logInStaff(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLStaffScene.fxml");
    }

    @FXML
    void logInClient(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLClientScene.fxml");
    }

    @FXML
    void registerNewClient(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLClientRegisterScene.fxml");
    }
}
