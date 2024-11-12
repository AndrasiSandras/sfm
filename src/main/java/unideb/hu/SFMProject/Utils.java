package unideb.hu.SFMProject;

import java.util.List;

public class Utils {

    private DAO pDAO;

    public Utils(DAO pDAO) {
        this.pDAO = pDAO;
    }

    public void runPUtils(Product product) {
        pDAO.saveProduct(product);
    }

    public void runCUtils(RegLogin c) {
        pDAO.saveRegLog(c);
    }

    public List<String> runReadUtils() {
        List<String> result = pDAO.getAllRegLog();
        System.out.println(result.toString());
        return result;
    }
}