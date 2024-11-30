package unideb.hu.SFMProject;

import javax.persistence.*;

/**
 * Represents a user registration entry in the database.
 * Stores credentials and an optional profile image.
 */
@Entity
public class RegLogin {

    /** Unique identifier for the registration entry. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User credentials in a single string format. */
    private String credentials;

    /** Binary data representing the user's profile image. */
    @Lob
    @Column(nullable = true)
    private byte[] profileImage;

    public Long getId() {
        return id;
    }

    public String getCredentials() {
        return credentials;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
}
