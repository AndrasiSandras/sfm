package unideb.hu.SFMProject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JPADAO extends DAO {

    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("unideb.hu.SFMProject");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveProduct(Product a) {
        entityManager.getTransaction().begin();
        entityManager.persist(a);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Product> getAllProduct() {
        TypedQuery<Product> query = entityManager.createQuery(
                "SELECT a FROM Product a", Product.class);
        List<Product> products = query.getResultList();
        return products;
    }

    @Override
    public void updateProduct(Product a) {
        entityManager.getTransaction().begin();
        entityManager.persist(a);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteProduct(Product a) {
        entityManager.getTransaction().begin();
        entityManager.remove(a);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void saveBeszallito(Beszallito a)
    {
        entityManager.getTransaction().begin();
        entityManager.persist(a);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Beszallito> getAllBeszallito() {
        TypedQuery<Beszallito> query = entityManager.createQuery(
                "SELECT a FROM Beszallito a", Beszallito.class);
        List<Beszallito> beszallito = query.getResultList();
        return beszallito;
    }

    @Override
    public void updateBeszallito(Beszallito a) {
        entityManager.getTransaction().begin();
        entityManager.persist(a);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteBeszallito(Beszallito a) {
        entityManager.getTransaction().begin();
        entityManager.remove(a);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void saveRegLog(RegLogin a) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(a);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllRegLog() {
        TypedQuery<String> query = entityManager.createQuery("SELECT a.Credentials FROM RegLogin a", String.class);
        List<String> result = query.getResultList();
        return result;
    }

    @Override
    public List<String> getAllStaffCred() {
        TypedQuery<String> query = entityManager.createQuery("SELECT a.Credentials FROM StaffCred a", String.class);
        List<String> result = query.getResultList();
        return result;
    }

    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}