package controller;

import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import model.EmotivBaseline;
import model.EmotivContext;
import model.EmotivData;
import thread.DeviceReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by RedShift on 26.8.2016..
 */
public class BaselineController extends EmotivMusicApp implements ControlledScreen {

    //private MainScreenController main;
    private ScreensController myController;

    public XYChart.Series<Integer, Double> seriesAlpha = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesBetaLow = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesBetaHigh = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesGamma = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesTheta = new XYChart.Series<>();

    /**
     * Thread variables
     */
    private Thread thread = null;
    private DeviceReader deviceReader = new DeviceReader();
    public List<EmotivData> allReadings = new ArrayList<>();
    private Service<Void> dbInsertThread;
    private EmotivBaseline baseline = new EmotivBaseline();


    @FXML
    private MenuItem itemAverage, itemSaveBaseline, itemSkip;

    @FXML
    public LineChart<Integer, Double> chartBaseline;

    @FXML
    public Button btnMainRestart, btnMainStart;


    @Override
    public void init() {

    }

    @FXML
    private void onItemSkip(ActionEvent event){

        baseline.setUserId(EmotivContext.LOGGED_USER.getId());
        baseline = EmotivContext.DAO.saveBasline(baseline);
        EmotivContext.DAO.saveBaselineReading(allReadings, baseline);
        EmotivContext.BASELINE = baseline;
        goToMainScreen();
    }

    @FXML
    private void onItemAverage(ActionEvent event){

        seriesAlpha.getData().clear();
        seriesBetaLow.getData().clear();
        seriesBetaHigh.getData().clear();
        seriesGamma.getData().clear();
        seriesTheta.getData().clear();


        System.out.println("Size: " + allReadings.size());

        Double avgAlpha = allReadings.stream().mapToDouble(EmotivData::getAlpha).summaryStatistics().getAverage();
        Double avgBetaLow = allReadings.stream().mapToDouble(EmotivData::getBetaLow).summaryStatistics().getAverage();
        Double avgBetaHigh = allReadings.stream().mapToDouble(EmotivData::getBetaHigh).summaryStatistics().getAverage();
        Double avgGamma = allReadings.stream().mapToDouble(EmotivData::getGamma).summaryStatistics().getAverage();
        Double avgTheta = allReadings.stream().mapToDouble(EmotivData::getTheta).summaryStatistics().getAverage();

        System.out.println("AvgAlpha: " + avgAlpha + ", avgBetaLow: " + avgBetaLow + ", avgBetaHigh: "
                + avgBetaHigh + ", avgGamma: " + avgGamma + ", avgTheta: " + avgTheta);

        for (int time = 0; time < 120; time++) {
            seriesAlpha.getData().add(new XYChart.Data<>(time, avgAlpha));
            seriesBetaLow.getData().add(new XYChart.Data<>(time, 2 + avgBetaLow));
            seriesBetaHigh.getData().add(new XYChart.Data<>(time, 4 + avgBetaHigh));
            seriesTheta.getData().add(new XYChart.Data<>(time, 6 + avgGamma));
            seriesGamma.getData().add(new XYChart.Data<>(time, 8 + avgTheta));
        }
    }

    @FXML
    private void onItemSaveBaseline(ActionEvent event){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(" ");
        alert.setHeaderText("Do you want to save user data?");
        alert.setContentText("Proceed");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            dbInsertThread = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {

                            baseline.setUserId(EmotivContext.LOGGED_USER.getId());
                            baseline = EmotivContext.DAO.saveBasline(baseline);
                            EmotivContext.DAO.saveBaselineReading(allReadings, baseline);
                            EmotivContext.BASELINE = baseline;
                            // myController.unloadScreen(EmotivMusicApp.screenBaselineID);

                            return null;
                        }
                    };
                }
            };
            dbInsertThread.start();
            dbInsertThread.setOnSucceeded(event1 -> {
                goToMainScreen();
            });
        } else {
            //TODO
        }
    }



    @FXML
    public void onBtnMainStart(ActionEvent event) {
        //TODO - method that fills line chart dynamically with random numbers
//        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
//        series.getData().addAll(new XYChart.Data<String, Integer>("Jan", 250));
//        series.getData().addAll(new XYChart.Data<String, Integer>("Feb", 123));
//        series.getData().addAll(new XYChart.Data<String, Integer>("Mar", 441));
        //series.getData().addAll(new XYChart.Data<Integer, Double>(120, 2.23));

        chartBaseline.setCreateSymbols(false);
        chartBaseline.setTitle("Test Baseline");
        seriesAlpha.setName("Alpha");
        chartBaseline.getData().add(seriesAlpha);

        seriesBetaHigh.setName("Beta High");
        chartBaseline.getData().add(seriesBetaHigh);

        seriesBetaLow.setName("Beta Low");
        chartBaseline.getData().add(seriesBetaLow);

        seriesGamma.setName("Gamma");
        chartBaseline.getData().add(seriesGamma);

        seriesTheta.setName("Theta");
        chartBaseline.getData().add(seriesTheta);

        //TODO - pokretanje threada za dinamične podatke

        //čistimo listu prije novog ubacivanja
        allReadings.clear();

        deviceReader.setReadLength(120);
        deviceReader.setThreadSleep(0);

        deviceReader.setCallback(data -> {
            System.out.println("data received..." + data);

            allReadings.addAll(data);

            Integer time = data.get(0).getTime();
            Double avgAlpha = data.stream().mapToDouble(EmotivData::getAlpha).summaryStatistics().getAverage();
            Double avgBetaLow = data.stream().mapToDouble(EmotivData::getBetaLow).summaryStatistics().getAverage();
            Double avgBetaHigh = data.stream().mapToDouble(EmotivData::getBetaHigh).summaryStatistics().getAverage();
            Double avgGamma = data.stream().mapToDouble(EmotivData::getGamma).summaryStatistics().getAverage();
            Double avgTheta = data.stream().mapToDouble(EmotivData::getTheta).summaryStatistics().getAverage();

            Platform.runLater(() -> {

                        seriesAlpha.getData().add(new XYChart.Data<>(time, avgAlpha));
                        seriesBetaLow.getData().add(new XYChart.Data<>(time, 2 + avgBetaLow));
                        seriesBetaHigh.getData().add(new XYChart.Data<>(time, 4 + avgBetaHigh));
                        seriesTheta.getData().add(new XYChart.Data<>(time, 6 + avgGamma));
                        seriesGamma.getData().add(new XYChart.Data<>(time, 8 + avgTheta));
                    }
            );

        });
        Thread thread = new Thread(deviceReader);
        thread.start();
    }


    @FXML
    public void onBtnMainReset(ActionEvent event) {

        this.deviceReader.terminate();

        try {
            chartBaseline.getData().clear();
        } catch (NullPointerException npe) {
            System.out.println("lineChart=" + chartBaseline);
            System.out.println("lineChart.getData()=" + chartBaseline.getData());
            throw npe;
        }
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }


    private void goToMainScreen(){
        try {
            EmotivContext.APP.primaryStage.setMinHeight(1030);
            EmotivContext.APP.primaryStage.setMinWidth(1520);
            WindowHelper.centerWindow();
            myController.setScreen(EmotivMusicApp.screenMainID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

