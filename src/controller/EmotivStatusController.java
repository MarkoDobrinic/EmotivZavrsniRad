package controller;

import controller.maincontroller.ControlledScreen;
import javafx.event.ActionEvent;

/**
 * Created by RedShift on 2.9.2016..
 */
public class EmotivStatusController implements ControlledScreen{

    ScreensController myController;

    @Override
    public void setScreenParent(ScreensController screenController) {
        myController = screenController;
    }

    @Override
    public void init() {

    }

    public void onMenuReturn(ActionEvent event) {

    }

    public void onMenuReconnect(ActionEvent event) {
    }
}
