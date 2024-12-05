package unideb.hu.SFMProject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class ClientFormControllerTest {

    private ClientFormController controller;

    @BeforeClass
    public static void initJavaFX() {
        // JavaFX szál inicializálása
        new Thread(() -> {
            Platform.startup(() -> {
                // A JavaFX szál elindítása
            });
        }).start();
    }

    @Before
    public void setUp() {
        controller = new ClientFormController();

        // JavaFX komponensek szimulálása
        controller.AccountButton = new Button();
        controller.TransInOutButton = new Button();
        controller.ViewProdButton = new Button();
        controller.AccountForm = new AnchorPane();
        controller.TransactionInOutForm = new AnchorPane();
        controller.ViewProductStocForm = new AnchorPane();
        controller.ProfilePicture = new ImageView();
        controller.cUserLabel = new Label();
        controller.beszallComboBox = new ComboBox<>();
        controller.beszalListView = new ListView<>();
        controller.clientHistoryList = new ListView<>();
        controller.beszallQuantityField = new TextField();
        controller.productTableView = new TableView<>();

        controller.initialize(); // Gomb-panelek kapcsolatainak inicializálása
    }

    @Test
    public void testInitialize() {

        assertNotNull(controller.buttonPaneMap);
        assertTrue(controller.buttonPaneMap.containsKey(controller.AccountButton));
        assertTrue(controller.buttonPaneMap.containsKey(controller.TransInOutButton));
        assertTrue(controller.buttonPaneMap.containsKey(controller.ViewProdButton));
    }

    @Test
    public void testSwitchForm() {
        Platform.runLater(() -> {
            // Esemény szimulálása
            ActionEvent event = new ActionEvent(controller.AccountButton, null);

            controller.switchForm(event);

            // Ellenőrizzük, hogy csak az AccountForm látható
            assertTrue(controller.AccountForm.isVisible());
            assertFalse(controller.TransactionInOutForm.isVisible());
            assertFalse(controller.ViewProductStocForm.isVisible());
        });
    }

    @Test
    public void testGenerateUniqueRandom() {
        Set<Integer> generatedNumbers = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            int number = controller.generateUniqueRandom();
            assertFalse(generatedNumbers.contains(number));
            generatedNumbers.add(number);
        }
        assertEquals(1000, generatedNumbers.size());
    }

    @Test
    public void testBeszalTransInHandle_ValidInput() {
        Platform.runLater(() -> {
            // Termék szimulálása
            Product testProduct = new Product();
            testProduct.setName("TestProduct");
            testProduct.setQuantity(10);

            controller.jpaDAO = new JPADAO() {
                @Override
                public Product findProductByName(String name) {
                    return name.equals("TestProduct") ? testProduct : null;
                }

                @Override
                public void updateProduct(Product product) {
                    assertEquals("TestProduct", product.getName());
                    assertEquals(20, product.getQuantity()); // 10 + 10
                }
            };

            // JavaFX elemek szimulálása
            controller.beszallComboBox.getItems().add(testProduct.getName());
            controller.beszallComboBox.setValue(testProduct.getName());
            controller.beszallQuantityField.setText("10");

            controller.beszalTransInHandle(null);

            // Lista frissítést ellenőrizzük
            assertEquals(1, controller.beszalListView.getItems().size());
            assertTrue(controller.beszalListView.getItems().get(0).contains("Added 10 to TestProduct"));
        });
    }

    @Test
    public void testBeszalTransOutHandle_ValidInput() {
        Platform.runLater(() -> {
            Product testProduct = new Product();
            testProduct.setName("TestProduct");
            testProduct.setQuantity(10);

            controller.jpaDAO = new JPADAO() {
                @Override
                public Product findProductByName(String name) {
                    return name.equals("TestProduct") ? testProduct : null;
                }

                @Override
                public void updateProduct(Product product) {
                    assertEquals("TestProduct", product.getName());
                    assertEquals(5, product.getQuantity()); // 10 - 5
                }
            };

            // JavaFX elemek szimulálása
            controller.beszallComboBox.getItems().add(testProduct.getName());
            controller.beszallComboBox.setValue(testProduct.getName());
            controller.beszallQuantityField.setText("5");

            controller.beszalTransOutHandle(null);

            // Lista frissítést ellenőrizzük
            assertEquals(1, controller.beszalListView.getItems().size());
            assertTrue(controller.beszalListView.getItems().get(0).contains("Removed 5 from TestProduct"));
        });
    }

    @Test
    public void testHandleClientHistoryRefresh() {
        Platform.runLater(() -> {
            Report report1 = new Report();
            report1.setStarterName("Client (Client)");
            report1.setInOut("IN");
            report1.setProduct("TestProduct: 10");
            report1.setTransactionId(123456);
            report1.setDate(LocalDateTime.now());

            Report report2 = new Report();
            report2.setStarterName("Client (Client)");
            report2.setInOut("OUT");
            report2.setProduct("TestProduct: 5");
            report2.setTransactionId(654321);
            report2.setDate(LocalDateTime.now());

            List<Report> reportList = Arrays.asList(report1, report2);

            controller.jpaDAO = new JPADAO() {
                @Override
                public List<Report> getAllReportsbyName(String name) {
                    return reportList;
                }
            };

            controller.setLoggedInUser("Client", "cred", null);
            controller.handleClientHistoryRefresh(null);

            assertEquals(2, controller.clientHistoryList.getItems().size());
            assertTrue(controller.clientHistoryList.getItems().get(0).contains("IN"));
            assertTrue(controller.clientHistoryList.getItems().get(1).contains("OUT"));
        });
    }
}
