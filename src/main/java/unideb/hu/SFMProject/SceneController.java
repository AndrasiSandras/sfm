package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField emailText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField UserNameText;
    @FXML
    private TextField rePasswordText;



    @FXML
    void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goBackToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLClientLoginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void staffLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLStaffLoginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void clientLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLClientLoginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void logInStaff(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLStaffScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void logInClient(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/FXMLClientScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void registerNewClient(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/view/FXMLClientRegisterScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


    }

    public void registerClient(ActionEvent actionEvent) throws IOException {
        if(register())
        {
            String name = UserNameText.getText();
            String password = passwordText.getText();

            String credentials = name + "," + password;

            RegLogin Credentials = new RegLogin();
            Credentials.setCredentials(credentials);

            Utils cutil = new Utils(new JPADAO());
            cutil.runCUtils(Credentials);

            root = FXMLLoader.load(getClass().getResource("/view/FXMLClientScene.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public boolean isItEamil()
    {
        String email = emailText.getText();
        System.out.println(email);

        if(email.contains("@"))
        {
            return true;
        }
        return false;
    }

    public boolean samePassword(TextField rePasswordText)
    {


        return passwordText.getText().equals(rePasswordText.getText());
    }

    @FXML
    public boolean register()
    {

        if(!isItEamil())
        {
            emailText.getText();
            emailText.setText("Hibás formátumban van az email cím!");
            return false;
        }
        else
        {
            if(!samePassword(rePasswordText))
            {
                emailText.setText("Nem egyezik meg a két jelszó!");
                return false;
            }
            else
            {
                return true;
            }

        }

    }


}
