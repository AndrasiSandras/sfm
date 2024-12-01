package unideb.hu.SFMProject;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.List;

/**
 * The AuthController handles user registration logic, including validation of input fields.
 * It interacts with the JPADAO to check if the username/email already exists.
 */
public class AuthController {

    // UI elements and DAO instance
    TextField emailText;
    TextField passwordText;
    TextField rePasswordText;
    TextField userNameText;
    Label registerErrorText;
    private JPADAO jpadao;

    /**
     * Constructor for AuthController to initialize the form fields and the JPADAO.
     */
    public AuthController(TextField emailText, TextField passwordText, TextField rePasswordText,
                          TextField userNameText, Label registerErrorText, JPADAO jpadao) {
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.rePasswordText = rePasswordText;
        this.userNameText = userNameText;
        this.registerErrorText = registerErrorText;
        this.jpadao = jpadao;
    }
    public AuthController()
    {
    }

    // Helper Methods for Validation
    private boolean isValidEmailFormat() {
        String email = emailText.getText();
        return email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,}$");
    }

    private boolean areFieldsFilledOut() {
        return !emailText.getText().isEmpty() && !userNameText.getText().isEmpty() &&
                !passwordText.getText().isEmpty() && !rePasswordText.getText().isEmpty();
    }

    private boolean areFieldsValid() {
        return !emailText.getText().contains(",") && !userNameText.getText().contains(",") &&
                !passwordText.getText().contains(",") && !rePasswordText.getText().contains(",");
    }

    private boolean isUsernameOrEmailAlreadyUsed() {
        List<String> userList = jpadao.getAllRegLog();
        return userList.stream()
                .anyMatch(user -> {
                    String[] credentials = user.split(",");
                    return credentials[0].equals(userNameText.getText()) && credentials[2].equals(emailText.getText());
                });
    }

    private boolean isPasswordValidLength() {
        return passwordText.getText().length() >= 4;
    }

    private boolean doPasswordsMatch() {
        return passwordText.getText().equals(rePasswordText.getText());
    }

    // Main Registration Validation Logic
    public boolean register() {
        if (!areFieldsFilledOut()) {
            setError("All fields must be filled out!");
            return false;
        }

        if (isUsernameOrEmailAlreadyUsed()) {
            setError("Username or Email is already being used!");
            return false;
        }

        if (!areFieldsValid()) {
            setError("No field can contain the comma character!");
            return false;
        }

        if (!isValidEmailFormat()) {
            setError("Invalid email address!");
            return false;
        }

        if (!isPasswordValidLength()) {
            setError("Password must be at least 4 characters long!");
            return false;
        }

        if (!doPasswordsMatch()) {
            setError("Passwords do not match!");
            return false;
        }

        setSuccess("Registration successful.");
        return true;
    }

    // Set error message in the UI
    private void setError(String message) {
        registerErrorText.setText(message);
    }

    // Set success message in the UI
    private void setSuccess(String message) {
        registerErrorText.setText(message);
    }
}
