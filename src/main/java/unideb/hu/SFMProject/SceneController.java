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
import java.util.List;

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
    private TextField ClientUserNameText;
    @FXML
    private TextField ClientPasswordText;



    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLLoginScene.fxml");
    }

    @FXML
    void goBackToLogin(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLClientLoginScene.fxml");
    }

    @FXML
    void staffLogin(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLStaffLoginScene.fxml");
    }

    @FXML
    void clientLogin(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLClientLoginScene.fxml");
    }

    @FXML
    void logInStaff(ActionEvent event) throws IOException {
        loadScene(event, "/view/FXMLStaffScene.fxml");
    }

    @FXML
    void logInClient(ActionEvent event) throws IOException {
        String name = ClientUserNameText.getText();
        String password = ClientPasswordText.getText();
        String credentials = name + "," + password;

       if(Clogin(credentials))
       {
           root = FXMLLoader.load(getClass().getResource("/view/FXMLClientScene.fxml"));
           stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
       }
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

            root = FXMLLoader.load(getClass().getResource("/view/FXMLClientLoginScene.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public boolean isItEamil()
    {
        String email = emailText.getText();

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

    public boolean Clogin(String Credentials)
    {
        Utils rutils = new Utils(new JPADAO());
        List<String> list = rutils.runReadUtils();


        if(list.contains(Credentials))
        {
            return true;
        }
        else
        {
            ClientUserNameText.setText("Hibás felhasználónév vagy jelszó!");
            return false;
        }
    }


}
