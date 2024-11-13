package unideb.hu.SFMProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class StaffCred {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Credentials;

    public String getCredentials() {
        return Credentials;
    }

    public void setCredentials(String credentials) {
        Credentials = credentials;
    }
}
