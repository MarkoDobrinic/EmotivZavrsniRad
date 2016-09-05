package controller;

import controller.maincontroller.ControlledScreen;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;

/**
 * Created by RedShift on 3.9.2016..
 */
public class SpinnerController implements ControlledScreen {

    private int value = 1680;

    @FXML
    public ProgressIndicator dialogSpinner;

    @Override
    public void setScreenParent(ScreensController screenPage) {

    }

    @Override
    public void init() {

    }
}
