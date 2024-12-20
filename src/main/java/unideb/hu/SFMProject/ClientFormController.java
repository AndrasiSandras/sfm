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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

public class ClientFormController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    public ImageView ProfilePicture;
    JPADAO jpaDAO = new JPADAO();
    Map<Button, AnchorPane> buttonPaneMap;
    private String loggedInUser;
    private String Cred;
    int min = 0,max = 1000;

    private final PasswordManager passwordManager = new PasswordManager();
    private final ProfilePictureManager profilePictureManager = new ProfilePictureManager();
    private final TableViewManager tableViewManager = new TableViewManager();

    @FXML
    Button AccountButton;
    @FXML
    Button TransInOutButton;
    @FXML
    Button ViewProdButton;
    @FXML
    AnchorPane AccountForm;
    @FXML
    AnchorPane TransactionInOutForm;
    @FXML
    AnchorPane ViewProductStocForm;
    @FXML
    ComboBox<String> beszallComboBox;
    @FXML
    ListView<String> beszalListView;
    @FXML
    ListView<String> clientHistoryList;
    @FXML
    TextField beszallQuantityField;
    @FXML
    TableView<Product> productTableView;
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
    Label cUserLabel;

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

    public void setLoggedInUser(String loggedInUser, String Creds, Image pImage) {
        this.loggedInUser = loggedInUser;
        this.Cred = Creds;
        ProfilePicture.setImage(pImage);
        cUserLabel.setText("Logged in as: " + loggedInUser);
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void fillComboBoxBeszall(MouseEvent mouseEvent) {
            List<Product> products;
            beszallComboBox.getItems().clear();

            products = jpaDAO.getAllProduct();

            List<String> productNames = products.stream()
                    .map(Product::getName)
                    .collect(Collectors.toList());

            beszallComboBox.getItems().addAll(productNames);

            beszallComboBox.hide();
            beszallComboBox.show();
    }

    public void beszalTransInHandle(ActionEvent actionEvent) {
        String selectedProductName = beszallComboBox.getValue(); // Kiválasztott termék neve
        if (selectedProductName == null) {
            showAlert("Error", "Please select a product!", Alert.AlertType.ERROR);
            return;
        }
        String quantityText = beszallQuantityField.getText(); // A beírt mennyiség
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
            beszalListView.getItems().add("Added " + quantity + " to " + product.getName() + " (New quantity: " + product.getQuantity() + ")");
            Report report = new Report();
            report.setTransactionId(generateUniqueRandom());
            report.setInOut("IN");
            report.setStarterName(loggedInUser + " (Client)");
            report.setProduct(product.getName()+": "+quantity + ",(New quantity: " + product.getQuantity() + ")");
            report.setDate(LocalDateTime.now());
            jpaDAO.saveReport(report);
        } catch (Exception e) {
            showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
        }
    }

    public void beszalTransOutHandle(ActionEvent actionEvent) {

        String selectedProductName = beszallComboBox.getValue();
        if (selectedProductName == null) {
            showAlert("Error", "Please select a product!", Alert.AlertType.ERROR);
            return;
        }
        String quantityText = beszallQuantityField.getText();
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
            beszalListView.getItems().add(("Removed " + quantity + " from " + product.getName() + " (New quantity: " + product.getQuantity() + ")"));
            Report report = new Report();
            report.setTransactionId(generateUniqueRandom());
            report.setInOut("OUT");
            report.setStarterName(loggedInUser + " (Client)");
            report.setProduct(product.getName()+": "+quantity + ",(New quantity: " + product.getQuantity() + ")");
            report.setDate(LocalDateTime.now());
            jpaDAO.saveReport(report);

        } catch (Exception e) {
            showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
        }
    }

    public void RefreshTableView(ActionEvent actionEvent) {
        generateTableView();
    }

    @FXML
    private void generateTableView() {
        tableViewManager.generateTableView(
                jpaDAO.getAllProduct(),
                tableName,
                tablePrice,
                tableQuantity,
                tableDescription,
                tableImage,
                productTableView
        );
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

    @FXML
    public void handleClientProfilePicture(ActionEvent actionEvent) {
        profilePictureManager.handleClientProfilePicture(Cred, jpaDAO, ProfilePicture);
    }

    @FXML
    private void handleChangePasswordClient() {
        passwordManager.handlePasswordChange(Cred, jpaDAO);
    }

    public void handleClientHistoryRefresh(ActionEvent actionEvent) {
        List<Report> reportList = jpaDAO.getAllReportsbyName(this.loggedInUser + " (Client)");
        List<String> stringList = reportList.stream()
                .map(report -> report.getStarterName() + ", " + report.getInOut() + ", " + report.getProduct() + ", " + report.getTransactionId() + ", " + report.getDate())
                .collect(toList());

        ObservableList<String> observableReportList = FXCollections.observableArrayList(stringList);
        clientHistoryList.setItems(observableReportList);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}