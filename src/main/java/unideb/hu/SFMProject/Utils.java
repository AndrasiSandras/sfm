package unideb.hu.SFMProject;

import java.util.List;

/**
 * Service class responsible for handling RegLogin and StaffCredential operations
 * using the DAO layer. It acts as an intermediary between the business logic
 * and the persistence layer.
 */
public class Utils {

    // The DAO instance responsible for interacting with the database
    private DAO dao;

    /**
     * Constructor to initialize the service with the DAO implementation.
     *
     * @param dao The DAO implementation to be used by this service.
     */
    public Utils(DAO dao) {
        this.dao = dao;
    }

    /**
     * Save a RegLogin entity to the database.
     *
     * @param regLogin The RegLogin entity to be saved.
     */
    public void saveRegLogin(RegLogin regLogin) {
        dao.saveRegLog(regLogin);
    }

    /**
     * Retrieve all RegLogin credentials from the database.
     *
     * @return A list of RegLogin credentials.
     */
    public List<String> getAllRegLog() {
        return dao.getAllRegLog();
    }

    /**
     * Retrieve all staff credentials from the database.
     *
     * @return A list of staff credentials.
     */
    public List<String> getAllStaffCredentials()
    {
        return dao.getAllStaffCred();
    }
}