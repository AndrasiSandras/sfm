package unideb.hu.SFMProject;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthControllerTest  {

    @BeforeClass
    public static void setUp() {
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
        assertEquals("Password must be at least 4 characters long!",registerErrorText.getText());
    }

    @Test
    public void testRegister_PasswordsDoNotMatch() {
        // Simulate user input
        TextField emailText = new TextField(); emailText.setText("test@example.com");
        TextField passwordText = new TextField(); passwordText.setText("password");
        TextField rePasswordText = new TextField(); rePasswordText.setText("differentpassword");
        TextField userNameText = new TextField(); userNameText.setText("testuser");
        Label registerErrorText = new Label();
        JPADAO jpadao = new JPADAO();
        AuthController authController = new AuthController(emailText,passwordText,rePasswordText, userNameText,registerErrorText,jpadao);
        // Verify the expected behavior
        assertFalse(authController.register());
        assertEquals("Passwords do not match!", registerErrorText.getText());
    }

    @Test
    public void testRegister_ValidRegistration() {
        // Simulate user input
        TextField emailText = new TextField(); emailText.setText("test@example.com");
        TextField passwordText = new TextField(); passwordText.setText("password");
        TextField rePasswordText = new TextField(); rePasswordText.setText("password");
        TextField userNameText = new TextField(); userNameText.setText("testuser");
        Label registerErrorText = new Label();
        JPADAO jpadao = new JPADAO();
        AuthController authController = new AuthController(emailText,passwordText,rePasswordText, userNameText,registerErrorText,jpadao);
        // Verify the expected behavior
        assertTrue(authController.register());
        assertEquals("Registration successful.", registerErrorText.getText());
    }

    @Test
    public void testRegister_EmailInvalid() {
        TextField emailText = new TextField(); emailText.setText("invalid-email");
        TextField passwordText = new TextField(); passwordText.setText("password");
        TextField rePasswordText = new TextField(); rePasswordText.setText("password");
        TextField userNameText = new TextField(); userNameText.setText("testuser");
        Label registerErrorText = new Label();
        JPADAO jpadao = new JPADAO();
        AuthController authController = new AuthController(emailText,passwordText,rePasswordText, userNameText,registerErrorText,jpadao);
        // Verify the expected behavior
        assertFalse(authController.register());
        assertEquals("Invalid email address!", registerErrorText.getText());
    }

    @Test
    public void testAreFieldsValid(){
        TextField emailText = new TextField(); emailText.setText("test@example.com");
        TextField passwordText = new TextField(); passwordText.setText("pas,sword");
        TextField rePasswordText = new TextField(); rePasswordText.setText("password");
        TextField userNameText = new TextField(); userNameText.setText("test,user");
        Label registerErrorText = new Label();
        JPADAO jpadao = new JPADAO();
        AuthController authController = new AuthController(emailText,passwordText,rePasswordText, userNameText,registerErrorText,jpadao);
        // Verify the expected behavior
        assertFalse(authController.register());
        assertEquals("No field can contain the comma character!", registerErrorText.getText());
    }

    @Test
    public void testAreFieldsEmpty(){
        TextField emailText = new TextField(); emailText.setText("");
        TextField passwordText = new TextField(); passwordText.setText("pas,sword");
        TextField rePasswordText = new TextField(); rePasswordText.setText("password");
        TextField userNameText = new TextField(); userNameText.setText("test,user");
        Label registerErrorText = new Label();
        JPADAO jpadao = new JPADAO();
        AuthController authController = new AuthController(emailText,passwordText,rePasswordText, userNameText,registerErrorText,jpadao);
        // Verify the expected behavior
        assertFalse(authController.register());
        assertEquals("All fields must be filled out!", registerErrorText.getText());
    }
}
