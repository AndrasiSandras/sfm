/*
package unideb.hu.SFMProject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JPADAO extends DAO {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("unideb.hu.SFMProject");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

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
*/

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
    public void saveBeszallito(Beszallito a) {
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
    public List<Beszallito> getAllBeszallito() {
        EntityManager entityManager = createEntityManager();
        try {
            TypedQuery<Beszallito> query = entityManager.createQuery("SELECT a FROM Beszallito a", Beszallito.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateBeszallito(Beszallito a) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(a); // merge szükséges a frissítéshez
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
    public void deleteBeszallito(Beszallito a) {
        EntityManager entityManager = createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Beszallito beszallito = entityManager.merge(a); // szükséges lehet a merge, mielőtt törlöd
            entityManager.remove(beszallito);
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
    public Product findProductByName(String name) {

        EntityManager entityManager = createEntityManager();

        try {
            return entityManager.createQuery("SELECT p FROM Product p WHERE p.name = :name", Product.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Ha nincs ilyen termék, akkor null
        } finally {
            entityManager.close();
        }
    }

    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
