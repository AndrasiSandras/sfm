package unideb.hu.SFMProject;

import javax.persistence.*;
import java.util.List;

public class JPADAO extends DAO {

    private final EntityManagerFactory entityManagerFactory;

    public JPADAO() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("unideb.hu.SFMProject");
    }

    private EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public void saveProduct(Product a) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(a);
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


    @Override
    public List<Product> getAllProduct() {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<Product> query = entityManager.createQuery("SELECT a FROM Product a", Product.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateProduct(Product a) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        Product existingProduct = entityManager.find(Product.class, a.getId());
        if (existingProduct != null) {
            existingProduct.setName(a.getName());
            existingProduct.setDescription(a.getDescription());
            existingProduct.setPrice(a.getPrice());
            existingProduct.setQuantity(a.getQuantity()); // Új mező kezelése
            entityManager.merge(existingProduct);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteProduct(Product a) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Product product = entityManager.merge(a); // szükséges lehet a merge, mielőtt törölsz
            entityManager.remove(product);
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

    @Override
    public void saveRegLog(RegLogin a) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(a);
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

    @Override
    public List<String> getAllRegLog() {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<String> query = entityManager.createQuery("SELECT a.Credentials FROM RegLogin a", String.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<String> getAllStaffCred() {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<String> query = entityManager.createQuery("SELECT a.Credentials FROM StaffCred a", String.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void saveReport(Report a) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(a);
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

    @Override
    public List<Report> getAllReports() {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<Report> query = entityManager.createQuery("SELECT a FROM Report a", Report.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Report> getAllReportsbyName(String a) {
        EntityManager entityManager = createEntityManager();
        List<Report> reports = null;
        try {
            reports = entityManager.createQuery("SELECT r FROM Report r WHERE r.starterName = :starterName", Report.class)
                    .setParameter("starterName", a)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();

        }

        return reports;
    }

    @Override
    public void updateStafCredpImage(StaffCred a) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        StaffCred existingProduct = entityManager.find(StaffCred.class, a.getId());
        if (existingProduct != null) {
            existingProduct.setCredentials(a.getCredentials());
            existingProduct.setpImage(a.getpImage()); // Új mező kezelése
            entityManager.merge(existingProduct);
            entityManager.flush();
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateRegLogpImage(RegLogin a) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        RegLogin existingProduct = entityManager.find(RegLogin.class, a.getId());
        if (existingProduct != null) {
            existingProduct.setCredentials(a.getCredentials());
            existingProduct.setpImage(a.getpImage()); // Új mező kezelése
            entityManager.merge(existingProduct);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public StaffCred findStaffcredbyCredentials(String cred) {
        EntityManager entityManager = createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM StaffCred p WHERE p.Credentials = :Cred", StaffCred.class)
                    .setParameter("Cred", cred)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public RegLogin findRegLogbyCredentials(String cred) {
        EntityManager entityManager = createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM RegLogin p WHERE p.Credentials = :Cred", RegLogin.class)
                    .setParameter("Cred", cred)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateStafCredPassword(StaffCred a) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        StaffCred existingProduct = entityManager.find(StaffCred.class, a.getId());
        if (existingProduct != null) {
            existingProduct.setCredentials(a.getCredentials());
            existingProduct.setpImage(a.getpImage());
            entityManager.merge(existingProduct);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateReglogpassword(RegLogin a) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        RegLogin existingProduct = entityManager.find(RegLogin.class, a.getId());
        if (existingProduct != null) {
            existingProduct.setCredentials(a.getCredentials());
            existingProduct.setpImage(a.getpImage());
            entityManager.merge(existingProduct);
        }
        entityManager.getTransaction().commit();
    }

    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
