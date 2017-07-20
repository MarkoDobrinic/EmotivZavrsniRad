package controller;

import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.*;
import service.MediaPlayerService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by RedShift on 28.8.2016..
 */
public class MainScreenController extends Window implements ControlledScreen {


    private ScreensController myController;
    private Group root = new Group();
    private Scene scene = new Scene(root, 500, 200);
    private MediaPlayerService mediaPlayerService;
    private BaselineController baseline;
    private EmotivBaseline emotivBaseline;
    private EmotivData emotivData;
    private EmotivTest emotivTest;
    private EmotivTestMeasure emotivTestMeasure;
    private Service<Void> dbTestInsertData;
    private List<EmotivData> mainReadings = new ArrayList<>();

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
    private boolean fileChosen = false;


    @Override
    public void setScreenParent(ScreensController screenController) {
        myController = screenController;
    }


    @FXML
    private Menu menuMain;

    @FXML
    private MenuItem menuShowAnalytics, menuShowEmotivStatus;

    @FXML
    private TextField tfArtist;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ChoiceBox<String> cbGenre;

    @FXML
    private Button btnOpenFile, btnSaveTest, btnResetTest;

    @FXML
    public Button btnPlayerStart, btnPlayerStop;

    @FXML
    private LineChart<Integer, Double> chartMainMusic;

    @Override
    public void init() {
        initializeGuiElements();
        mediaPlayerService = new MediaPlayerService();
        emotivTest = new EmotivTest();
        emotivTestMeasure = new EmotivTestMeasure();
        emotivData = new EmotivData();
        emotivBaseline = new EmotivBaseline();
    }

    @FXML
    private void onMenuShowEmotivStatus(ActionEvent event) {

        EmotivContext.APP.primaryStage.setHeight(735);
        EmotivContext.APP.primaryStage.setWidth(1461);
        WindowHelper.centerWindow();
        myController.setScreen(EmotivMusicApp.screenEmotivStatusID);
    }

    @FXML
    private void onMenuShowAnalytics(ActionEvent event) {

        EmotivContext.APP.primaryStage.setHeight(735);
        EmotivContext.APP.primaryStage.setWidth(1461);
        EmotivContext.APP.primaryStage.setMaxHeight(735);
        EmotivContext.APP.primaryStage.setMaxWidth(1461);
        WindowHelper.centerWindow();
        myController.setScreen(EmotivMusicApp.screenAnalyticsID);
    }

