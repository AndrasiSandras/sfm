package unideb.hu.SFMProject;

import javax.persistence.*;

/**
 * Entity class representing staff credentials and profile images.
 */
@Entity
public class StaffCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String credentials;

    @Lob
    @Column(nullable = true)
    private byte[] profileImage;

    /**
     * Returns the ID of the staff credential.
     *
     * @return the ID as a long value.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the credentials associated with the staff.
     *
     * @return the credentials as a String.
     */
    public String getCredentials() {
        return credentials;
    }

    /**
     * Sets the credentials for the staff.
     *
     * @param credentials a String containing the staff credentials.
     */
    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    /**
     * Returns the profile image associated with the staff.
     *
     * @return the profile image as a byte array.
     */
    public byte[] getProfileImage() {
        return profileImage;
    }

    /**
     * Sets the profile image for the staff and logs its size.
     *
     * @param profileImage a byte array representing the profile image.
     */
    public void setProfileImage(byte[] profileImage) {
        if (profileImage != null) {
            System.out.println("Setting image bytes. Size: " + profileImage.length);
        } else {
            System.out.println("Setting image bytes to null.");
        }
        this.profileImage = profileImage;
    }
}
