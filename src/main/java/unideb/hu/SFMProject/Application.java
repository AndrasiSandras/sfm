package unideb.hu.SFMProject;

import java.io.IOException;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Console;
import org.h2.tools.Server;

import static javafx.application.Application.launch;

public class Application extends javafx.application.Application{

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("FXMLLoginScene"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }




    public static void main(String[] args) throws SQLException {



        startDatabase();
        launch();

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