    @FXML
    private void onBtnSaveTest(ActionEvent event) {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(" ");
        alert.setHeaderText("Do you want to save this test data?");
        alert.setContentText("Proceed");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            dbTestInsertData = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            emotivTest = new EmotivTest();

                            emotivTest.setBaselineId(EmotivContext.BASELINE.getId());
                            emotivTest.setGenre(cbGenre.getValue());
                            emotivTest.setArtist(tfArtist.getText());
                            emotivTest.setDescription(txtDescription.getText());
                            emotivTest.setSongname(mediaPlayerService.getSongName());
                            emotivTest.setSongDuration(mediaPlayerService.getSongDuration());
                            emotivTest = EmotivContext.DAO.saveTest(emotivTest);
                            EmotivContext.TEST = emotivTest;
                            System.out.println("test: " + EmotivContext.TEST.getId());

                            emotivTestMeasure.setBaselineId(EmotivContext.BASELINE.getId());
                            emotivTestMeasure.setTestId(EmotivContext.TEST.getId());
                            EmotivContext.DAO.saveTestReading(mainReadings, EmotivContext.TEST, EmotivContext.BASELINE);

                            // todo - još treba vidjet jel ovo potrebno spremiti

                            return null;
                        }
                    };
                }
            };
            dbTestInsertData.start();
            System.out.println("Successfully inserted data.");
        } else {
            //TODO
        }
    }

    @FXML
    private void onBtnResetTest(ActionEvent event) {

        EmotivContext.DEVICE_READER_SERVICE.stopCollecting();
        try {
            chartMainMusic.getData().clear();
        } catch (NullPointerException npe) {
            System.out.println("lineChart=" + chartMainMusic);
            System.out.println("lineChart.getData()=" + chartMainMusic.getData());
            throw npe;
        }

        tfArtist.clear();
        txtDescription.clear();
        cbGenre.getSelectionModel().clearSelection();
    }


    @FXML
    public void onBtnPlayerStart(ActionEvent event) {


        mediaPlayerService.start();
        ((ValueAxis) chartMainMusic.getXAxis()).setUpperBound(mediaPlayerService.getSongDuration() * EmotivContext.TIMELINE_SCALE);
        System.out.println(mediaPlayerService.getSongDuration());
        mainReadings.clear();
        plotChart();
        startDrawingData();
        /***
         * dodano novo: provjeriti
         */

    }

    @FXML
    public void onBtnPlayerStop() {

        mediaPlayerService.stop();
        EmotivContext.DEVICE_READER_SERVICE.stopCollecting();
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


    private void initializeGuiElements() {

        Image imageStart = new Image(String.valueOf(getClass().getResource("../documents/resources/start.png")), true);
        btnPlayerStart.setGraphic(new ImageView(imageStart));

        Image imageStop = new Image(String.valueOf(getClass().getResource("../documents/resources/stop.png")), true);
        btnPlayerStop.setGraphic(new ImageView(imageStop));

        cbGenre.setItems(FXCollections.observableArrayList(
                "Classical", "Rap/Hip Hop", "Rock", "Pop",
                "Folk", "Vocal", "Metal", "Ambiental", "Jazz",
                "Blues", "Electronic", "Other"
        ));

//        cbTest.setItems(FXCollections.observableArrayList(
//                "Baseline", "Alpha/Theta", "Theta/Low Beta"
//        ));
//        cbTest.setValue("Baseline");

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

        //čistimo listu prije novog ubacivanja
        //allReadings.clear();

//        deviceReader.setReadLength(mediaPlayerService.getSongDuration().intValue());
//        deviceReader.setThreadSleep(500);

        EmotivContext.DEVICE_READER_SERVICE.startCollecting(data -> {

            System.out.println("drawing data" + data);
            mainReadings.addAll(data);

            Integer time = data.get(0).getTime();
            Double avgAlpha = data.stream().mapToDouble(EmotivData::getAlpha).summaryStatistics().getAverage();
            Double avgBetaLow = data.stream().mapToDouble(EmotivData::getBetaLow).summaryStatistics().getAverage();
            Double avgBetaHigh = data.stream().mapToDouble(EmotivData::getBetaHigh).summaryStatistics().getAverage();
            Double avgGamma = data.stream().mapToDouble(EmotivData::getGamma).summaryStatistics().getAverage();
            Double avgTheta = data.stream().mapToDouble(EmotivData::getTheta).summaryStatistics().getAverage();

            Platform.runLater(() -> {
                        mainAlpha.getData().add(new XYChart.Data<>(time, avgAlpha));
                mainBetaLow.getData().add(new XYChart.Data<>(time, 1 * EmotivContext.TIMELINE_MAX_CAP + avgBetaLow));
                mainBetaHigh.getData().add(new XYChart.Data<>(time, 2 * EmotivContext.TIMELINE_MAX_CAP + avgBetaHigh));
                mainTheta.getData().add(new XYChart.Data<>(time, 3 * EmotivContext.TIMELINE_MAX_CAP + avgGamma));
                mainGamma.getData().add(new XYChart.Data<>(time, 4 * EmotivContext.TIMELINE_MAX_CAP + avgTheta));
                    }
            );

        });

    }

    private void plotChart() {

        EmotivBaselineMeasure measure = EmotivContext.DAO.getAverageMeasure(EmotivContext.BASELINE);
        System.out.println("plotting...." + measure.toString());

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
            seriesBetaLowBase.getData().add(new XYChart.Data<>(time, 1 * EmotivContext.TIMELINE_MAX_CAP + measure.getBetaLow()));
            seriesBetaHighBase.getData().add(new XYChart.Data<>(time, 2 * EmotivContext.TIMELINE_MAX_CAP + measure.getBetaHigh()));
            seriesGammaBase.getData().add(new XYChart.Data<>(time, 3 * EmotivContext.TIMELINE_MAX_CAP + measure.getGamma()));
            seriesThetaBase.getData().add(new XYChart.Data<>(time, 4 * EmotivContext.TIMELINE_MAX_CAP + measure.getTheta()));
        }

        chartMainMusic.getYAxis().setTickLabelsVisible(false);
    }

    private void clearAllData() {

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

    public void onMenuMain(ActionEvent event) {
    }


}



