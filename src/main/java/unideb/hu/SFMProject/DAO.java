package unideb.hu.SFMProject;

import java.util.List;

public abstract class DAO {
    public abstract void saveProduct(Product a);       //C
    public  abstract List<Product> getAllProduct();    //R
    public abstract void updateProduct(Product a);     //U
    public abstract void deleteProduct(Product a);
    public abstract Product findProductByName(String name);
    public abstract void saveRegLog(RegLogin a);
    public abstract List<String> getAllRegLog();
    public abstract List<String> getAllStaffCred();
    public abstract void saveReport(Report a);
    public abstract List<Report> getAllReports();
    public abstract List<Report> getAllReportsbyName(String a);
    public abstract void updateStafCredpImage(StaffCredential a);
    public abstract void updateRegLogpImage(RegLogin a);
    public abstract StaffCredential findStaffcredbyCredentials(String cred);
    public abstract RegLogin findRegLogbyCredentials(String cred);
    public abstract void updateStafCredPassword(StaffCredential a);
    public abstract void updateReglogpassword(RegLogin a);
}