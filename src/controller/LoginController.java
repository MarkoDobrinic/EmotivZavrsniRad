package controller;


import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.Constants;
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
import java.sql.*;
import java.util.ResourceBundle;


public class LoginController implements Initializable, ControlledScreen {




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
        if (ValidateUser()) {
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

            EmotivMusicApp.mainApp.primaryStage.setMinHeight(800);
            EmotivMusicApp.mainApp.primaryStage.setMinWidth(900);
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

    private boolean ValidateUser(){

        boolean enableLogin=false;
        System.out.println("SELECT * FROM users WHERE USERNAME= " + "'" + tfUsername.getText() + "'"
        + " AND PASSWORD= " + "'" + tfPassword.getText() + "'");

        Connection connection = null;
        Statement statement = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:"+Constants.Database.DB_NAME);
            connection.setAutoCommit(false);

            System.out.println("Opened DB connection...");
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Constants.Database.TABLE_NAME + " WHERE USERNAME= " + "'" + tfUsername.getText() + "'"
                    + " AND PASSWORD= " + "'" + tfPassword.getText() + "'");

            while (resultSet.next()){
                if (resultSet.getString(Constants.Database.USERNAME) != null && resultSet.getString(Constants.Database.PASSWORD) != null) {
                    String  username = resultSet.getString("USERNAME");
                    System.out.println( "USERNAME = " + username );
                    String password = resultSet.getString("PASSWORD");
                    System.out.println("PASSWORD = " + password);
                    enableLogin = true;
                }
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        return enableLogin;
    }

//
//    public void init(MainScreenController mainController) {
//        main = mainController;
//    }
}







































