package unideb.hu.SFMProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String loginName;
    private String Creds;

    JPADAO jpadao = new JPADAO();

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

    public SceneController() throws IOException {
    }

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

            StaffFormController staffController = loader.getController();

            byte[] profileP = jpadao.findStaffcredbyCredentials(Creds).getpImage();
            Image pImage = null;
            if( profileP != null)
            {
                pImage = new Image(new ByteArrayInputStream(profileP));
                staffController.setLoggedInUser(loginName, Creds,pImage);
            }
            staffController.setLoggedInUser(loginName, Creds,pImage);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLClientScene.fxml"));
            Parent root = loader.load();

            ClientFormController clientController = loader.getController();

            if(jpadao.findRegLogbyCredentials(Creds) != null) {
                byte[] profileP = jpadao.findRegLogbyCredentials(Creds).getpImage();
                Image pImage = null;
                if (profileP != null) {
                    pImage = new Image(new ByteArrayInputStream(profileP));
                    clientController.setLoggedInUser(loginName, Creds, pImage);
                }
                clientController.setLoggedInUser(loginName, Creds, pImage);
            }
            else
            {
                byte[] profileP = jpadao.findStaffcredbyCredentials(Creds).getpImage();
                Image pImage = null;
                if( profileP != null)
                {
                    pImage = new Image(new ByteArrayInputStream(profileP));
                    clientController.setLoggedInUser(loginName, Creds,pImage);
                }
                clientController.setLoggedInUser(loginName, Creds,pImage);
            }
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

    public boolean Clogin(String[] Credentials) throws IOException {
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
                Creds = e;
            }
        }

        for(String e : Slist)
        {
            Scred = e.split(",");
            if(Scred[0].equals(Credentials[0]) && Scred[1].equals(Credentials[1]))
            {
                credentail = true;
                Creds = e;
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

    public boolean Slogin(String[] Credentials) throws IOException {
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
                Creds = e;
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