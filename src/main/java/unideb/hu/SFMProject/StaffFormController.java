package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class StaffFormController {

    public Button GenerateReportButton;
    public ImageView ProfilePicture;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField productNameField; // Termék neve
    @FXML
    private TextField productPriceField; // Termék ára
    @FXML
    private TextArea productDescriptionField; // Termék leírása
    @FXML
    private ImageView ProductImageView; // Termékkép
    @FXML
    private Button BrowseButton;
    @FXML
    private Button AddButton; // Mentés gomb
    @FXML
    private Button AccountButton, AddProductButton, ReportsButton, TransInOutButton, ViewProdButton, LogoutButton;
    @FXML
    private AnchorPane AccountForm, AddProductForm, ReportsForm, TransactionInOutForm, ViewProductStockForm;

    private JPADAO jpaDAO = new JPADAO();
    private Map<Button, AnchorPane> buttonPaneMap;

    private File selectedFile;
    private byte[] imageBytes;

    @FXML
    public void initialize() {
        buttonPaneMap = new HashMap<>();
        buttonPaneMap.put(AccountButton, AccountForm);
        buttonPaneMap.put(AddProductButton, AddProductForm);
        buttonPaneMap.put(ReportsButton, ReportsForm);
        buttonPaneMap.put(TransInOutButton, TransactionInOutForm);
        buttonPaneMap.put(ViewProdButton, ViewProductStockForm);
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
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Kép betöltése az ImageView-ba
                Image image = new Image(new FileInputStream(selectedFile));
                ProductImageView.setImage(image);

                // Kép átalakítása byte[] formátumba az adatbázishoz
                imageBytes = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the image!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSaveProduct(ActionEvent event) {
        // 1. Adatok begyűjtése
        String name = productNameField.getText();
        String priceText = productPriceField.getText();
        String description = productDescriptionField.getText();

        // 2. Adatok validálása
        if (name.isEmpty() || priceText.isEmpty() || description.isEmpty()) {
            showAlert("Error", "All fields must be filled in!", Alert.AlertType.ERROR);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText); // Ár konvertálása számra
        } catch (NumberFormatException e) {
            showAlert("Error", "The price must be a valid number!", Alert.AlertType.ERROR);
            return;
        }

        // 3. Új termék példány létrehozása
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setDescription(description);
        //newProduct.setQuantity(0); // Alapértelmezett darabszám


        // Kép hozzáadása
        if (imageBytes != null) {
            newProduct.setImage(imageBytes); // A kép byte tömbként tárolva
        }

        // 4. Adatok mentése az adatbázisba
        try {
            jpaDAO.saveProduct(newProduct);
            showAlert("Success", "The product has been successfully saved!", Alert.AlertType.INFORMATION);
            clearForm(); // Mezők kiürítése
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save the product!", Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        productNameField.clear();
        productPriceField.clear();
        productDescriptionField.clear();
        // Alapértelmezett képre állítás, ha szükséges
        //productImageView.setImage(null);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void reportGenerator(ActionEvent actionEvent) {
    }
}
