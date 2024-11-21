package unideb.hu.SFMProject;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class AuthController {

    private TextField emailText;
    private TextField passwordText;
    private TextField rePasswordText;
    private TextField userNameText;
    private Label registerErrorText;

    public AuthController(TextField emailText, TextField passwordText, TextField rePasswordText, TextField userNameText, Label registerErrorText) {
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.rePasswordText = rePasswordText;
        this.userNameText = userNameText;
        this.registerErrorText = registerErrorText;
    }

    public boolean validateEmail() {
        String email = emailText.getText();
        return email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,}$");
    }

    public boolean validatePasswordLength() {
        return passwordText.getText().length() >= 4;
    }

    public boolean passwordsMatch() {
        return passwordText.getText().equals(rePasswordText.getText());
    }

    public boolean fieldsFilledOut() {
        return !emailText.getText().isEmpty() &&
                !userNameText.getText().isEmpty() &&
                !passwordText.getText().isEmpty() &&
                !rePasswordText.getText().isEmpty();
    }
    public boolean fieldsCantContainDash()
    {
        return !emailText.getText().contains(",") &&
                !userNameText.getText().contains(",") &&
                !passwordText.getText().contains(",") &&
                !rePasswordText.getText().contains(",");
    }

    public boolean isItAlreadyUsed()
    {
       JPADAO jpadao = new JPADAO();
        List<String> list = jpadao.getAllRegLog();
        String[] cred;
        for(String e : list)
        {
            cred = e.split(",");
            if(cred[0].equals(userNameText.getText()) && cred[2].equals(emailText.getText()));
            {
                return true;
            }
        }

        return false;
    }

    public boolean register() {
        if (!fieldsFilledOut()) {
            registerErrorText.setText("All fields must be filled out!");
            return false;
        }

        if(!isItAlreadyUsed())
        {
            registerErrorText.setText("Username or Email is already being used!");
            return false;
        }

        if (!fieldsCantContainDash())
        {
            registerErrorText.setText("No filed can contain dash character!");
            return false;
        }

        if (!validateEmail()) {
            registerErrorText.setText("Invalid email address!");
            return false;
        }

        if (!validatePasswordLength()) {
            registerErrorText.setText("Password must be at least 4 characters long!");
            return false;
        }

        if (!passwordsMatch()) {
            registerErrorText.setText("Passwords do not match!");
            return false;
        }

        registerErrorText.setText("Registration successful.");
        return true;
    }
}