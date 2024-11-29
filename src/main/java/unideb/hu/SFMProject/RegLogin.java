package unideb.hu.SFMProject;

import javax.persistence.*;

@Entity
public class RegLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Credentials;
    @Lob
    @Column(nullable = true)
    private byte[] pImage;

    public void setCredentials(String credentials) {
        Credentials = credentials;
    }

    public String getCredentials()
    {
        return Credentials;
    }

    public byte[] getpImage() {
        return pImage;
    }

    public void setpImage(byte[] pImage) {
        this.pImage = pImage;
    }

    public long getId(){
        return id;
    }
}