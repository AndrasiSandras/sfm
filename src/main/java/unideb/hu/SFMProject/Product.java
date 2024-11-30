package unideb.hu.SFMProject;

import javax.persistence.*;

/**
 * Represents a product entity in the database.
 * Contains details such as name, price, description, quantity, and an optional image.
 */
@Entity
public class Product {

    /** Default value for the product quantity. */
    private static final int DEFAULT_QUANTITY = 0;

    /** Unique identifier for the product. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Name of the product. */
    private String name;

    /** Price of the product. */
    private double price;

    /** Description of the product. */
    private String description;

    /** Quantity of the product in stock. */
    private int quantity = DEFAULT_QUANTITY;

    /** Optional image of the product. Stored as a binary object (BLOB). */
    @Lob
    private byte[] image;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public byte[] getImage() {
        return image;
    }

    // =============== Setters ===============

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
