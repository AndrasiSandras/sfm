package unideb.hu.SFMProject;

import java.util.List;

public abstract class DAO {
    public abstract void saveProduct(Product a);       //C
    public  abstract List<Product> getAllProduct();    //R
    public abstract void updateProduct(Product a);     //U
    public abstract void deleteProduct(Product a);

    public abstract void saveBeszallito(Beszallito a);       //C
    public  abstract List<Beszallito> getAllBeszallito();    //R
    public abstract void updateBeszallito(Beszallito a);     //U
    public abstract void deleteBeszallito(Beszallito a);
}
