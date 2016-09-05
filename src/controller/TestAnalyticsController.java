package controller;

import application.EmotivMusicApp;
import controller.maincontroller.ControlledScreen;
import helper.WindowHelper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.*;
import thread.DeviceReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RedShift on 2.9.2016..
 */
public class TestAnalyticsController implements ControlledScreen {

    private ScreensController myController;
    private EmotivTest emotivTest;
    private EmotivBaseline emotivBaseline;
    private EmotivUser emotivUser;
    private Service<Void> analyticsThread;
    private Service<Void> deleteThread;

    public XYChart.Series<Integer, Double> alphaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> betaLowBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> betaHighBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> gammaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> thetaBase = new XYChart.Series<>();

    public XYChart.Series<Integer, Double> alphaTest = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> betaLowTest = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> betaHighTest = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> gammaTest = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> thetaTest = new XYChart.Series<>();


    @FXML
    private ListView<EmotivTest> lvTestList = new ListView<>();


    @FXML
    private MenuItem menuItemMainScreen;

    @FXML
    private TextArea txtTestInfo;

    @FXML
    private ChoiceBox<String> cbTestMeasure;

    @FXML
    private LineChart<Integer, Double> chartTest;

    @FXML
    private Button btnReload, btnDelete;

    @FXML
    private void onBtnReload(){
        populateTestList();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void init() {
        initializeGuiElements();
        emotivBaseline = new EmotivBaseline();
        emotivTest = EmotivContext.TEST;
        setOnListItemClickListener();
        populateTestList();
    }

    @FXML
    private void onItemMainScreen(ActionEvent event){

        EmotivContext.APP.primaryStage.isResizable();
        EmotivContext.APP.primaryStage.setHeight(1020);
        EmotivContext.APP.primaryStage.setWidth(1512);
        WindowHelper.centerWindow();
        myController.setScreen(EmotivMusicApp.screenMainID);
    }

    private void populateTestList(){

        analyticsThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        emotivUser = EmotivContext.LOGGED_USER;
                        List<EmotivTest> tests = EmotivContext.DAO.findTestsByUser(emotivUser);
                        ObservableList<EmotivTest> myTests = FXCollections.observableList(tests);
                        lvTestList.setItems(myTests);
                        lvTestList.setCellFactory(new Callback<ListView<EmotivTest>, ListCell<EmotivTest>>() {
                            @Override
                            public ListCell<EmotivTest> call(ListView<EmotivTest> param) {

                                ListCell<EmotivTest> cell = new ListCell<EmotivTest>(){
                                    @Override
                                    protected void updateItem(EmotivTest item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (item != null) {
                                            Platform.runLater(() -> {
                                                setText(item.getId() + ". " + item.getArtist()+ "/ " + item.getSongname());

                                            });
                                        }
                                    }
                                };
                            return cell;
                            }
                        });
                        return null;
                    }
                };
            }
        };
        analyticsThread.start();
    }

    @FXML
    public void onBtnDelete(ActionEvent event) {

        deleteThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        int id = lvTestList.getSelectionModel().getSelectedItem().getId();
                        EmotivContext.DAO.deleteTest(id);
                        return null;
                    }
                };
            }
        };
        deleteThread.start();
        populateTestList();

    }

    private void setOnListItemClickListener(){
        lvTestList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EmotivTest>() {
            @Override
            public void changed(ObservableValue<? extends EmotivTest> observable, EmotivTest oldValue, EmotivTest newValue) {
                System.out.println("Observable: " + observable.getValue());
                if(observable.getValue() != null){
                Platform.runLater(() -> {
                    txtTestInfo.setText("Song name: " + observable.getValue().getSongname() + "\n" +
                            "Artist: " + observable.getValue().getArtist()+ "Measures: " + observable.getValue().getMeasures().size());

                    observable.equals(EmotivContext.TEST);
                    //System.out.println(EmotivContext.DAO.findTestMeasuresByTestId(observable.getValue().getId()));
                    //chartTest.getData().clear();
                    plotChart(observable.getValue());

                    });
                }
            }
        });
    }

    private void plotChart(EmotivTest test) {

        EmotivBaselineMeasure measure = EmotivContext.DAO.getAverageMeasure(EmotivContext.BASELINE);

        System.out.println("plotting....");
        String choice = cbTestMeasure.getValue();

        clearChartData();
       // addAllChartData();

        System.out.println("duration: " + test.getSongDuration());
        System.out.println("baseAlpha: " + measure.getAlpha() + ", testAlpha: " + EmotivContext.DAO.findAvgAlphaByTestId(test.getId()));


        switch (choice){

            case "Base Alpha / Test Alpha" :
                clearChartData();
                alphaBase.setName("Alpha Base");
                chartTest.getData().add(alphaBase);
                alphaTest.setName("Alpha test");
                chartTest.getData().add(alphaTest);
                for (int time = 0; time < test.getSongDuration() ; time++) {
                    alphaBase.getData().add(new XYChart.Data<>(time, 2 + measure.getAlpha()));
                    alphaTest.getData().add(new XYChart.Data<>(time, 2 +EmotivContext.DAO.findAvgAlphaByTestId(test.getId())));
                }
                break;
            case "Base BetaLow / Test BetaLow" :
                clearChartData();
                betaLowBase.setName("BetaLow Base");
                chartTest.getData().add(betaLowBase);
                betaLowTest.setName("BetaLow Test");
                chartTest.getData().add(betaLowTest);
                for (int time = 0; time < test.getSongDuration() ; time++) {
                    betaLowBase.getData().add(new XYChart.Data<>(time, 2 + measure.getBetaLow()));
                    betaLowTest.getData().add(new XYChart.Data<>(time, 2 + EmotivContext.DAO.findAvgBetaLowByTestId(test.getId())));
                }
                break;
            case "Base BetaHigh / Test BetaHigh" :
                clearChartData();
                betaLowBase.setName("BetaHigh Base");
                chartTest.getData().add(betaHighBase);
                betaLowTest.setName("BetaHigh Test");
                chartTest.getData().add(betaHighTest);
                for (int time = 0; time < test.getSongDuration() ; time++) {
                    betaHighBase.getData().add(new XYChart.Data<>(time, 2 + measure.getBetaHigh()));
                    betaHighTest.getData().add(new XYChart.Data<>(time, 2 + EmotivContext.DAO.findAvgBetaHighByTestId(test.getId())));
                }
                break;
            case "Base Gamma / Test Gamma" :
                clearChartData();
                gammaBase.setName("Gamma Base");
                chartTest.getData().add(gammaBase);
                gammaTest.setName("Gamma Test");
                chartTest.getData().add(gammaTest);
                for (int time = 0; time < test.getSongDuration() ; time++) {
                    gammaBase.getData().add(new XYChart.Data<>(time, 2 + measure.getGamma()));
                    gammaTest.getData().add(new XYChart.Data<>(time, 2 + EmotivContext.DAO.findAvgGammaByTestId(test.getId())));
                }
                break;
            default:
                break;
        }

//        for (int time = 0; time < test.getSongDuration() ; time++) {
//
//            alphaBase.getData().add(new XYChart.Data<>(time, measure.getAlpha()));
//            betaLowBase.getData().add(new XYChart.Data<>(time, 2 + measure.getBetaLow()));
//            betaHighBase.getData().add(new XYChart.Data<>(time, 4 + measure.getBetaHigh()));
//            gammaBase.getData().add(new XYChart.Data<>(time, 6 + measure.getGamma()));
//            thetaBase.getData().add(new XYChart.Data<>(time, 8 + measure.getTheta()));
//
//            alphaTest.getData().add(new XYChart.Data<>(time, EmotivContext.DAO.findAvgAlphaByTestId(test.getId())));
//            betaLowTest.getData().add(new XYChart.Data<>(time, 2 + EmotivContext.DAO.findAvgAlphaByTestId(test.getId())));
//            betaHighBase.getData().add(new XYChart.Data<>(time, 4 + EmotivContext.DAO.findAvgAlphaByTestId(test.getId())));
//            gammaTest.getData().add(new XYChart.Data<>(time, 6 + EmotivContext.DAO.findAvgAlphaByTestId(test.getId())));
//            thetaTest.getData().add(new XYChart.Data<>(time, 8 + EmotivContext.DAO.findAvgAlphaByTestId(test.getId())));





                //test.getMeasures().get(time).getAlpha()
//                Double avgAlpha = test.getMeasures().get(time).getAlpha() / 14;


        //chartTest.getYAxis().setTickLabelsVisible(false);

    }

    private void initializeGuiElements(){

        lvTestList.refresh();
        cbTestMeasure.setItems(FXCollections.observableArrayList(
                "Baseline / Measured Average", "Base Alpha / Test Alpha", "Base BetaLow / Test BetaLow",
                "Base BetaHigh / Test BetaHigh" , "Base Gamma / Test Gamma" , "Base Theta / Test Theta"
        ));
        cbTestMeasure.setValue("Base Alpha / Test Alpha");


        cbTestMeasure.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> plotChart(EmotivContext.TEST));
            }
        });

    }

    private void clearChartData(){

        alphaBase.getData().clear();
        betaLowBase.getData().clear();
        betaHighBase.getData().clear();
        gammaBase.getData().clear();
        thetaBase.getData().clear();

        thetaTest.getData().clear();
        betaLowTest.getData().clear();
        betaHighTest.getData().clear();
        gammaTest.getData().clear();
        thetaTest.getData().clear();

        chartTest.getData().clear();
    }

    private void addAllChartData(){

        alphaBase.setName("Alpha Base");
        chartTest.getData().add(alphaBase);

        betaLowBase.setName("BetaLow Base");
        chartTest.getData().add(betaLowBase);

        betaHighBase.setName("BetaHigh Base");
        chartTest.getData().add(betaHighBase);

        gammaBase.setName("Gamma Base");
        chartTest.getData().add(gammaBase);

        thetaBase.setName("Theta Base");
        chartTest.getData().add(thetaBase);
    /** -----------------------------------------**/
        alphaTest.setName("Alpha test");
        chartTest.getData().add(alphaTest);

        betaLowTest.setName("BetaLow test");
        chartTest.getData().add(betaLowTest);

        betaHighTest.setName("BetaHigh test");
        chartTest.getData().add(betaHighTest);

        gammaTest.setName("Gamma test");
        chartTest.getData().add(gammaTest);

        thetaTest.setName("Theta test");
        chartTest.getData().add(thetaTest);
    }


 }
