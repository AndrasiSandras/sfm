package unideb.hu.SFMProject;

import java.util.List;

/**
 * Abstract class for data access operations on various entities like Product, RegLogin, Report, etc.
 * This class provides the contract for all DAO implementations.
 */
public abstract class DAO {

    /**
     * Saves a Product object to the database.
     * @param product The product to be saved.
     */
    public abstract void saveProduct(Product product); // Create

    /**
     * Retrieves all products from the database.
     * @return A list of all products.
     */
    public abstract List<Product> getAllProduct(); // Read

    /**
     * Updates an existing Product object in the database.
     * @param product The product to be updated.
     */
    public abstract void updateProduct(Product product); // Update

    /**
     * Deletes a product from the database.
     * @param product The product to be deleted.
     */
    public abstract void deleteProduct(Product product);

    /**
     * Finds a Product by its name.
     * @param name The name of the product.
     * @return The product with the specified name.
     */
    public abstract Product findProductByName(String name);

    /**
     * Saves a RegLogin object to the database.
     * @param regLogin The registration login data to be saved.
     */
    public abstract void saveRegLog(RegLogin regLogin);

    /**
     * Retrieves all RegLogin entries from the database.
     * @return A list of all registration login entries.
     */
    public abstract List<String> getAllRegLog();

    /**
     * Retrieves all staff credentials from the database.
     * @return A list of all staff credentials.
     */
    public abstract List<String> getAllStaffCred();

    /**
     * Saves a Report object to the database.
     * @param report The report to be saved.
     */
    public abstract void saveReport(Report report);

    /**
     * Retrieves all reports from the database.
     * @return A list of all reports.
     */
    public abstract List<Report> getAllReports();

    /**
     * Retrieves all reports for a specific product from the database.
     * @param productName The name of the product.
     * @return A list of reports for the specified product.
     */
    public abstract List<Report> getAllReportsbyName(String productName);

    /**
     * Updates the profile image of a staff credential.
     * @param staffCredential The staff credential to be updated.
     */
    public abstract void updateStafCredpImage(StaffCredential staffCredential);

    /**
     * Updates the profile image of a RegLogin entry.
     * @param regLogin The RegLogin entry to be updated.
     */
    public abstract void updateRegLogpImage(RegLogin regLogin);

    /**
     * Finds a StaffCredential by its credentials.
     * @param credentials The credentials to search for.
     * @return The StaffCredential with the specified credentials.
     */
    public abstract StaffCredential findStaffcredbyCredentials(String credentials);

    /**
     * Finds a RegLogin entry by its credentials.
     * @param credentials The credentials to search for.
     * @return The RegLogin entry with the specified credentials.
     */
    public abstract RegLogin findRegLogbyCredentials(String credentials);

    /**
     * Updates the password of a StaffCredential.
     * @param staffCredential The staff credential to be updated.
     */
    public abstract void updateStafCredPassword(StaffCredential staffCredential);

    /**
     * Updates the password of a RegLogin entry.
     * @param regLogin The RegLogin entry to be updated.
     */
    public abstract void updateReglogpassword(RegLogin regLogin);
}
