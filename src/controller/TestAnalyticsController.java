package controller;

import controller.maincontroller.ControlledScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import model.EmotivBaseline;
import model.EmotivContext;
import model.EmotivTest;
import model.EmotivUser;

import java.util.List;
import java.util.Observable;

/**
 * Created by RedShift on 2.9.2016..
 */
public class TestAnalyticsController implements ControlledScreen {

    private ScreensController myController;
    private EmotivTest emotivTest;
    private EmotivBaseline emotivBaseline;
    private EmotivUser emotivUser;

    @FXML
    private ListView<EmotivTest> lvTestList = new ListView<>();

    @FXML
    private TextArea txtTestInfo;

    @FXML
    private LineChart<Integer, Double> chartTest;
    private Service<Void> analyticsThread;

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
        emotivBaseline = new EmotivBaseline();
        emotivTest = new EmotivTest();
        populateTestList();
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
                                            setText(item.getArtist()+ ": " + item.getSongname());
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
    }
}
