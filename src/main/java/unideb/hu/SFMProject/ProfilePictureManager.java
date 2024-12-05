package unideb.hu.SFMProject;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ProfilePictureManager {

    /**
     * Handles profile picture upload for clients.
     *
     * @param cred      The credential of the client.
     * @param jpaDAO    The DAO object for database operations.
     * @param profileImageView The ImageView to display the profile picture.
     */
    public void handleClientProfilePicture(String cred, JPADAO jpaDAO, ImageView profileImageView) {
        File selectedFile = openFileChooser();
        if (selectedFile == null) return;

        try {
            byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
            if (imageBytes.length == 0) {
                showAlert("Error", "The selected image is invalid or empty!", Alert.AlertType.ERROR);
                return;
            }

            Image image = new Image(new FileInputStream(selectedFile));
            profileImageView.setImage(image);

            RegLogin regLogin = jpaDAO.findRegLogbyCredentials(cred);
            if (regLogin != null) {
                regLogin.setProfileImage(imageBytes);
                jpaDAO.updateRegLogpImage(regLogin);
            } else {
                StaffCredential staffCredential = jpaDAO.findStaffcredbyCredentials(cred);
                staffCredential.setProfileImage(imageBytes);
                jpaDAO.updateStafCredpImage(staffCredential);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the image!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles profile picture upload for staff.
     *
     * @param cred      The credential of the staff.
     * @param jpaDAO    The DAO object for database operations.
     * @param profileImageView The ImageView to display the profile picture.
     */
    public void handleStaffProfilePicture(String cred, JPADAO jpaDAO, ImageView profileImageView) {
        File selectedFile = openFileChooser();
        if (selectedFile == null) return;

        try {
            byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
            if (imageBytes.length == 0) {
                showAlert("Error", "The selected image is invalid or empty!", Alert.AlertType.ERROR);
                return;
            }

            Image image = new Image(new FileInputStream(selectedFile));
            profileImageView.setImage(image);

            StaffCredential staffCredential = jpaDAO.findStaffcredbyCredentials(cred);
            if (staffCredential != null) {
                staffCredential.setProfileImage(imageBytes);
                jpaDAO.updateStafCredpImage(staffCredential);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the image!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Opens a file chooser for image selection.
     *
     * @return The selected file or null if no file was selected.
     */
    public File openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        return fileChooser.showOpenDialog(null);
    }

    /**
     * Shows an alert dialog with the given message and alert type.
     *
     * @param title   The title of the alert.
     * @param message The message to display in the alert.
     * @param alertType The type of alert to display.
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
