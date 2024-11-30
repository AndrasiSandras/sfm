package unideb.hu.SFMProject;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PasswordManager {

    private static final String STYLE_PATH = "/View/style.css"; // A style.css útvonala

    private Stage popupStage;

    public PasswordManager() {
        this.popupStage = null;
    }

    public void handlePasswordChange(String cred, JPADAO jpaDAO) {
        if (popupStage != null) {
            popupStage.toFront();
            return;
        }

        popupStage = new Stage();
        popupStage.setTitle("Change Password");

        Image logoImage = new Image(getClass().getResourceAsStream("/image/palacklogo.png"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(100);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        TextField currentPasswordField = createPasswordField("Current Password");
        TextField newPasswordField = createPasswordField("New Password");
        Button submitButton = createSubmitButton();

        submitButton.setOnAction(event -> {
            String currentPassword = currentPasswordField.getText();
            String newPassword = newPasswordField.getText();

            try {
                // Ellenőrzések
                if (currentPassword == null || currentPassword.isEmpty()) {
                    showAlert("Error", "Current password cannot be empty!", Alert.AlertType.ERROR);
                    return;
                }

                if (newPassword == null || newPassword.isEmpty()) {
                    showAlert("Error", "New password cannot be empty!", Alert.AlertType.ERROR);
                    return;
                }

                if (newPassword.equals(currentPassword)) {
                    showAlert("Error", "The new password cannot be the same as the current password!", Alert.AlertType.ERROR);
                    return;
                }

                if (newPassword.length() < 4 || newPassword.length() > 50) {
                    showAlert("Error", "Password must be between 4 and 50 characters long!", Alert.AlertType.ERROR);
                    return;
                }

                // Ellenőrizd, hogy staff vagy kliens
                boolean isStaff = jpaDAO.findStaffcredbyCredentials(cred) != null;
                if (isStaff) {
                    handleStaffPasswordChange(cred, jpaDAO, currentPassword, newPassword);
                } else {
                    handleClientPasswordChange(cred, jpaDAO, currentPassword, newPassword);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to change password!", Alert.AlertType.ERROR);
            }
        });

        VBox layout = createLayout(logoImageView, currentPasswordField, newPasswordField, submitButton);
        Scene popupScene = new Scene(layout, 350, 400);
        popupScene.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());

        popupStage.setScene(popupScene);
        popupStage.show();
        popupStage.setOnCloseRequest(event -> popupStage = null);
    }

    private TextField createPasswordField(String promptText) {
        TextField passwordField = new TextField();
        passwordField.setPromptText(promptText);
        passwordField.getStyleClass().add("popup-password-field"); // "View/" előtag eltávolítva
        return passwordField;
    }

    private Button createSubmitButton() {
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("popup-submit-button"); // "View/" előtag eltávolítva
        return submitButton;
    }

    private VBox createLayout(ImageView logoImageView, TextField currentPasswordField, TextField newPasswordField, Button submitButton) {
        VBox layout = new VBox(15, logoImageView, currentPasswordField, newPasswordField, submitButton);
        layout.getStyleClass().add("popup-layout"); // "View/" előtag eltávolítva
        layout.setAlignment(Pos.CENTER);
        return layout;
    }

    private void handleStaffPasswordChange(String cred, JPADAO jpaDAO, String currentPassword, String newPassword) {
        System.out.println("Changing password for staff: " + cred);
        StaffCredential staff = jpaDAO.findStaffcredbyCredentials(cred);

        String[] data = staff.getCredentials().split(",");
        if (data.length < 3 || !data[1].equals(currentPassword)) {
            showAlert("Error", "Current password does not match the database record!", Alert.AlertType.ERROR);
            return;
        }

        data[1] = newPassword;
        String newCred = String.join(",", data);
        staff.setCredentials(newCred);
        jpaDAO.updateStafCredPassword(staff);
        showAlert("Success", "Password successfully changed! Sign out and login!", Alert.AlertType.INFORMATION);
    }

    private void handleClientPasswordChange(String cred, JPADAO jpaDAO, String currentPassword, String newPassword) {
        System.out.println("Changing password for client: " + cred);
        RegLogin client = jpaDAO.findRegLogbyCredentials(cred);
        if (client == null) {
            showAlert("Error", "Sign out and login!", Alert.AlertType.ERROR);
            return;
        }

        String[] data = client.getCredentials().split(",");
        if (data.length < 3 || !data[1].equals(currentPassword)) {
            showAlert("Error", "Current password does not match the database record!", Alert.AlertType.ERROR);
            return;
        }

        data[1] = newPassword;
        String newCred = String.join(",", data);
        client.setCredentials(newCred);
        jpaDAO.updateReglogpassword(client);
        showAlert("Success", "Password successfully changed! Sign out and login!", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
