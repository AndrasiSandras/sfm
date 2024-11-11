package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientFormController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button AccountButton, TransInOutButton, ViewProdButton, LogoutButton;

    @FXML
    private AnchorPane AccountForm, TransactionInOutForm, ViewProductStocForm;

    private Map<Button, AnchorPane> buttonPaneMap;

    @FXML
    public void initialize() {
        buttonPaneMap = new HashMap<>();
        buttonPaneMap.put(AccountButton, AccountForm);
        buttonPaneMap.put(TransInOutButton, TransactionInOutForm);
        buttonPaneMap.put(ViewProdButton, ViewProductStocForm);
    }

    @FXML
    void switchForm(ActionEvent event) {
        buttonPaneMap.values().forEach(pane -> pane.setVisible(false));

        Button sourceButton = (Button) event.getSource();
        AnchorPane targetPane = buttonPaneMap.get(sourceButton);
        if (targetPane != null) {
            targetPane.setVisible(true);
        }
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}