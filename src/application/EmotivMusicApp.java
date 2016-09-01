package application;


import controller.ScreensController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EmotivMusicApp extends Application {

    public Stage primaryStage;
    private AnchorPane rootLayout;
    //public MainController childMainScreen;
    public static EmotivMusicApp mainApp;

    public static String screen1ID = "main";
    public static String screen1File = "../view/Main.fxml";
    public static String screen2ID = "screen2";
    public static String screen2File = "../view/UserInfo.fxml";
    public static String screen3ID = "screen3";
    public static String screen3File = "../view/BaselineCalibration.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainApp = this;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Emotiv EEG");
//
//        initRootLayout();
//        showUserScene();

        ScreensController mainContainer = new ScreensController();
        //mainContainer.loadScreen(EmotivMusicApp.screen1ID, EmotivMusicApp.screen1File);
        mainContainer.loadScreen(EmotivMusicApp.screen2ID, EmotivMusicApp.screen2File);
        mainContainer.loadScreen(EmotivMusicApp.screen3ID, EmotivMusicApp.screen3File);
        mainContainer.setScreen(EmotivMusicApp.screen2ID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        root.setAutoSizeChildren(true);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // prepareData();
    }



//    public void showCalibrationScene() {
//        try {
//            Scene scene2 = new FXMLLoader().load(getClass().getResource("../view/BaselineCalibration.fxml"));
//            primaryStage.setScene(scene2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showUserScene(){
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("../view/UserInfo.fxml"));
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void initRootLayout() {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("../view/UserInfo.fxml"));
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    private Parent replaceSceneContent(String fxml) throws Exception {
//        Parent page = (Parent) FXMLLoader.load(App.class.getResource(fxml), null, new JavaFXBuilderFactory());
//        Scene scene = stage.getScene();
//        if (scene == null) {
//            scene = new Scene(page, 700, 450);
//            scene.getStylesheets().add(App.class.getResource("demo.css").toExternalForm());
//            stage.setScene(scene);
//        } else {
//            stage.getScene().setRoot(page);
//        }
//        stage.sizeToScene();
//        return page;
//    }

    public static void main(String[] args) {

        launch(args);
    }

}
