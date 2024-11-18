package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField productQuantityField;
    @FXML
    private ListView<String> productListView;
    

    private int current;
    private int difference;



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

        if(isItAlreadyIn(name))
        {
            showAlert("Error","Product already in database!",Alert.AlertType.ERROR);
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

    private boolean isItAlreadyIn(String name)
    {
        List<Product> products;
        products = jpaDAO.getAllProduct();

        List<String> productnames = products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());


        if(productnames.contains(name))
        {
            return true;
        }
        return false;
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

    public void fillComboBox() {

        List<Product> products;

        products = jpaDAO.getAllProduct();

        List<String> productNames = products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        productComboBox.getItems().addAll(productNames);
        
    }

    int min = 0, max = 1000;
    
    public void transctionInHandle(ActionEvent actionEvent) {
        String selectedProductName = productComboBox.getValue(); // Kiválasztott termék neve
        if (selectedProductName == null) {
            showAlert("Error", "Please select a product!", Alert.AlertType.ERROR);
            return;
        }

        String quantityText = productQuantityField.getText(); // A beírt mennyiség
        int quantity;

        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for quantity!", Alert.AlertType.ERROR);
            return;
        }

        if (quantity <= min || quantity >= max) {
            showAlert("Error", "Quantity must be between " + min+1 + " and " + max + "!", Alert.AlertType.ERROR);
            return;
        }


        Product product = jpaDAO.findProductByName(selectedProductName);
        if (product == null) {
            showAlert("Error", "Product not found in the database!", Alert.AlertType.ERROR);
            return;
        }


        if (product.getQuantity() + quantity > max) {
            showAlert("Error", "The product quantity exceeds the maximum limit!", Alert.AlertType.ERROR);
            return;
        }


        product.setQuantity(product.getQuantity() + quantity);


        try {
            jpaDAO.updateProduct(product);
            productListView.getItems().add("Added " + quantity + " to " + product.getName() + " (New quantity: " + product.getQuantity() + ")");
         } catch (Exception e) {
            showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
        }

    }


    @FXML
    private void transactionOutHandle(ActionEvent event) {
            String selectedProductName = productComboBox.getValue(); // A kiválasztott termék neve
            if (selectedProductName == null) {
                showAlert("Error", "Please select a product!", Alert.AlertType.ERROR);
                return;
            }


            String quantityText = productQuantityField.getText();
            int quantity;


            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number for quantity!", Alert.AlertType.ERROR);
                return;
            }

            if (quantity < min || quantity > max) {
                showAlert("Error", "Quantity must be between " + min + " and " + max + "!", Alert.AlertType.ERROR);
                return;
            }


            Product product = jpaDAO.findProductByName(selectedProductName);
            if (product == null) {
                showAlert("Error", "Product not found in the database!", Alert.AlertType.ERROR);
                return;
            }


            if (product.getQuantity() - quantity < 0) {
                showAlert("Error", "The product quantity cannot be less than 0!", Alert.AlertType.ERROR);
                return;
            }


            product.setQuantity(product.getQuantity() - quantity);


            try {
                jpaDAO.updateProduct(product);
                productListView.getItems().add(("Removed " + quantity + " from " + product.getName() + " (New quantity: " + product.getQuantity() + ")"));

            } catch (Exception e) {
                showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
            }
    }



}
