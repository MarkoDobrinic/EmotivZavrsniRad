package controller;

import application.EmotivMusicApp;
import com.sun.jna.ptr.DoubleByReference;
import controller.maincontroller.ControlledScreen;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import model.EmotivData;

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
    public XYChart.Series series = new XYChart.Series();

    public double threshold, barChartValue, baseline = 0, divider = 1, timePlaying = 0;

    @FXML
    public Label lblTimeCounter;

    @FXML
    public LineChart<String, Number> chartBaseline;

    @FXML
    public Button btnMainRestart, btnMainStart;


    @FXML
    public ChoiceBox choiceBoxDifficulty;

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

    @FXML
    public void onBtnMainStart(ActionEvent event) {
        //TODO - method that fills line chart dynamically with random numbers
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.getData().addAll(new XYChart.Data<String, Number>("Jan", 250));
        series.getData().addAll(new XYChart.Data<String, Number>("Feb", 123));
        series.getData().addAll(new XYChart.Data<String, Number>("Mar", 441));
        series.getData().addAll(new XYChart.Data<String, Number>("Apr", 665));
        chartBaseline.getData().add(series);
    }

    @FXML
    public void onBtnMainReset(ActionEvent event) {
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
