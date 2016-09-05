package controller;


import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.EmotivBaseline;
import model.EmotivContext;
import model.EmotivUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable, ControlledScreen {


    private EmotivBaseline baseline = new EmotivBaseline();
    private EmotivUser user = new EmotivUser();
    private ScreensController myController;

    @FXML
    private MenuItem itemRegisterDelete;

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
            myController.setScreen(EmotivMusicApp.screenBaselineID);

        } else {
            System.out.println("Username or password invalid!");
        }
    }

    @FXML
    private void onItemRegisterDelete(){

        EmotivContext.APP.primaryStage.setMaxHeight(680);
        EmotivContext.APP.primaryStage.setHeight(680);
        EmotivContext.APP.primaryStage.setMaxWidth(356);
        EmotivContext.APP.primaryStage.setWidth(356);
        WindowHelper.centerWindow();
        myController.setScreen(EmotivMusicApp.screenRegisterDeleteID);
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







































