package unideb.hu.SFMProject;

import javax.persistence.*;
import java.util.List;

/**
 * JPADAO provides data access functionality for various entities like Product, RegLogin, Report, etc.
 * Implements the CRUD (Create, Read, Update, Delete) operations for managing database entities.
 */
public class JPADAO extends DAO {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Constructor initializes the EntityManagerFactory for the application.
     */
    public JPADAO() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("unideb.hu.SFMProject");
    }

    /**
     * Helper method to create a new EntityManager.
     * @return A new EntityManager instance.
     */
    private EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Saves a Product entity to the database.
     * @param product The product entity to be saved.
     */
    @Override
    public void saveProduct(Product product) {
        executeInTransaction(entityManager -> {
            entityManager.persist(product);
        });
    }

    /**
     * Retrieves all products from the database.
     * @return A list of all products.
     */
    @Override
    public List<Product> getAllProduct() {
        return executeQuery("SELECT a FROM Product a", Product.class);
    }

    /**
     * Updates an existing product in the database.
     * @param product The product entity to be updated.
     */
    @Override
    public void updateProduct(Product product) {
        executeInTransaction(entityManager -> {
            Product existingProduct = entityManager.find(Product.class, product.getId());
            if (existingProduct != null) {
                existingProduct.setName(product.getName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setQuantity(product.getQuantity());
                entityManager.merge(existingProduct);
            }
        });
    }

    /**
     * Finds a Product by its name.
     * @param name The name of the product.
     * @return The Product with the specified name, or null if not found.
     */
    @Override
    public Product findProductByName(String name) {
        EntityManager entityManager = createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Product p WHERE p.name = :name", Product.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Deletes a product from the database.
     * @param product The product entity to be deleted.
     */
    @Override
    public void deleteProduct(Product product) {
        executeInTransaction(entityManager -> {
            Product mergedProduct = entityManager.merge(product);
            entityManager.remove(mergedProduct);
        });
    }

    /**
     * Saves a RegLogin entity to the database.
     * @param regLogin The RegLogin entity to be saved.
     */
    @Override
    public void saveRegLog(RegLogin regLogin) {
        executeInTransaction(entityManager -> {
            entityManager.persist(regLogin);
        });
    }

    /**
     * Retrieves all RegLogin credentials from the database.
     * @return A list of all RegLogin credentials.
     */
    @Override
    public List<String> getAllRegLog() {
        return executeQuery("SELECT a.credentials FROM RegLogin a", String.class);
    }

    /**
     * Retrieves all staff credentials from the database.
     * @return A list of all staff credentials.
     */
    @Override
    public List<String> getAllStaffCred() {
        return executeQuery("SELECT a.credentials FROM StaffCredential a", String.class);
    }

    /**
     * Saves a Report entity to the database.
     * @param report The Report entity to be saved.
     */
    @Override
    public void saveReport(Report report) {
        executeInTransaction(entityManager -> {
            entityManager.persist(report);
        });
    }

    /**
     * Retrieves all reports from the database.
     * @return A list of all reports.
     */
    @Override
    public List<Report> getAllReports() {
        return executeQuery("SELECT a FROM Report a", Report.class);
    }

    /**
     * Retrieves reports filtered by a specific starter name.
     * @param starterName The starter name to filter reports.
     * @return A list of reports by starter name.
     */
    @Override
    public List<Report> getAllReportsbyName(String starterName) {
        return executeQuery("SELECT r FROM Report r WHERE r.starterName = :starterName", Report.class, starterName);
    }

    /**
     * Updates a StaffCredential's profile image in the database.
     * @param staffCredential The StaffCredential entity to be updated.
     */
    @Override
    public void updateStafCredpImage(StaffCredential staffCredential) {
        executeInTransaction(entityManager -> {
            StaffCredential existingStaffCredential = entityManager.find(StaffCredential.class, staffCredential.getId());
            if (existingStaffCredential != null) {
                existingStaffCredential.setProfileImage(staffCredential.getProfileImage());
                entityManager.merge(existingStaffCredential);
            }
        });
    }

    /**
     * Updates a RegLogin's profile image in the database.
     * @param regLogin The RegLogin entity to be updated.
     */
    @Override
    public void updateRegLogpImage(RegLogin regLogin) {
        executeInTransaction(entityManager -> {
            RegLogin existingRegLogin = entityManager.find(RegLogin.class, regLogin.getId());
            if (existingRegLogin != null) {
                existingRegLogin.setProfileImage(regLogin.getProfileImage());
                entityManager.merge(existingRegLogin);
            }
        });
    }

    /**
     * Finds a StaffCredential by its credentials.
     * @param credentials The credentials to search for.
     * @return The StaffCredential entity with the given credentials.
     */
    @Override
    public StaffCredential findStaffcredbyCredentials(String credentials) {
        return executeQuerySingleResult(
                "SELECT p FROM StaffCredential p WHERE p.credentials = :cred",
                StaffCredential.class,
                credentials);  // Paraméter átadása
    }

    /**
     * Finds a RegLogin by its credentials.
     * @param credentials The credentials to search for.
     * @return The RegLogin entity with the given credentials.
     */
    @Override
    public RegLogin findRegLogbyCredentials(String credentials) {
        return executeQuerySingleResult("SELECT p FROM RegLogin p WHERE p.credentials = :cred", RegLogin.class, credentials);
    }

    /**
     * Updates the password of a StaffCredential.
     * @param staffCredential The StaffCredential entity to be updated.
     */
    @Override
    public void updateStafCredPassword(StaffCredential staffCredential) {
        executeInTransaction(entityManager -> {
            StaffCredential existingStaffCredential = entityManager.find(StaffCredential.class, staffCredential.getId());
            if (existingStaffCredential != null) {
                existingStaffCredential.setCredentials(staffCredential.getCredentials());
                existingStaffCredential.setProfileImage(staffCredential.getProfileImage());
                entityManager.merge(existingStaffCredential);
            }
        });
    }

    /**
     * Updates the password of a RegLogin.
     * @param regLogin The RegLogin entity to be updated.
     */
    @Override
    public void updateReglogpassword(RegLogin regLogin) {
        executeInTransaction(entityManager -> {
            RegLogin existingRegLogin = entityManager.find(RegLogin.class, regLogin.getId());
            if (existingRegLogin != null) {
                existingRegLogin.setCredentials(regLogin.getCredentials());
                existingRegLogin.setProfileImage(regLogin.getProfileImage());
                entityManager.merge(existingRegLogin);
            }
        });
    }

    /**
     * Utility method to execute an update operation within a transaction.
     * @param action The action to be performed within the transaction.
     */
    private void executeInTransaction(EntityManagerAction action) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            action.execute(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Utility method to execute a query and return the result list.
     * @param query The query string.
     * @param resultClass The class of the result type.
     * @param params Optional parameters to be set in the query.
     * @param <T> The type of the result.
     * @return A list of results from the query.
     */
    private <T> List<T> executeQuery(String query, Class<T> resultClass, Object... params) {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<T> typedQuery = entityManager.createQuery(query, resultClass);
            for (int i = 0; i < params.length; i++) {
                typedQuery.setParameter(i + 1, params[i]);
            }
            return typedQuery.getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Utility method to execute a query and return a single result.
     * @param query The query string.
     * @param resultClass The class of the result type.
     * @param params Optional parameters to be set in the query.
     * @param <T> The type of the result.
     * @return A single result from the query.
     */
    private <T> T executeQuerySingleResult(String query, Class<T> resultClass, Object... params) {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<T> typedQuery = entityManager.createQuery(query, resultClass);

            for (int i = 0; i < params.length; i++) {
                typedQuery.setParameter("cred", params[i]);
            }

            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Closes the EntityManagerFactory.
     */
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    /**
     * Functional interface for actions that can be executed within a transaction.
     */
    @FunctionalInterface
    private interface EntityManagerAction {
        void execute(EntityManager entityManager);
    }
}
