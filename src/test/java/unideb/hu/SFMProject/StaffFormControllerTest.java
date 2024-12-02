package unideb.hu.SFMProject;


import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

public class StaffFormControllerTest {

    private static StaffFormController controller;

    @BeforeClass
    public static void setUp() {
        new Thread(() -> {
            Platform.startup(() -> {
                // A JavaFX szál elindítása
            });
        }).start();

        controller = new StaffFormController();

        // Szimulált JavaFX komponensek inicializálása
        controller.productNameField = new TextField();
        controller.productPriceField = new TextField();
        controller.productDescriptionField = new TextField();
        controller.productQuantityField = new TextField();
        controller.productComboBox = new ComboBox<>();
        controller.productListView = new ListView<>();
        controller.ProductImageView = new ImageView();
    }

    @Test
    public void testHandleSaveProduct_ValidInputs() {
        Platform.runLater(() -> {
            // Beállítjuk a mezőket érvényes adatokkal
            controller.productNameField.setText("TestProduct");
            controller.productPriceField.setText("99.99");
            controller.productDescriptionField.setText("This is a test product.");

            // Meghívjuk a metódust
            controller.handleSaveProduct(null);

            // Ellenőrizzük, hogy a mezők kiürültek-e
            assertTrue(controller.productNameField.getText().isEmpty());
            assertTrue(controller.productPriceField.getText().isEmpty());
            assertTrue(controller.productDescriptionField.getText().isEmpty());
            assertNull(controller.ProductImageView.getImage());
        });
    }

    @Test
    public void testHandleSaveProduct_MissingFields() {
        Platform.runLater(() -> {
        // Csak a név mezőt töltjük ki
        controller.productNameField.setText("TestProduct");
        controller.productPriceField.setText("");
        controller.productDescriptionField.setText("");

        // Meghívjuk a metódust
        controller.handleSaveProduct(null);

        // Az értékek nem változnak, mivel hiba történt
        assertEquals("TestProduct", controller.productNameField.getText());
        assertEquals("", controller.productPriceField.getText());
        assertEquals("", controller.productDescriptionField.getText());
        });
    }

    @Test
    public void testTransactionOutHandle_ValidQuantity() {
        Platform.runLater(() -> {
        // Szimuláljuk a termékek beállítását
        Product testProduct = new Product();
        testProduct.setName("TestProduct");
        testProduct.setQuantity(10);

        controller.productComboBox.getItems().add(testProduct.getName());
        controller.productComboBox.setValue(testProduct.getName());
        controller.productQuantityField.setText("5");

        // Meghívjuk a metódust
        controller.transactionOutHandle(null);

        // Ellenőrizzük a végeredményt
        assertEquals(5, testProduct.getQuantity());
        });
    }

    @Test
    public void testTransactionOutHandle_InvalidQuantity() {
        Platform.runLater(() -> {
        // Szimuláljuk a termékek beállítását
        Product testProduct = new Product();
        testProduct.setName("TestProduct");
        testProduct.setQuantity(10);

        controller.productComboBox.getItems().add(testProduct.getName());
        controller.productComboBox.setValue(testProduct.getName());
        controller.productQuantityField.setText("15");

        // Meghívjuk a metódust
        controller.transactionOutHandle(null);

        // A termék mennyisége nem változik, mert a kivont mennyiség túl nagy
        assertEquals(10, testProduct.getQuantity());
        });
    }
}
