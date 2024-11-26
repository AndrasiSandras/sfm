package unideb.hu.SFMProject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static unideb.hu.SFMProject.PDFGenerator.generateReport;

public class StaffFormController {

    public Button GenerateReportButton;
    public ImageView ProfilePicture;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stage popupStage = null;


    @FXML
    private TextField productNameField; // Termék neve
    @FXML
    private TextField productPriceField; // Termék ára
    @FXML
    private TextField productDescriptionField; // Termék leírása
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
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product,String> tableName;
    @FXML
    private TableColumn<Product,Double> tablePrice;
    @FXML
    private TableColumn<Product,Integer> tableQuantity;
    @FXML
    private TableColumn<Product,String> tableDescription;
    @FXML
    private TableColumn<Product, ImageView> tableImage;
    @FXML
    private TableView<Report> reportTableView;
    @FXML
    private TableColumn<Report,Integer> transactionId;
    @FXML
    private TableColumn<Report,String> inOut;
    @FXML
    private TableColumn<Report,String> pName;
    @FXML

    private TableColumn<Report,String> pProduct;

    @FXML
    private Label sUserLabel;

    private String loggedInUser;
    private String Cred;

    private JPADAO jpaDAO = new JPADAO();
    private Map<Button, AnchorPane> buttonPaneMap;

    private File selectedFile;
    private byte[] imageBytes;
    private byte[] ProfileimageBytes;

    public byte[] getProfileimageBytes()
    {
        return ProfileimageBytes;
    }


    ClientFormController clientFormController;

    public ClientFormController getClientFormController() {
        return clientFormController;
    }

    SceneController sceneController;

    public SceneController getSceneController() {
        return sceneController;
    }

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
        //newProduct.setQuantity(0);


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
        ProductImageView.setImage(null);
        imageBytes = null;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void reportGenerator(ActionEvent actionEvent) {
        generateReport();
    }

