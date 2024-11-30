package unideb.hu.SFMProject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a transaction report in the system.
 * Tracks details such as transaction ID, type (in/out), starter's name, product involved, and the transaction date.
 */
@Entity
public class Report {

    /** Unique identifier for the report. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Transaction ID associated with the report. */
    private int transactionId;

    /** Indicates if the transaction is an "in" or "out" transaction. */
    private String inOut;

    /** Name of the person who initiated the transaction. */
    private String starterName;

    /** Name of the product involved in the transaction. Cannot be null. */
    @Column(nullable = false)
    private String product;

    /** The date and time of the transaction in "yyyy-MM-dd HH:mm:ss" format. */
    private String date;

    public long getId() {
        return id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getInOut() {
        return inOut;
    }

    public String getStarterName() {
        return starterName;
    }

    public String getProduct() {
        return product;
    }

    public String getDate() {
        return date;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
    }

    public void setStarterName(String starterName) {
        this.starterName = starterName;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * Sets the date and formats it to "yyyy-MM-dd HH:mm:ss".
     *
     * @param date The date of the transaction.
     */
    public void setDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = date.format(formatter);
    }
}
