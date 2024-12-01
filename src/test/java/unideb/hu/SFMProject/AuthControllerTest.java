package unideb.hu.SFMProject;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class AuthControllerTest  {

    private TextField emailText;
    private TextField passwordText;
    private TextField rePasswordText;
    private TextField userNameText;
    private Label registerErrorText;

    private AuthController authController;

    @Before
    public void setUp() {
        // Inicializálja a JavaFX platformot
        Platform.startup(() -> {});
    }

    @Test
    public void testRegister_PasswordTooShort() {


        TextField emailText = new TextField(); emailText.setText("test@example.com");
        TextField passwordText = new TextField(); passwordText.setText("123");
        TextField rePasswordText = new TextField(); rePasswordText.setText("123");
        TextField userNameText = new TextField(); userNameText.setText("testuser");
        Label registerErrorText = new Label();
        JPADAO jpadao = new JPADAO();
        AuthController authController = new AuthController(emailText,passwordText,rePasswordText, userNameText,registerErrorText,jpadao);

        assertFalse(authController.register());

    }

    @Test
    public void testRegister_PasswordsDoNotMatch() {
        // Simulate user input
        emailText.setText("test@example.com");
        passwordText.setText("password");
        rePasswordText.setText("differentpassword");
        userNameText.setText("testuser");

        // Trigger the registration process
        boolean result = authController.register();

        // Verify the expected behavior
        assertFalse(result);
        assertEquals("Passwords do not match!", registerErrorText.getText());
    }

    @Test
    public void testRegister_ValidRegistration() {
        // Simulate user input
        emailText.setText("test@example.com");
        passwordText.setText("password");
        rePasswordText.setText("password");
        userNameText.setText("testuser");

        // Simulate valid registration
        boolean result = authController.register();

        // Verify the expected outcome
        assertTrue(result);
        assertEquals("Registration successful.", registerErrorText.getText());
    }

    @Test
    public void testRegister_EmailInvalid() {
        // Simulate user input
        emailText.setText("invalid-email");
        passwordText.setText("password");
        rePasswordText.setText("password");
        userNameText.setText("testuser");

        // Trigger the registration process
        boolean result = authController.register();

        // Verify the expected behavior
        assertFalse(result);
        assertEquals("Invalid email address!", registerErrorText.getText());
    }
}