    public void fillComboBox() {

        List<Product> products;

        productComboBox.getItems().clear();

        products = jpaDAO.getAllProduct();

        List<String> productNames = products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        productComboBox.getItems().addAll(productNames);

        productComboBox.hide();
        productComboBox.show();
        
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
            Report report = new Report();
            report.setTransactionId(generateUniqueRandom());
            report.setInOut("IN");
            report.setpName(loggedInUser + " (Staff)");
            report.setProduct(product.getName()+": "+ quantity + ",(New quantity: " + product.getQuantity() + ")");
            report.setDate(report.setDate(LocalDateTime.now()));
            jpaDAO.saveReport(report);

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
                //setTransOut(generateUniqueRandom()+ ",OUT," + product.getName() + "," + product.getQuantity() + "db\n");

                Report report = new Report();
                report.setTransactionId(generateUniqueRandom());
                report.setInOut("OUT");
                report.setpName(loggedInUser + " (Staff)");
                report.setProduct(product.getName()+": "+quantity + ",(New quantity: " + product.getQuantity() + ")");
                report.setDate(LocalDateTime.now());
                jpaDAO.saveReport(report);

           } catch (Exception e) {
                showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
            }
    }

    @FXML
    private void handleChangePasswordStaff() {
        // Ellenőrizzük, hogy az ablak már nyitva van-e
        if (popupStage != null) {
            popupStage.toFront();
            return;
        }

        // Felugró ablak létrehozása
        popupStage = new Stage();
        popupStage.setTitle("Change Password");

        // Logó betöltése
        Image logoImage = new Image(getClass().getResourceAsStream("/image/palacklogo.png"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(100);  // Méret beállítása
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        // Stílusos TextField-ek
        TextField currentPasswordField = new TextField();
        currentPasswordField.setPromptText("Current Password");
        currentPasswordField.setStyle(
                "-fx-background-color: #3A4750;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #EA9215;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-padding: 5px;" +
                        "-fx-font-size: 14px;"
        );

        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("New Password");
        newPasswordField.setStyle(
                "-fx-background-color: #3A4750;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #EA9215;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-padding: 5px;" +
                        "-fx-font-size: 14px;"
        );

        // Stílusos Submit gomb
        Button submitButton = new Button("Submit");
        submitButton.setStyle(
                "-fx-background-color: #EA9215;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-padding: 10px 20px;"
        );

        submitButton.setOnMouseEntered(event -> submitButton.setStyle(
                "-fx-background-color: #FFD479;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0, 0, 2);"
        ));

        submitButton.setOnMouseExited(event -> submitButton.setStyle(
                "-fx-background-color: #EA9215;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-padding: 10px 20px;"
        ));

        submitButton.setOnAction(event -> {
            // Gomb funkció
            System.out.println("Password changed!");
            popupStage.close();
        });

        // Ablak elrendezése
        VBox layout = new VBox(15, logoImageView, currentPasswordField, newPasswordField, submitButton);
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #3A4750, #282C30);" +
                        "-fx-border-color: #EA9215;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-padding: 20px;"
        );
        layout.setAlignment(Pos.CENTER);

        Scene popupScene = new Scene(layout, 350, 400);
        popupStage.setScene(popupScene);

        // Bezárás esemény
        popupStage.setOnCloseRequest(event -> popupStage = null);

        popupStage.show();
    }

    public void generateTableView()
    {
        ObservableList<Product> products;

        products = FXCollections.observableList(jpaDAO.getAllProduct());

        tableName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tablePrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        tableQuantity.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        tableDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        tableImage.setCellValueFactory(new Callback<CellDataFeatures<Product, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(CellDataFeatures<Product, ImageView> param) {
                byte[] imageBytes = param.getValue().getImage();
                Image image = null;

                if (imageBytes != null) {
                    image = new Image(new ByteArrayInputStream(imageBytes)); // Byte[] -> Image
                }

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                return new SimpleObjectProperty<>(imageView);
            }
        });



        productTableView.setItems(products);
    }


    public void refreshTableView(ActionEvent actionEvent) {
        generateTableView();
    }

    public void deleteProductHandle(ActionEvent actionEvent) {

        // Kiválasztott név lekérése
        String selectedProductName = productComboBox.getValue();

        if (selectedProductName == null || selectedProductName.isEmpty()) {
            showAlert("Error", "Please select a product to delete!", Alert.AlertType.ERROR);
            return;
        }

        // Keresd meg a Product objektumot a név alapján
        Product productToDelete = jpaDAO.findProductByName(selectedProductName);

        if (productToDelete == null) {
            showAlert("Error", "The selected product was not found in the database.", Alert.AlertType.ERROR);
            return;
        }

        // Törlés a jpaDAO.deleteProduct() segítségével
        try {
            jpaDAO.deleteProduct(productToDelete);
            showAlert("Success", "The product has been successfully deleted!", Alert.AlertType.INFORMATION);

            // Frissítsük a ComboBox tartalmát
            productComboBox.getItems().remove(selectedProductName);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete the product.", Alert.AlertType.ERROR);
        }

    }


        private static final int MIN = 100000;
        private static final int MAX = 999999;
        private Set<Integer> generatedNumbers = new HashSet<>();
        private Random random = new Random();

        public int generateUniqueRandom() {
            if (generatedNumbers.size() >= (MAX - MIN + 1)) {
                throw new IllegalStateException("Minden lehetséges 6 jegyű számot generáltál!");
            }

            int number;
            do {
                number = random.nextInt(MAX - MIN + 1) + MIN;
            } while (generatedNumbers.contains(number));

            generatedNumbers.add(number);
            return number;
        }


    public void setLoggedInUser(String loggedInUser, String Creds, Image pImage) {
        this.loggedInUser = loggedInUser;
        this.Cred = Creds;
        ProfilePicture.setImage(pImage);
        // Frissítjük a Label-t
        sUserLabel.setText("Logged in as: "+ loggedInUser);
    }

    public void refreshReportHandle(ActionEvent actionEvent) {
        generateReportTableivew();
    }


    public void generateReportTableivew()
    {
        ObservableList<Report> reports;

        reports = FXCollections.observableList(jpaDAO.getAllReports());

        transactionId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTransactionId()).asObject());
        inOut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInOut()));
        pName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getpName()));
        pProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct()));


        reportTableView.setItems(reports);

    }

    public void handleCHangePassword(ActionEvent actionEvent) {
    }

    public void handleChangeProfilePicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Kép betöltése az ImageView-ba
                Image image = new Image(new FileInputStream(selectedFile));
                ProfilePicture.setImage(image);

                // Kép átalakítása byte[] formátumba az adatbázishoz
                ProfileimageBytes = Files.readAllBytes(selectedFile.toPath());

                if (ProfileimageBytes.length == 0) {
                    showAlert("Error", "The selected image is invalid or empty!", Alert.AlertType.ERROR);
                    return;
                }

                StaffCred staffCred = jpaDAO.findStaffcredbyCredentials(Cred);
                staffCred.setpImage(ProfileimageBytes); // Ezen dolgozol
                jpaDAO.updateStafCredpImage(staffCred);


            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the image!", Alert.AlertType.ERROR);
            }
        }

    }
}
