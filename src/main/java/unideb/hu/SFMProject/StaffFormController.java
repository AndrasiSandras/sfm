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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import java.time.LocalDateTime;
import java.util.*;
import static java.util.stream.Collectors.toList;
import static unideb.hu.SFMProject.PDFGenerator.generateReport;

public class StaffFormController {

    public Button GenerateReportButton;
    public ImageView ProfilePicture;
    public Button HistoryRefreshButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String loggedInUser;
    private String Cred;
    private JPADAO jpaDAO = new JPADAO();
    private Map<Button, AnchorPane> buttonPaneMap;
    private File selectedFile;
    private byte[] imageBytes;
    private byte[] ProfileimageBytes;
    private int quantity;
    int min = 0, max = 1000;

    private final PasswordManager passwordManager = new PasswordManager();
    private final ProfilePictureManager profilePictureManager = new ProfilePictureManager();

    @FXML
    private TextField productNameField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productDescriptionField;
    @FXML
    private ImageView ProductImageView;
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
    private ListView<String> StaffHistoryList;
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

    public void setLoggedInUser(String loggedInUser, String Creds, Image pImage) {
        this.loggedInUser = loggedInUser;
        this.Cred = Creds;
        ProfilePicture.setImage(pImage);
        sUserLabel.setText("Logged in as: "+ loggedInUser);
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
                Image image = new Image(new FileInputStream(selectedFile));
                ProductImageView.setImage(image);
                imageBytes = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the image!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSaveProduct(ActionEvent event) {
        int maxNameLength = 50;
        int maxDescriptionLength = 250;
        double price;

        String name = productNameField.getText();
        String priceText = productPriceField.getText();
        String description = productDescriptionField.getText();
        if (name.isEmpty() || priceText.isEmpty() || description.isEmpty()) {
            showAlert("Error", "All fields must be filled in!", Alert.AlertType.ERROR);
            return;
        }
        if(isItAlreadyIn(name))
        {
            showAlert("Error","Product already in database!",Alert.AlertType.ERROR);
            return;
        }
        if (name.length() > maxNameLength) {
            showAlert("Error", "The product name is too long! Maximum " + maxNameLength + " characters allowed.", Alert.AlertType.ERROR);
            return;
        }
        if (description.length() > maxDescriptionLength) {
            showAlert("Error", "The product description is too long! Maximum " + maxDescriptionLength + " characters allowed.", Alert.AlertType.ERROR);
            return;
        }
        try {
            price = Double.parseDouble(priceText); // Ár konvertálása számra
        } catch (NumberFormatException e) {
            showAlert("Error", "The price must be a valid number!", Alert.AlertType.ERROR);
            return;
        }

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setDescription(description);

        if (imageBytes != null) {
            newProduct.setImage(imageBytes);
        }
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
                .collect(toList());

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

    public void reportGenerator(ActionEvent actionEvent) {
        generateReport();
    }

    public void fillComboBox() {
        List<Product> products;
        productComboBox.getItems().clear();
        products = jpaDAO.getAllProduct();
        List<String> productNames = products.stream()
                .map(Product::getName)
                .collect(toList());

        productComboBox.getItems().addAll(productNames);
        productComboBox.hide();
        productComboBox.show();
    }

    public void transctionInHandle(ActionEvent actionEvent) {
        int quantity;

        // Ellenőrizze, hogy van-e kiválasztott termék
        String selectedProductName = productComboBox.getValue();
        String quantityText = productQuantityField.getText();

        if (selectedProductName == null) {
            showAlert("Error", "Please select a product!", Alert.AlertType.ERROR);
            return;
        }

        // Mennyiség ellenőrzése
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for quantity!", Alert.AlertType.ERROR);
            return;
        }

        if (quantity <= min || quantity >= max) {
            showAlert("Error", "Quantity must be between " + (min + 1) + " and " + max + "!", Alert.AlertType.ERROR);
            return;
        }

        // Kiválasztott termék keresése
        Product product = jpaDAO.findProductByName(selectedProductName);
        if (product == null) {
            showAlert("Error", "Product not found in the database!", Alert.AlertType.ERROR);
            return;
        }

        if (product.getQuantity() + quantity > max) {
            showAlert("Error", "The product quantity exceeds the maximum limit!", Alert.AlertType.ERROR);
            return;
        }

        // Termék mennyiségének frissítése
        product.setQuantity(product.getQuantity() + quantity);

        try {
            jpaDAO.updateProduct(product);

            // Terméklista frissítése
            productListView.getItems().add("Added " + quantity + " to " + product.getName() + " (New quantity: " + product.getQuantity() + ")");

            // Jelentés létrehozása és mentése
            Report report = new Report();
            report.setTransactionId(generateUniqueRandom());
            report.setInOut("IN");
            report.setStarterName(loggedInUser + " (Staff)");
            report.setProduct(product.getName() + ": " + quantity + ",(New quantity: " + product.getQuantity() + ")");
            report.setDate(LocalDateTime.now()); // Dátum beállítása közvetlenül

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
                Report report = new Report();
                report.setTransactionId(generateUniqueRandom());
                report.setInOut("OUT");
                report.setStarterName(loggedInUser + " (Staff)");
                report.setProduct(product.getName()+": "+quantity + ",(New quantity: " + product.getQuantity() + ")");
                report.setDate(LocalDateTime.now());
                jpaDAO.saveReport(report);

           } catch (Exception e) {
                showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
            }
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

        String selectedProductName = productComboBox.getValue();

        if (selectedProductName == null || selectedProductName.isEmpty()) {
            showAlert("Error", "Please select a product to delete!", Alert.AlertType.ERROR);
            return;
        }

        Product productToDelete = jpaDAO.findProductByName(selectedProductName);

        if (productToDelete == null) {
            showAlert("Error", "The selected product was not found in the database.", Alert.AlertType.ERROR);
            return;
        }

        try {
            jpaDAO.deleteProduct(productToDelete);
            showAlert("Success", "The product has been successfully deleted!", Alert.AlertType.INFORMATION);

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

    public void refreshReportHandle(ActionEvent actionEvent) {
        generateReportTableivew();
    }

    public void generateReportTableivew()
    {
        ObservableList<Report> reports;
        reports = FXCollections.observableList(jpaDAO.getAllReports());
        transactionId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTransactionId()).asObject());
        inOut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInOut()));
        pName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStarterName()));
        pProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct()));
        reportTableView.setItems(reports);
    }

    @FXML
    public void handleChangeProfilePicture(ActionEvent actionEvent) {
        profilePictureManager.handleStaffProfilePicture(Cred, jpaDAO, ProfilePicture);
    }

    @FXML
    private void handleChangePasswordStaff() {
        passwordManager.handlePasswordChange(Cred, jpaDAO);
    }

    public void handelHistoryRefresh(ActionEvent actionEvent) {
        List<Report> reportList = jpaDAO.getAllReportsbyName(this.loggedInUser + " (Staff)");
        List<String> stringList = reportList.stream()
                .map(report -> report.getStarterName() + ", " + report.getInOut() + ", " + report.getProduct() + ", " + report.getTransactionId() + ", " + report.getDate())
                .collect(toList());

        ObservableList<String> observableReportList = FXCollections.observableArrayList(stringList);
        StaffHistoryList.setItems(observableReportList);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
