package unideb.hu.SFMProject;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.tools.Console;
import org.h2.tools.Server;

public class Application {

    public static void main(String[] args) throws SQLException {
        startDatabase();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("unideb.hu.SFMProject");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        Beszallito beszallito = new Beszallito();
        Product product = new Product();

        beszallito.setName("Lajos");
        beszallito.setAddress("valami utca");
        beszallito.setEmail("valami@valami.pi");

        product.setCurrent(321);
        product.setName("Jaeger");

        entityManager.getTransaction().begin();
        entityManager.persist(beszallito);
        entityManager.persist(product);
        entityManager.getTransaction().commit();




    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }
}
