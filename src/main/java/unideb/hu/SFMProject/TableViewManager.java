package unideb.hu.SFMProject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.util.List;

public class TableViewManager {

    /**
     * Generates a TableView for displaying Product data.
     *
     * @param products          The list of products to display.
     * @param tableName         The table column for the product name.
     * @param tablePrice        The table column for the product price.
     * @param tableQuantity     The table column for the product quantity.
     * @param tableDescription  The table column for the product description.
     * @param tableImage        The table column for the product image.
     * @param tableView         The TableView to populate.
     */
    public void generateTableView(
            List<Product> products,
            TableColumn<Product, String> tableName,
            TableColumn<Product, Double> tablePrice,
            TableColumn<Product, Integer> tableQuantity,
            TableColumn<Product, String> tableDescription,
            TableColumn<Product, ImageView> tableImage,
            TableView<Product> tableView) {

        ObservableList<Product> observableProducts = FXCollections.observableList(products);

        // Bind columns to Product properties
        tableName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tablePrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        tableQuantity.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        tableDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        tableImage.setCellValueFactory(new Callback<>() {
            @Override
            public SimpleObjectProperty<ImageView> call(TableColumn.CellDataFeatures<Product, ImageView> param) {
                byte[] imageBytes = param.getValue().getImage();
                Image image = (imageBytes != null) ? new Image(new ByteArrayInputStream(imageBytes)) : null;

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                return new SimpleObjectProperty<>(imageView);
            }
        });

        // Set the TableView's items
        tableView.setItems(observableProducts);
    }
}
