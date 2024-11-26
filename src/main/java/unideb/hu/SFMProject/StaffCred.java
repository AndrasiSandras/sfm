package unideb.hu.SFMProject;

import javax.persistence.*;

@Entity

public class StaffCred {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Credentials;
    @Lob
    @Column(nullable = true)
    private byte[] pImage;

    public String getCredentials() {
        return Credentials;
    }

    public void setCredentials(String credentials) {
        Credentials = credentials;
    }

    public byte[] getpImage() {
        return pImage;
    }

    public void setpImage(byte[] pImage) {
        System.out.println("Setting image bytes. Size: " + (pImage != null ? pImage.length : "null"));
        this.pImage = pImage;
    }

    public Object getId() {
        return id;
    }
}
