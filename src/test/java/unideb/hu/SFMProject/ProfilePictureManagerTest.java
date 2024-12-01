package unideb.hu.SFMProject;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.Mockito.*;

class ProfilePictureManagerTest {

    private ProfilePictureManager profilePictureManager;
    private JPADAO jpaDAO;
    private ImageView profileImageView;
    private File file;
    private RegLogin regLogin;
    private StaffCredential staffCredential;

    @BeforeEach
    void setUp() {
        profilePictureManager = new ProfilePictureManager();
        jpaDAO = mock(JPADAO.class);
        profileImageView = mock(ImageView.class);
        file = mock(File.class);
        regLogin = mock(RegLogin.class);
        staffCredential = mock(StaffCredential.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleClientProfilePictureSuccess() throws IOException {
        // Arrange
        String cred = "clientCred";
        File file = mock(File.class); // Mock létrehozása a File-hoz
        when(file.exists()).thenReturn(true); // Mock viselkedés beállítása

        // Act
        profilePictureManager.handleClientProfilePicture(cred, jpaDAO, profileImageView);

        // Assert
        verify(profileImageView).setImage(any(Image.class));  // Ellenőrizzük, hogy a profilkép be lett állítva
    }

    @Test
    void handleClientProfilePictureFail() throws IOException {
        // Arrange
        String cred = "clientCred";
        FileInputStream fileInputStream = mock(FileInputStream.class);
        byte[] imageBytes = new byte[0]; // empty image array simulates invalid image
        when(file.exists()).thenReturn(true);
        when(file.toPath()).thenReturn(mock(java.nio.file.Path.class));
        when(Files.readAllBytes(file.toPath())).thenReturn(imageBytes);

        // Act
        profilePictureManager.handleClientProfilePicture(cred, jpaDAO, profileImageView);

        // Assert
        verify(profileImageView, never()).setImage(any(Image.class));  // Ensure the image was not set
        // You would also want to verify an alert is shown in the case of failure (if needed)
    }

    @Test
    void handleStaffProfilePictureSuccess() throws IOException {
        // Arrange
        String cred = "staffCred";
        FileInputStream fileInputStream = mock(FileInputStream.class);
        byte[] imageBytes = new byte[]{1, 2, 3}; // mock image byte array
        when(file.exists()).thenReturn(true);
        when(file.toPath()).thenReturn(mock(java.nio.file.Path.class));
        when(Files.readAllBytes(file.toPath())).thenReturn(imageBytes);
        when(jpaDAO.findStaffcredbyCredentials(cred)).thenReturn(staffCredential);

        // Act
        profilePictureManager.handleStaffProfilePicture(cred, jpaDAO, profileImageView);

        // Assert
        verify(profileImageView).setImage(any(Image.class));  // Verify that the profile image was set
        verify(jpaDAO).updateStafCredpImage(staffCredential);  // Ensure the DAO update was called
    }

    @Test
    void handleStaffProfilePictureFail() throws IOException {
        // Arrange
        String cred = "staffCred";
        byte[] imageBytes = new byte[0]; // empty image array simulates invalid image
        when(file.exists()).thenReturn(true);
        when(file.toPath()).thenReturn(mock(java.nio.file.Path.class));
        when(Files.readAllBytes(file.toPath())).thenReturn(imageBytes);

        // Act
        profilePictureManager.handleStaffProfilePicture(cred, jpaDAO, profileImageView);

        // Assert
        verify(profileImageView, never()).setImage(any(Image.class));  // Ensure the image was not set
    }
}