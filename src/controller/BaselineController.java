package controller;

import application.EmotivMusicApp;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import com.sun.org.apache.xpath.internal.operations.Number;
import controller.maincontroller.ControlledScreen;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.EmotivData;
import thread.DataCallback;
import thread.DeviceReader;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by RedShift on 26.8.2016..
 */
public class BaselineController extends EmotivMusicApp implements ControlledScreen {

    //private MainController main;
    public static EmotivData emotivData = new EmotivData();
    private ScreensController myController;


    public static DoubleByReference alpha = new DoubleByReference(0);
    public static DoubleByReference low_beta = new DoubleByReference(0);
    public static DoubleByReference high_beta = new DoubleByReference(0);
    public static DoubleByReference gamma = new DoubleByReference(0);
    public static DoubleByReference theta = new DoubleByReference(0);

    private Service<Void> backgorundThread;

    private boolean startButton = false;
    public int timerMax = 120, timer = 0;
    private Timeline time = new Timeline();
    private double totalAlpha, totalLowBeta, totalHighBeta, totalGamma, totalTheta;

    public NumberAxis xAxisLine = new NumberAxis();
    public NumberAxis yAxisLine = new NumberAxis();
    public XYChart.Series<Integer, Double> seriesAlpha = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesBetaLow = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesBetaHigh = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesGamma = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> seriesTheta = new XYChart.Series<>();

    /**
     * Thread variables
     * */
    private Thread thread = null;
    private DeviceReader deviceReader = new DeviceReader();


    public double threshold, barChartValue, baseline = 0, divider = 1, timePlaying = 0;

    @FXML
    public Label lblTimeCounter;

    @FXML
    public LineChart<Integer, Double> chartBaseline;

    @FXML
    public Button btnMainRestart, btnMainStart;


    @FXML
    public ChoiceBox choiceBoxDifficulty;


    @FXML
    public void onBtnMainStart(ActionEvent event) {
        //TODO - method that fills line chart dynamically with random numbers
//        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
//        series.getData().addAll(new XYChart.Data<String, Integer>("Jan", 250));
//        series.getData().addAll(new XYChart.Data<String, Integer>("Feb", 123));
//        series.getData().addAll(new XYChart.Data<String, Integer>("Mar", 441));
        //series.getData().addAll(new XYChart.Data<Integer, Double>(120, 2.23));

        chartBaseline.setCreateSymbols(false);
        chartBaseline.setTitle("Derp");
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

        deviceReader.setCallback(data -> {
            System.out.println("data received..." + data);
            Platform.runLater(() -> {
                        seriesAlpha.getData().add(new XYChart.Data<>(data.getTime(), data.getAlpha()));
                        seriesBetaLow.getData().add(new XYChart.Data<>(data.getTime(), 2+data.getBeta_low()));
                        seriesBetaHigh.getData().add(new XYChart.Data<>(data.getTime(), 4+data.getBeta_high()));
                        seriesTheta.getData().add(new XYChart.Data<>(data.getTime(), 6+data.getTheta()));
                        seriesGamma.getData().add(new XYChart.Data<>(data.getTime(), 8+data.getGamma()));
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

//
//    public void init(MainController mainController) {
//        main = mainController;
//    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}

//    @FXML
//    public void initialize(){
//
//        initializeChoiceDifficulty();
//
//        btnMainStart.setOnAction(e -> {
//
//            //buttonMainFinish.setDisable(false);
//            btnMainStart.setDisable(true);
//
//            resetLineChart();
//
////            disableItems();
////            startStimulation();
//            startTimer();
//
//            startButton = true;
//            killEpocThread = false;
//            //https://www.youtube.com/watch?v=wOtGPJBUAVs
//            backgorundThread = new Service<Void>() {
//                @Override
//                protected Task<Void> createTask() {
//                    return new Task<Void>() {
//                        @Override
//                        protected Void call() throws Exception {
//
//                            //epocData(isCancelled());
//                            emotivData.testData();  //Starts a stream of data
//                            return null;
//                        }
//                    };
//                }
//            };
//
//            backgorundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    //TODO
//                }
//            });
//            //barChartMain10.barGapProperty().bind(backgorundThread.);
//            backgorundThread.restart();
//        });
//    }
//
//
//    public void initializeChoiceDifficulty(){
//        choiceBoxDifficulty.setItems(FXCollections.observableArrayList(
//                "Easy", "Medium", "Hard")
//        );
//        choiceBoxDifficulty.getSelectionModel().selectFirst();
//    }
//
//    public void setThreshold(){
//
//        if (choiceBoxDifficulty.getValue().toString().equals("Easy"))
//            threshold = baseline * 0.9;
//        else if (choiceBoxDifficulty.getValue().toString().equals("Medium"))
//            threshold = baseline * 0.8;
//        else
//            threshold = baseline * 0.7;
//    }
//
//    public void resetLineChart(){
//        series.getData().clear();
//        xSeriesData=0;
//    }
//
//    public void startTimer(){
//        timer = 0;
//        timePlaying = 0;
//
//        KeyFrame update = new KeyFrame(Duration.seconds(1), ae -> {
//            updateLabel();
//        });
//        time = new Timeline(update);
//        time.setCycleCount(Timeline.INDEFINITE);
//        time.play();
//    }
//
//    public void stopTimer(){
//        time.stop();
//    }
//    public void updateLabel(){
//        timer++;
//        lblTimeCounter.setText("" + timer + " sec");
//
//        if (timer == 120){
//            stopTimer();
//            //buttonMainFinish.fire();
//        }
//
//    }
//
//
//    static int xSeriesData = 0;
//    public void initializeBarChartConcentrationData(){
//
//        XYChart.Series series1 = new XYChart.Series();
//        final XYChart.Data<String, Number> data = new XYChart.Data(choiceBoxDifficulty.getValue().toString(), 0);
//        series1.getData().add(data);
//
//
//        //ProgressChart
//        xSeriesData = 0;
//        //defining a series
//        series = new XYChart.Series();
//        chartBaseline.getData().remove(series.getData());
//        chartBaseline.getData().add(series);
//
//
//        Timeline timeline = new Timeline();
//        timeline.getKeyFrames().add(
//                new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
//                    double value = getMeasuringBarValue();
//
//                    data.setYValue(value);
//                    series1.getData().set(0, data);
//
//                    if (startButton) {
//                        series.getData().add(new XYChart.Data(++xSeriesData, value));
//                    }
//                            timePlaying++;
//                }));
//
//        timeline.setCycleCount(1000);
//        timeline.setAutoReverse(true);
//        timeline.play();
//    }
//
//
//    public double getMeasuringBarValue(){
//        double theta, lowBeta, value;
//
//        getMeasuringValue();
//        value = getMeasuringValue();
//        return value;
//    }
//
//    public double getMeasuringValue(){
//        double theta, lowBeta;
//
//        theta = emotivData.randomTheta;
//        lowBeta = emotivData.randomLowBeta;
//
//        return theta/lowBeta;
//    }