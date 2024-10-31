package unideb.hu.SFMProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RegLogin {
    @Id
    @GeneratedValue
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
