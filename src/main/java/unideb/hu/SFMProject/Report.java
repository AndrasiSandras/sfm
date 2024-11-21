package unideb.hu.SFMProject;

import javax.persistence.*;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private int transactionId;
    private String inOut;
    private String pName;
    @Column(nullable = false)
    private int pQuantity;


    public int getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(int pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
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
