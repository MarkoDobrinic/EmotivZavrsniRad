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

    public XYChart.Series<Integer, Double> alphaThetaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> alphaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> betaLowBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> betaHighBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> gammaBase = new XYChart.Series<>();
    public XYChart.Series<Integer, Double> thetaBase = new XYChart.Series<>();

    public XYChart.Series<Integer, Double> alphaThetaTest = new XYChart.Series<>();
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
    private void onBtnReload() {

        populateTestList();

    }

    @Override
    public void setScreenParent(ScreensController screenController) {
        myController = screenController;
    }

    @Override
    public void init() {

        initializeGuiElements();
        addAllChartData();
        emotivBaseline = new EmotivBaseline();
        emotivTest = EmotivContext.TEST;
        setOnListItemClickListener();
        populateTestList();
    }

    @FXML
    private void onItemMainScreen(ActionEvent event) {

        EmotivContext.APP.primaryStage.isResizable();
        EmotivContext.APP.primaryStage.setHeight(1020);
        EmotivContext.APP.primaryStage.setWidth(1512);
        WindowHelper.centerWindow();
        myController.setScreen(EmotivMusicApp.screenMainID);
    }

    private void populateTestList() {

        analyticsThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        emotivUser = EmotivContext.LOGGED_USER;
                        List<EmotivTest> tests = EmotivContext.DAO.findTestsWithAvgMeasuresByUser(emotivUser);
                        ObservableList<EmotivTest> myTests = FXCollections.observableList(tests);
                        lvTestList.setItems(myTests);
                        lvTestList.setCellFactory(new Callback<ListView<EmotivTest>, ListCell<EmotivTest>>() {
                            @Override
                            public ListCell<EmotivTest> call(ListView<EmotivTest> param) {

                                ListCell<EmotivTest> cell = new ListCell<EmotivTest>() {
                                    @Override
                                    protected void updateItem(EmotivTest item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (item != null) {
                                            Platform.runLater(() -> {
                                                setText(item.getId() + ". " + item.getArtist() + "/ " + item.getSongname());

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

    private void setOnListItemClickListener() {
        lvTestList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EmotivTest>() {
            @Override
            public void changed(ObservableValue<? extends EmotivTest> observable, EmotivTest oldValue, EmotivTest newValue) {
                System.out.println("Observable: " + observable.getValue());
                if (observable.getValue() != null) {
                    Platform.runLater(() -> {
                        txtTestInfo.setText(
                                "Song name: " + observable.getValue().getSongname() + "\n" +
                                "Artist: " + observable.getValue().getArtist() + "\n" +
                                "Measures: " + observable.getValue().getMeasures().size() + "\n" +
                                "Description: " + observable.getValue().getDescription());
                        System.out.println(observable.getValue().toString());
                        EmotivTest test1 = observable.getValue();
                        System.out.println("passing test.. " + test1.toString());
                        plotChart(test1);
                    });
                }
            }
        });
    }

    private void plotChart(EmotivTest test) {

        Integer songDuration = test.getSongDuration().intValue();

        System.out.println("Total measures: " + test.getMeasures().size());
        EmotivBaselineMeasure measure = EmotivContext.DAO.getAverageMeasure(EmotivContext.BASELINE);

        System.out.println("plotting....");
        String choice = cbTestMeasure.getValue();

        clearChartData();
        // addAllChartData();

        System.out.println("duration: " + test.getSongDuration());
        System.out.println("baseAlpha: " + measure.getAlpha() + ", testAlpha: " + EmotivContext.DAO.findAvgAlphaByTestId(test.getId()));


        switch (choice) {

            case "Base Alpha / Test Alpha":
                alphaBase.setName("Alpha Base");
                alphaTest.setName("Alpha test");

                System.out.println(test.getSongDuration().intValue());
                for (int time = 0; time < songDuration; time++) {

                    alphaBase.getData().add(new XYChart.Data<>(time, measure.getAlpha()));
                    alphaTest.getData().add(new XYChart.Data<>(time, test.getMeasures().get(time).getAlpha()));
                }
                break;
            case "Base BetaLow / Test BetaLow":

                betaLowBase.setName("BetaLow Base");
                betaLowTest.setName("BetaLow Test");

                for (int time = 0; time < songDuration; time++) {
                    betaLowBase.getData().add(new XYChart.Data<>(time, measure.getBetaLow()));
                    betaLowTest.getData().add(new XYChart.Data<>(time, test.getMeasures().get(time).getBetaLow()));
                }
                break;
            case "Base BetaHigh / Test BetaHigh":

                betaHighBase.setName("BetaHigh Base");
                betaHighTest.setName("BetaHigh Test");

                for (int time = 0; time < songDuration; time++) {
                    betaHighBase.getData().add(new XYChart.Data<>(time, measure.getBetaHigh()));
                    betaHighTest.getData().add(new XYChart.Data<>(time, test.getMeasures().get(time).getBetaHigh()));
                }
                break;
            case "Base Gamma / Test Gamma":

                gammaBase.setName("Gamma Base");
                gammaTest.setName("Gamma Test");

                for (int time = 0; time < songDuration; time++) {
                    gammaBase.getData().add(new XYChart.Data<>(time, measure.getGamma()));
                    gammaTest.getData().add(new XYChart.Data<>(time, test.getMeasures().get(time).getGamma()));
                }
                break;

            case "Base Theta / Test Theta":

                gammaBase.setName("Theta Base");
                gammaTest.setName("Theta Test");

                for (int time = 0; time < songDuration; time++) {
                    gammaBase.getData().add(new XYChart.Data<>(time, measure.getTheta()));
                    gammaTest.getData().add(new XYChart.Data<>(time, test.getMeasures().get(time).getTheta()));
                }
                break;

            case "Alpha / Theta":

                alphaThetaBase.setName("Alpha / Theta Baseline");
                alphaThetaTest.setName("Alpha / Theta Test");

                for (int time = 0; time < songDuration; time++) {
                    alphaThetaBase.getData().add(new XYChart.Data<>(time, measure.getAlpha() / measure.getTheta()));
                    alphaThetaTest.getData().add(new XYChart.Data<>(time,(test.getMeasures().get(time).getAlpha()/test.getMeasures().get(time).getTheta())));
                }
                break;
            default:
                break;
        }

    }

    private void initializeGuiElements() {

        lvTestList.refresh();

        cbTestMeasure.setItems(FXCollections.observableArrayList(
                "Baseline / Measured Average", "Base Alpha / Test Alpha", "Base BetaLow / Test BetaLow",
                "Base BetaHigh / Test BetaHigh", "Base Gamma / Test Gamma", "Base Theta / Test Theta", "Alpha / Theta"
        ));
        cbTestMeasure.setValue("Base Alpha / Test Alpha");


        cbTestMeasure.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                System.out.println("Old Selected Option: " + oldValue);
                System.out.println("New Selected Option: " + newValue);
                Platform.runLater(() -> plotChart(lvTestList.getSelectionModel().getSelectedItems().get(0)));
            }
        });


    }

    private void clearChartData() {

        alphaBase.getData().clear();
        betaLowBase.getData().clear();
        betaHighBase.getData().clear();
        gammaBase.getData().clear();
        thetaBase.getData().clear();

        alphaTest.getData().clear();
        betaLowTest.getData().clear();
        betaHighTest.getData().clear();
        gammaTest.getData().clear();
        thetaTest.getData().clear();

        //  chartTest.getData().clear();
    }

    private void addAllChartData() {

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

        alphaThetaBase.setName("Alpha Theta Baseline");
        chartTest.getData().add(alphaThetaBase);
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

        alphaThetaTest.setName("Alpha Theta test");
        chartTest.getData().add(alphaThetaTest);

        chartTest.setAnimated(false);
    }


}
