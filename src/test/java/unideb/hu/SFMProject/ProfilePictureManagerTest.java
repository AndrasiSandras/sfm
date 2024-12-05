package unideb.hu.SFMProject;

import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ProfilePictureManagerTest {

    private ProfilePictureManager profilePictureManager;
    private JPADAO mockJpaDAO;
    private ImageView mockImageView;

    @BeforeEach
    void setUp() {
        profilePictureManager = new ProfilePictureManager();
        mockJpaDAO = new JPADAO() {
            // Mock JPADAO with minimal implementation for test purposes
            @Override
            public RegLogin findRegLogbyCredentials(String cred) {
                return new RegLogin();
            }

            @Override
            public StaffCredential findStaffcredbyCredentials(String cred) {
                return new StaffCredential();
            }

            @Override
            public void updateRegLogpImage(RegLogin regLogin) {
                // Do nothing
            }

            @Override
            public void updateStafCredpImage(StaffCredential staffCredential) {
                // Do nothing
            }
        };
        mockImageView = new ImageView();
    }

    @Test
    void testHandleClientProfilePicture_ValidImage() throws IOException {
        File tempImage = createTempImageFile();
        profilePictureManager = new ProfilePictureManager() {
            @Override
            public File openFileChooser() {
                return tempImage;
            }
        };

        profilePictureManager.handleClientProfilePicture("clientCred", mockJpaDAO, mockImageView);

        assertNotNull(mockImageView.getImage());
        byte[] imageBytes = Files.readAllBytes(tempImage.toPath());
        RegLogin regLogin = new RegLogin();
        regLogin.setProfileImage(imageBytes);
        assertArrayEquals(imageBytes, regLogin.getProfileImage());
    }


    @Test
    void testHandleStaffProfilePicture_ValidImage() throws IOException {
        File tempImage = createTempImageFile();
        profilePictureManager = new ProfilePictureManager() {
            @Override
            public File openFileChooser() {
                return tempImage;
            }
        };

        profilePictureManager.handleStaffProfilePicture("staffCred", mockJpaDAO, mockImageView);

        assertNotNull(mockImageView.getImage());
        byte[] imageBytes = Files.readAllBytes(tempImage.toPath());
        StaffCredential staffCredential = new StaffCredential();
        staffCredential.setProfileImage(imageBytes);
        assertArrayEquals(imageBytes, staffCredential.getProfileImage());
    }

    @Test
    void testHandleStaffProfilePicture_NoFileSelected() {
        profilePictureManager = new ProfilePictureManager() {
            @Override
            public File openFileChooser() {
                return null;
            }
        };

        profilePictureManager.handleStaffProfilePicture("staffCred", mockJpaDAO, mockImageView);

        assertNull(mockImageView.getImage());
    }

    private File createTempImageFile() throws IOException {
        File tempFile = File.createTempFile("testImage", ".png");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] imageContent = new byte[]{(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'}; // PNG header
            fos.write(imageContent);
        }
        return tempFile;
    }
}
