package unideb.hu.SFMProject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class ClientFormController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    int min = 0,max = 1000;

    @FXML
    private Button AccountButton, TransInOutButton, ViewProdButton, LogoutButton;

    @FXML
    private AnchorPane AccountForm, TransactionInOutForm, ViewProductStocForm;
    @FXML
    private ComboBox<String> beszallComboBox;
    @FXML
    private ListView<String> beszalListView;
    @FXML
    private TextField beszallQuantityField;
    @FXML
    private TableView<Product> beszallTableview;
    @FXML
    private TableColumn<Product,String> beszalTableName;
    @FXML
    private TableColumn<Product,Double> beszalTablePrice;
    @FXML
    private TableColumn<Product,Integer> beszalTableStock;
    @FXML
    private TableColumn<Product,String> beszalTableDescription;
    @FXML
    private TableColumn<Product, ImageView> beszalTableImage;
    @FXML
    private Label loggedInField;

    public void setLoggedInField(String s)
    {
        loggedInField.setText(s);
    }

    private JPADAO jpaDAO = new JPADAO();

    private Map<Button, AnchorPane> buttonPaneMap;

    private List<String> report2List;

    public List<String> getReport2List()
    {
        return report2List;
    }


    private String transIn;
    private String transOut;

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut;
    }

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

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

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            //setTransIn(generateUniqueRandom()+ ",IN," + product.getName() + "," + product.getQuantity() + "db\n");


        } catch (Exception e) {
            showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
        }
    }

    public void beszalTransOutHandle(ActionEvent actionEvent) {

        String selectedProductName = beszallComboBox.getValue(); // A kiválasztott termék neve
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
            //setTransOut(generateUniqueRandom()+ ",OUT," + product.getName() + "," + product.getQuantity() + "db\n");

        } catch (Exception e) {
            showAlert("Error", "Failed to update the product quantity!", Alert.AlertType.ERROR);
        }
    }

    public void RefreshTableView(ActionEvent actionEvent) {
        generateTableView();
    }

    private void generateTableView() {
        ObservableList<Product> products;

        products = FXCollections.observableList(jpaDAO.getAllProduct());

        beszalTableName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        beszalTablePrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        beszalTableStock.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        beszalTableDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        beszalTableImage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<Product, ImageView> param) {
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



        beszallTableview.setItems(products);
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



}