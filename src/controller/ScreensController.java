package controller;

import controller.maincontroller.ControlledScreen;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by RedShift on 29.8.2016..
 */
public class ScreensController extends StackPane {

    /***
     * Main Screen controller that handles loading, setting and transitioning between screens.
     */

    public ScreensController() {
        super();
    }

    private Image imageStart, imagePause, imageStop;

    private HashMap<String, Node> screens = new HashMap<String, Node>();

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            System.out.println(loadScreen.toString());
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());

            myScreenController.setScreenParent(this);
            myScreenController.init();

            addScreen(name, loadScreen);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setScreen(final String name) {

        if (screens.get(name) != null) {
            //screen loaded
            //is there more than one screen
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
                //add new screen
                getChildren().add(0, screens.get(name));
            } else {
                //no one else been displayed, then just show
                //setOpacity(0.0);
                getChildren().add(screens.get(name));
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!\n");
            return false;
        }
    }

    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist!");
            return false;
        } else {
            return true;
        }
    }


}
