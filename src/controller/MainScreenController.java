package controller;

import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.MediaControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.applet.Main;

import java.io.File;

/**
 * Created by RedShift on 28.8.2016..
 */
public class MainScreenController implements ControlledScreen {


    private ScreensController myController;
    private Group root = new Group();
    private Scene scene = new Scene(root, 500, 200);
    private MediaControl mediaControl;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private Button btnOpenFile;

    @FXML
    private LineChart<Integer, Double> chartMainMusic;

    @FXML
    private void onBtnOpenFile(ActionEvent event) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(400);
        window.setMinHeight(400);
        MediaPlayer player = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        File file = fileChooser.showOpenDialog(window);
        String song = file.toURI().toString();

        Media pick = new Media(song);

        player = new MediaPlayer(pick);

        player.play();

    }





    @FXML
    public void initialize(){
//        Stage window = new Stage();
//        window.initModality(Modality.APPLICATION_MODAL);
//        window.setMinWidth(400);
//        window.setMinHeight(400);
//        MediaPlayer player = null;
//
//        try {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Open Resource File");
//            fileChooser.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter("MP3", "*.mp3"));
//            File file = fileChooser.showOpenDialog(window);
//
//            String song = file.toURI().toString();
//
//            Media pick = new Media(song);
//
//            player = new MediaPlayer(pick);
//
//            player.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //Add a mediaView, to display the media. Its necessary !
//        //This mediaView is added to a Pane
//        MediaView mediaView = new MediaView(player);
//        ((Group)scene.getRoot()).getChildren().add(mediaView);
//        String something = (String) player.getMedia().getMetadata().get("Album");
//        System.out.println(something);
    }

}



