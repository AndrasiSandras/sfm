package unideb.hu.SFMProject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private int transactionId;
    private String inOut;
    private String starterName;
    @Column(nullable = false)
    private String product;
    private String date;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getpName() {
        return starterName;
    }

    public void setpName(String pName) {
        this.starterName = pName;
    }

    public String getInOut() {
        return inOut;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

   public String getDate()
   {
       return date;
   }

    public LocalDateTime setDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date =  date.format(formatter);
        return date;
    }
}
