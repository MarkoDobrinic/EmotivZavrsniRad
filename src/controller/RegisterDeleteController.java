package controller;

import application.EmotivMusicApp;
import controller.ScreensController;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.EmotivContext;
import model.EmotivUser;

import java.util.Objects;

/**
 * Created by RedShift on 4.9.2016..
 */
public class RegisterDeleteController implements ControlledScreen {

    private ScreensController myController;
    private EmotivUser user;

    @FXML
    private TextField tfRegister, tfDelete;

    @FXML
    private PasswordField pfRegister, pfDelete;

    @FXML
    private Button btnRegisterUser, btnDeleteUser, btnLogin;

    @FXML
    private Label lblRegister, lblDelete;

    @FXML
    private void onBtnLogin(){

//        EmotivContext.APP.primaryStage.setHeight(396);
//        EmotivContext.APP.primaryStage.setWidth(441);
        WindowHelper.centerWindow();
        myController.setScreen(EmotivMusicApp.screenLoginID);
    }

    @FXML
    private void onBtnRegisterUser(){

        String username =  tfRegister.getText();
        String password = pfRegister.getText();

        if (!Objects.equals(password, "") && !Objects.equals(username, "")) {
            EmotivContext.DAO.saveUser(username,password);
            lblRegister.setText("User created!");
        }
    }

    @FXML
    private void onBtnDeleteUser(){

        String username =  tfRegister.getText();
        String password = pfRegister.getText();

        if (!Objects.equals(password, "") && !Objects.equals(username, "")) {
            EmotivContext.DAO.deleteUser(username);
            lblDelete.setText("User deleted!");
        }
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void init() {
        user = new EmotivUser();
    }
}
