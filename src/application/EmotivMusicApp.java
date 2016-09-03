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

public class EmotivMusicApp extends Application {

    public Stage primaryStage;
    private AnchorPane rootLayout;
    //public MainScreenController childMainScreen;
    private MediaPlayer mediaPlayer;


    public static String screen2ID = "screen2";
    public static String screen2File = "../view/UserInfo.fxml";
    public static String screen3ID = "screen3";
    public static String screen3File = "../view/BaselineCalibration.fxml";
    public static String screenMainID = "screenMain";
    public static String screenMainFile = "../view/MainScreen.fxml";
//    public static String spinnerID = "spinner";
//    public static String spinnerFile = "../view/Spinner.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {

        EmotivContext.APP = this;
        EmotivContext.DAO = new EmotivDao();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Emotiv EEG");
//
//        initRootLayout();
//        showUserScene();

        ScreensController mainContainer = new ScreensController();
        //mainContainer.loadScreen(EmotivMusicApp.screen1ID, EmotivMusicApp.screen1File);
        mainContainer.loadScreen(EmotivMusicApp.screen2ID, EmotivMusicApp.screen2File);
        mainContainer.loadScreen(EmotivMusicApp.screen3ID, EmotivMusicApp.screen3File);
        mainContainer.loadScreen(EmotivMusicApp.screenMainID, EmotivMusicApp.screenMainFile);


        mainContainer.setScreen(EmotivMusicApp.screen2ID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        root.setAutoSizeChildren(true);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // prepareData();
    }


    public static void main(String[] args) {

        launch(args);
    }

}
