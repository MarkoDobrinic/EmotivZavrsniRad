package controller.maincontroller;

import controller.ScreensController;

/**
 * Created by RedShift on 29.8.2016..
 */
public interface ControlledScreen {

    /***
     * Interface used for communication between screens - setting the screen parent.
     * @param screenController
     */

    public void setScreenParent(ScreensController screenController);

    public void init();
}
