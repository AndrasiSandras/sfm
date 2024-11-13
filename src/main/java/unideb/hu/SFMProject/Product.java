package unideb.hu.SFMProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int current;

    public String getName() {
        return name;
    }

    public int getCurrent() {
        return current;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

}