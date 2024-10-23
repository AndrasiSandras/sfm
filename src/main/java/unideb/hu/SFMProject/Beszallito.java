package unideb.hu.SFMProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Beszallito {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String email;
    private String address;
    private String password;

    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
