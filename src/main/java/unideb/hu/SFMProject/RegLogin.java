package unideb.hu.SFMProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Credentials;

    public void setCredentials(String credentials) {
        Credentials = credentials;
    }

    public String getCredentials()
    {
        return Credentials;
    }
}
