package unideb.hu.SFMProject;

import java.util.List;

public class Utils {

    private DAO pDAO;

    public Utils(DAO pDAO) {
        this.pDAO = pDAO;
    }

    public void runCUtils(RegLogin c) {
        pDAO.saveRegLog(c);
    }

    public List<String> runReadUtils() {
        List<String> result = pDAO.getAllRegLog();
        return result;
    }
    public List<String> runStaffUtils()
    {
        List<String> result = pDAO.getAllStaffCred();
        return  result;
    }
}