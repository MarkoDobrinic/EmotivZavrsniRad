package controller;

import controller.maincontroller.ControlledScreen;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.EmotivBaselineMeasure;
import model.EmotivContext;
import model.EmotivData;
import service.MediaPlayerService;
import thread.DeviceReader;

import java.io.File;

/**
 * Created by RedShift on 28.8.2016..
 */
public class MainScreenController extends Window implements ControlledScreen {


    private ScreensController myController;
    private Group root = new Group();
    private Scene scene = new Scene(root, 500, 200);
    private MediaPlayerService mediaPlayerService;
    private DeviceReader deviceReader = new DeviceReader();
    private BaselineController baseline;

    public XYChart.Series<Integer, Double> seriesAlphaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesBetaLowBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesBetaHighBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesGammaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesThetaBase = new XYChart.Series<>();

    public XYChart.Series<Integer, Double> mainAlpha = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> mainBetaLow = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> mainBetaHigh = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> mainGamma = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> mainTheta = new XYChart.Series<>();

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private ChoiceBox<String> cbGenre;
    @FXML
    private ChoiceBox cbTest;

    @FXML
    private Button btnOpenFile;

    @FXML
    public Button btnPlayerStart, btnPlayerStop;

    @FXML
    private LineChart<Integer, Double> chartMainMusic;

    @FXML
    private void onBtnSaveTest(ActionEvent event) {

    }

    @FXML
    private void onBtnResetTest(ActionEvent event) {

        this.deviceReader.terminate();
        try {
            chartMainMusic.getData().clear();
        } catch (NullPointerException npe) {
            System.out.println("lineChart=" + chartMainMusic);
            System.out.println("lineChart.getData()=" + chartMainMusic.getData());
            throw npe;
        }
    }

    @FXML
    private void onBtnSaveSongInfo(ActionEvent event) {

    }

    @FXML
    public void onBtnPlayerStart(ActionEvent event) {
        mediaPlayerService.start();
        System.out.println(mediaPlayerService.getSongDuration());
        plotChart();
        startDrawingData();
    }

    @FXML
    public void onBtnPlayerStop() {

        mediaPlayerService.stop();
        deviceReader.terminate();
        try {
            chartMainMusic.getData().clear();
            clearAllData();
        } catch (NullPointerException npe) {
            System.out.println("lineChart=" + chartMainMusic);
            System.out.println("lineChart.getData()=" + chartMainMusic.getData());
            throw npe;
        }
    }

