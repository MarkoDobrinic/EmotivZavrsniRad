package application;


import controller.ScreensController;
import dao.EmotivDao;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.EmotivContext;
import model.EmotivTest;

import java.io.File;

public class EmotivMusicApp extends Application {

    public Stage primaryStage;
    private AnchorPane rootLayout;
    private MediaPlayer mediaPlayer;

    public static String screenLoginID = "screen2";
    public static String screenLoginFile = "../view/Login.fxml";
    public static String screenBaselineID = "screen3";
    public static String screenBaselineFile = "../view/BaselineCalibration.fxml";
    public static String screenMainID = "screenMain";
    public static String screenMainFile = "../view/MainScreen.fxml";
    public static String screenAnalyticsID = "screenAnalytics";
    public static String screenAnalyticsFile = "../view/TestAnalytics.fxml";
    public static String screenRegisterDeleteID = "registerDelete";
    public static String screenRegisterDeleteFile = "../view/RegisterDeleteScreen.fxml";
    public static String screenEmotivStatusID = "screenEmotivStatus";
    public static String screenEmotivStatusFile = "../view/EmotivStatus.fxml";
//    public static String spinnerID = "spinner";
//    public static String spinnerFile = "../view/Spinner.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.load(System.getProperty("user.dir") + File.separator + "bin" + File.separator + "win64" + File.separator + "edk.dll");
        System.load(System.getProperty("user.dir") + File.separator + "bin" + File.separator + "win64" + File.separator + "glut64.dll");

        EmotivContext.APP = this;
        EmotivContext.DAO = new EmotivDao();
        EmotivContext.TEST = new EmotivTest();
        //EmotivContext.DAO.findLastTest();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Emotiv EEG");


        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(EmotivMusicApp.screenLoginID, EmotivMusicApp.screenLoginFile);
        mainContainer.loadScreen(EmotivMusicApp.screenBaselineID, EmotivMusicApp.screenBaselineFile);
        mainContainer.loadScreen(EmotivMusicApp.screenMainID, EmotivMusicApp.screenMainFile);
        mainContainer.loadScreen(EmotivMusicApp.screenAnalyticsID, EmotivMusicApp.screenAnalyticsFile);
        mainContainer.loadScreen(EmotivMusicApp.screenRegisterDeleteID, EmotivMusicApp.screenRegisterDeleteFile);
        mainContainer.loadScreen(EmotivMusicApp.screenEmotivStatusID, EmotivMusicApp.screenEmotivStatusFile);

        mainContainer.setScreen(EmotivMusicApp.screenLoginID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        root.setAutoSizeChildren(true);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void stop() throws Exception {
        EmotivContext.DEVICE_READER_SERVICE.shutdown();
    }
}
