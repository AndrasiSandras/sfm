package unideb.hu.SFMProject;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartingSceneControllerTest {

    private StartingSceneController controller;
    private JPADAO mockJpaDAO;

    @BeforeAll
    public static void InitializeToolkit()
    {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws IOException {
        controller = new StartingSceneController();
        mockJpaDAO = new JPADAO() {
            // Mock JPADAO with minimal implementation for test purposes
            @Override
            public List<String> getAllRegLog() {
                List<String> credentials = new ArrayList<>();
                credentials.add("clientUser,password123,email@example.com");
                return credentials;
            }

            @Override
            public List<String> getAllStaffCred() {
                List<String> credentials = new ArrayList<>();
                credentials.add("staffUser,staffPass123");
                return credentials;
            }
        };

        controller.jpadao = mockJpaDAO;

        // Initialize the fields required for testing
        controller.clientLoginErrorText = new Label();
        controller.LoginErrorText = new Label();
        controller.ClientUserNameText = new TextField();
        controller.ClientPasswordText = new TextField();
        controller.staffNameText = new TextField();
        controller.staffPasswordText = new TextField();
    }

    @Test
    void testClogin_ValidClientCredentials() throws IOException {
        controller.ClientUserNameText.setText("clientUser");
        controller.ClientPasswordText.setText("password123");

        String[] credentials = {
                controller.ClientUserNameText.getText(),
                controller.ClientPasswordText.getText()
        };

        boolean result = controller.Clogin(credentials);

        assertTrue(result);
        assertEquals("clientUser", controller.loginName);
    }

    @Test
    void testClogin_InvalidClientCredentials() throws IOException {
        controller.ClientUserNameText.setText("wrongUser");
        controller.ClientPasswordText.setText("wrongPass");

        String[] credentials = {
                controller.ClientUserNameText.getText(),
                controller.ClientPasswordText.getText()
        };

        boolean result = controller.Clogin(credentials);

        assertFalse(result);
        assertEquals("The Username or Password is incorrect. Try again.", controller.clientLoginErrorText.getText());
    }

    @Test
    void testSlogin_ValidStaffCredentials() throws IOException {
        controller.staffNameText.setText("staffUser");
        controller.staffPasswordText.setText("staffPass123");

        String[] credentials = {
                controller.staffNameText.getText(),
                controller.staffPasswordText.getText()
        };

        boolean result = controller.Slogin(credentials);

        assertTrue(result);
        assertEquals("staffUser", controller.loginName);
    }

    @Test
    void testSlogin_InvalidStaffCredentials() throws IOException {
        controller.staffNameText.setText("wrongStaff");
        controller.staffPasswordText.setText("wrongPass");

        String[] credentials = {
                controller.staffNameText.getText(),
                controller.staffPasswordText.getText()
        };

        boolean result = controller.Slogin(credentials);

        assertFalse(result);
        assertEquals("The Username or Password is incorrect. Try again.", controller.LoginErrorText.getText());
    }
}
