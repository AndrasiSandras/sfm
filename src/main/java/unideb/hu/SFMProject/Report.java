package unideb.hu.SFMProject;

import javax.persistence.*;

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
}
