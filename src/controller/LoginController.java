package controller;


import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable, ControlledScreen {

    private EmotivMusicApp mainApp;
    private ScreensController myController;

    @FXML
    private Label lblMessage;

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private void btnLoginAction(ActionEvent actionEvent) {
        if (tfUsername.getText().equals("") && tfPassword.getText().equals("")) {
            //lblMessage.setText("login successful!");
//            try {
//                Parent parent = FXMLLoader.load(getClass().getResource("../view/BaselineCalibration.fxml"));
//                Stage stage = new Stage();
//                Scene calibrationScene = new Scene(parent);
//                stage.setScene(calibrationScene);
//                stage.show();
//                //hide this current window
//                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            myController.setScreen(EmotivMusicApp.screen3ID);
        } else {
            lblMessage.setText("Username or password invalid!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }


//
//    public void init(MainController mainController) {
//        main = mainController;
//    }
}
