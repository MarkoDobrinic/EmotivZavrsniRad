package controller;

import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

        EmotivContext.APP.primaryStage.setHeight(396);
        EmotivContext.APP.primaryStage.setWidth(550);
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

        String username =  tfDelete.getText();
        String password = pfDelete.getText();

        if (!Objects.equals(password, "") && !Objects.equals(username, "")) {
          if(EmotivContext.DAO.deleteUser(username)){
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("User info");
              alert.setHeaderText(null);
              alert.setContentText("User successfully deleted");
              alert.showAndWait();
          }else {
              Alert alert = new Alert(Alert.AlertType.WARNING);
              alert.setTitle("Warning: ");
              alert.setHeaderText("User cannot be deleted!");
              alert.setContentText("User: " + username + " does not exist!" );
              alert.showAndWait();
          }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning: ");
            alert.setHeaderText("Please enter username and password!");

            alert.showAndWait();
        }

    }

    @Override
    public void setScreenParent(ScreensController screenController) {
        myController = screenController;
    }

    @Override
    public void init() {
        user = new EmotivUser();
    }
}
