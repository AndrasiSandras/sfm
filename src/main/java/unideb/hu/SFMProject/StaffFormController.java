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

public class StaffFormController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button AccountButton, AddProductButton, ReportsButton, TransInButton, TransOutButton, ViewProdButton, LogoutButton;

    @FXML
    private AnchorPane AccountForm, AddProductForm, ReportsForm, TransactionInForm, TransactionOutForm, ViewProductStockForm;

    // Map to store button-pane relationships
    private Map<Button, AnchorPane> buttonPaneMap;

    @FXML
    public void initialize() {
        // Initialize the map with button-pane pairs
        buttonPaneMap = new HashMap<>();
        buttonPaneMap.put(AccountButton, AccountForm);
        buttonPaneMap.put(AddProductButton, AddProductForm);
        buttonPaneMap.put(ReportsButton, ReportsForm);
        buttonPaneMap.put(TransInButton, TransactionInForm);
        buttonPaneMap.put(TransOutButton, TransactionOutForm);
        buttonPaneMap.put(ViewProdButton, ViewProductStockForm);
    }

    @FXML
    void switchForm(ActionEvent event) {
        // Hide all forms initially
        buttonPaneMap.values().forEach(pane -> pane.setVisible(false));

        // Get the source button and make the corresponding pane visible
        Button sourceButton = (Button) event.getSource();
        AnchorPane targetPane = buttonPaneMap.get(sourceButton);
        if (targetPane != null) {
            targetPane.setVisible(true);
        }
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
