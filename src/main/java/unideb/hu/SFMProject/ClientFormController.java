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

public class ClientFormController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button AccountButton;

    @FXML
    private AnchorPane AccountForm;

    @FXML
    private Button TransInButton;

    @FXML
    private Button TransOutButton;

    @FXML
    private AnchorPane TransactionInForm;

    @FXML
    private AnchorPane TransactionOutForm;

    @FXML
    private Button ViewProdButton;

    @FXML
    private AnchorPane ViewProductStocForm;

    @FXML
    private Button LogoutButton;

    @FXML
    void switchForm(ActionEvent event) {
        if (event.getSource() == TransInButton) {
            AccountForm.setVisible(false);
            TransactionInForm.setVisible(true);
            TransactionOutForm.setVisible(false);
            ViewProductStocForm.setVisible(false);
        }
        else if (event.getSource() == AccountButton) {
            AccountForm.setVisible(true);
            TransactionInForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            ViewProductStocForm.setVisible(false);
        }
        else if (event.getSource() == TransOutButton) {
            AccountForm.setVisible(false);
            TransactionInForm.setVisible(false);
            TransactionOutForm.setVisible(true);
            ViewProductStocForm.setVisible(false);
        }
        else if (event.getSource() == ViewProdButton) {
            AccountForm.setVisible(false);
            TransactionInForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            ViewProductStocForm.setVisible(true);
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
