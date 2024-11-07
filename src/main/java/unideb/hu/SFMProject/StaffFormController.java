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

public class StaffFormController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button AccountButton;

    @FXML
    private AnchorPane AccountForm;

    @FXML
    private Button AddCostumerButton;

    @FXML
    private AnchorPane AddCostumerForm;

    @FXML
    private Button AddProductButton;

    @FXML
    private AnchorPane AddProductForm;

    @FXML
    private Button AddSupplierButton;

    @FXML
    private AnchorPane AddSupplierForm;

    @FXML
    private Button ReportsButton;

    @FXML
    private AnchorPane ReportsForm;

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
    private AnchorPane ViewProductStockForm;

    @FXML
    private Button LogoutButton;

    @FXML
    void switchForm(ActionEvent event) {
        if (event.getSource() == TransInButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(false);
            TransactionInForm.setVisible(false);
            TransactionOutForm.setVisible(true);
            ViewProductStockForm.setVisible(false);
        }
        else if (event.getSource() == AccountButton) {
            AccountForm.setVisible(true);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(false);
            TransactionInForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            ViewProductStockForm.setVisible(false);
        }
        else if (event.getSource() == TransOutButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            TransactionInForm.setVisible(true);
            ViewProductStockForm.setVisible(false);
        }
        else if (event.getSource() == ViewProdButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            TransactionInForm.setVisible(false);
            ViewProductStockForm.setVisible(true);
        }
        else if (event.getSource() == AddCostumerButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(true);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            TransactionInForm.setVisible(false);
            ViewProductStockForm.setVisible(false);
        }
        else if (event.getSource() == AddProductButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(true);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            TransactionInForm.setVisible(false);
            ViewProductStockForm.setVisible(false);
        }
        else if (event.getSource() == AddSupplierButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(true);
            ReportsForm.setVisible(false);
            TransactionOutForm.setVisible(false);
            TransactionInForm.setVisible(false);
            ViewProductStockForm.setVisible(false);
        }
        else if (event.getSource() == ReportsButton) {
            AccountForm.setVisible(false);
            AddCostumerForm.setVisible(false);
            AddProductForm.setVisible(false);
            AddSupplierForm.setVisible(false);
            ReportsForm.setVisible(true);
            TransactionOutForm.setVisible(false);
            TransactionInForm.setVisible(false);
            ViewProductStockForm.setVisible(false);
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