    @FXML
    public void onBtnOpenFile(ActionEvent event) {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP3", "*.mp3"));
            File file = fileChooser.showOpenDialog(this);
            if (file != null) {
                mediaPlayerService.setSong(file.toURI().toString());
                mediaPlayerService.preparePlayer();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        initializeGuiElements();
        mediaPlayerService = new MediaPlayerService();
    }

    private void initializeGuiElements() {

        Image imageStart = new Image(String.valueOf(getClass().getResource("../documents/resources/start.png")), true);
        btnPlayerStart.setGraphic(new ImageView(imageStart));
        Image imageStop = new Image(String.valueOf(getClass().getResource("../documents/resources/stop.png")), true);
        btnPlayerStop.setGraphic(new ImageView(imageStop));

        cbGenre.setItems(FXCollections.observableArrayList(
                "Classical", "Rap/Hip Hop", "Rock", "Pop",
                "Folk", "Vocal", "Metal", "Ambiental", "Jazz",
                "Blues", "Electronic"
        ));

        cbTest.setItems(FXCollections.observableArrayList(
                "Baseline", "Alpha/Theta", "Theta/Low Beta"
        ));
        cbTest.setValue("Baseline");

    }

    private void startDrawingData() {

        chartMainMusic.setTitle("Test");
        mainAlpha.setName("Alpha");
        chartMainMusic.getData().add(mainAlpha);

        mainBetaLow.setName("Beta High");
        chartMainMusic.getData().add(mainBetaLow);

        mainBetaHigh.setName("Beta Low");
        chartMainMusic.getData().add(mainBetaHigh);

        mainGamma.setName("Gamma");
        chartMainMusic.getData().add(mainGamma);

        mainTheta.setName("Theta");
        chartMainMusic.getData().add(mainTheta);

        //TODO - pokretanje threada za dinamične podatke

        //čistimo listu prije novog ubacivanja
        //allReadings.clear();

        deviceReader.setReadLength(mediaPlayerService.getSongDuration().intValue());
        deviceReader.setThreadSleep(1000);
        deviceReader.setCallback(data -> {

            System.out.println("data received..." + data);
            //allReadings.addAll(data);

            Integer time = data.get(0).getTime();
            Double avgAlpha = data.stream().mapToDouble(EmotivData::getAlpha).summaryStatistics().getAverage();
            Double avgBetaLow = data.stream().mapToDouble(EmotivData::getBetaLow).summaryStatistics().getAverage();
            Double avgBetaHigh = data.stream().mapToDouble(EmotivData::getBetaHigh).summaryStatistics().getAverage();
            Double avgGamma = data.stream().mapToDouble(EmotivData::getGamma).summaryStatistics().getAverage();
            Double avgTheta = data.stream().mapToDouble(EmotivData::getTheta).summaryStatistics().getAverage();

            Platform.runLater(() -> {
                        mainAlpha.getData().add(new XYChart.Data<>(time, avgAlpha));
                        mainBetaLow.getData().add(new XYChart.Data<>(time, 2 + avgBetaLow));
                        mainBetaHigh.getData().add(new XYChart.Data<>(time, 4 + avgBetaHigh));
                        mainTheta.getData().add(new XYChart.Data<>(time, 6 + avgGamma));
                        mainGamma.getData().add(new XYChart.Data<>(time, 8 + avgTheta));
                    }
            );

        });
        Thread thread = new Thread(deviceReader);
        thread.start();

    }

    private void plotChart() {

        System.out.println("plotting....");
        EmotivBaselineMeasure measure = EmotivContext.DAO.getAverageMeasure(EmotivContext.BASELINE);

        System.out.println(measure);
        seriesAlphaBase.getData().clear();
        seriesBetaLowBase.getData().clear();
        seriesBetaHighBase.getData().clear();
        seriesGammaBase.getData().clear();
        seriesThetaBase.getData().clear();

        chartMainMusic.getData().add(seriesAlphaBase);
        chartMainMusic.getData().add(seriesBetaLowBase);
        chartMainMusic.getData().add(seriesBetaHighBase);
        chartMainMusic.getData().add(seriesGammaBase);
        chartMainMusic.getData().add(seriesThetaBase);

        for (int time = 0; time < mediaPlayerService.getSongDuration(); time++) {
            seriesAlphaBase.getData().add(new XYChart.Data<>(time, measure.getAlpha()));
            seriesBetaLowBase.getData().add(new XYChart.Data<>(time, 2 + measure.getAlpha()));
            seriesBetaHighBase.getData().add(new XYChart.Data<>(time, 4 + measure.getAlpha()));
            seriesThetaBase.getData().add(new XYChart.Data<>(time, 6 + measure.getAlpha()));
            seriesGammaBase.getData().add(new XYChart.Data<>(time, 8 + measure.getAlpha()));
        }

        chartMainMusic.getYAxis().setTickLabelsVisible(false);

    }

    private void clearAllData(){

        seriesAlphaBase.getData().clear();
        seriesBetaLowBase.getData().clear();
        seriesBetaHighBase.getData().clear();
        seriesGammaBase.getData().clear();
        seriesThetaBase.getData().clear();

        mainAlpha.getData().clear();
        mainBetaLow.getData().clear();
        mainBetaHigh.getData().clear();
        mainGamma.getData().clear();
        mainTheta.getData().clear();

    }

}



