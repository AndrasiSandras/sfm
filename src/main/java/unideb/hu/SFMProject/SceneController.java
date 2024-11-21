package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SceneController {



    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label clientLoginErrorText;
    @FXML
    private Label registerErrorText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField userNameText;
    @FXML
    private TextField rePasswordText;
    @FXML
    private TextField ClientUserNameText;
    @FXML
    private TextField ClientPasswordText;
    @FXML
    private TextField staffNameText;
    @FXML
    private TextField staffPasswordText;
    @FXML
    private Label LoginErrorText;

    private String loginName;

    public String getloginName()
    {
        return loginName;
    }




    AuthController authController = new AuthController(emailText, passwordText, rePasswordText, userNameText, registerErrorText);



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
    void logInStaff(ActionEvent event) throws IOException, InterruptedException {
        String name = staffNameText.getText();
        String password = staffPasswordText.getText();

        String[] credentials = {name,password};

        if(Slogin(credentials))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLStaffScene.fxml"));
            Parent root = loader.load();

            // Lekérjük a Staff Scene controller-ét
            StaffFormController staffController = loader.getController();

            // Átadjuk a bejelentkezett felhasználó nevét
            staffController.setLoggedInUser(loginName);

            // Scene betöltése
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }


    }

    @FXML
    void logInClient(ActionEvent event) throws IOException, InterruptedException {
        String name = ClientUserNameText.getText();
        String password = ClientPasswordText.getText();

         String[] credentials = {name,password};

        if(Clogin(credentials))
        {
            /*
            Thread.sleep(1000);
            root = FXMLLoader.load(getClass().getResource("/view/FXMLClientScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            */

            // Ha a bejelentkezés sikeres
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLClientScene.fxml"));
            Parent root = loader.load();

            // Lekérjük a Client Scene controller-ét
            ClientFormController clientController = loader.getController();

            // Átadjuk a bejelentkezett felhasználó nevét
            clientController.setLoggedInUser(loginName);

            // Scene betöltése
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
        AuthController authController = new AuthController(emailText, passwordText, rePasswordText, userNameText, registerErrorText);
        if (authController.register()) {
            String name = userNameText.getText();
            String password = passwordText.getText();
            String email = emailText.getText();

            String credentials = name + "," + password + "," + email;

            RegLogin Credentials = new RegLogin();
            Credentials.setCredentials(credentials);

            Utils cutil = new Utils(new JPADAO());
            cutil.runCUtils(Credentials);
        }
    }

    public boolean Clogin(String[] Credentials)
    {
        Utils rutils = new Utils(new JPADAO());
        List<String> Clist = rutils.runReadUtils();
        List<String> Slist = rutils.runStaffUtils();
        String[] Ccred;
        String[] Scred;
        boolean credentail = false;


        for (String e:Clist)
        {
            Ccred = e.split(",");

            if(Ccred[0].equals(Credentials[0]) && Ccred[1].equals(Credentials[1]))
            {
                credentail = true;
            }

        }

        for(String e : Slist)
        {
            Scred = e.split(",");
            if(Scred[0].equals(Credentials[0]) && Scred[1].equals(Credentials[1]))
            {
                credentail = true;
            }

        }


        if(credentail)
        {
            loginName = Credentials[0];

            return true;
        }
        else
        {
            clientLoginErrorText.setText("The Username or Password is incorrect. Try again.");
            return false;
        }

    }
    public boolean Slogin(String[] Credentials)
    {
        Utils sutils = new Utils(new JPADAO());
        List<String> Slist = sutils.runStaffUtils();
        String[] cred = new String[0];
        boolean Staffcred = false;

        for(String e : Slist)
        {
            cred = e.split(",");
            if(cred[0].equals(Credentials[0]) && cred[1].equals(Credentials[1]))
            {
                Staffcred = true;
            }

        }



        if(Staffcred)
        {
            loginName = Credentials[0];

            return true;
        }
        else
        {
            LoginErrorText.setText("The Username or Password is incorrect. Try again.");
            return false;
        }
    }
}
