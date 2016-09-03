package controller;


import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.EmotivBaseline;
import model.EmotivContext;
import model.EmotivUser;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable, ControlledScreen {


    private EmotivBaseline baseline = new EmotivBaseline();
    private EmotivUser user = new EmotivUser();
    private ScreensController myController;

    @FXML
    private Label lblMessage;

    @FXML
    public TextField tfUsername;

    @FXML
    public PasswordField tfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private void btnLoginAction(ActionEvent actionEvent) {

        user.setUsername(tfUsername.getText());
        user.setPassword(tfPassword.getText());

        if (validateUser(user)) {

            EmotivContext.LOGGED_USER = user;

            EmotivContext.APP.primaryStage.setMinHeight(800);
            EmotivContext.APP.primaryStage.setMinWidth(900);
            WindowHelper.centerWindow();
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

    @Override
    public void init() {

    }

    private boolean validateUser(EmotivUser user) {
        EmotivUser userByUsername = EmotivContext.DAO.findUserByUsername(user.getUsername());
        if (userByUsername != null) {
            user.setId(userByUsername.getId());
        }
        return userByUsername != null;
    }

//
//    public void init(MainScreenController mainController) {
//        main = mainController;
//    }
}







































