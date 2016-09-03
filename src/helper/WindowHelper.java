package helper;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import model.EmotivContext;

/**
 * Created by RedShift on 2.9.2016..
 */
public final class WindowHelper {

    private WindowHelper() {
    }

    public static void centerWindow() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        EmotivContext.APP.primaryStage.setX((primScreenBounds.getWidth() - EmotivContext.APP.primaryStage.getWidth()) / 2);
        EmotivContext.APP.primaryStage.setY((primScreenBounds.getHeight() - EmotivContext.APP.primaryStage.getHeight()) / 2);
    }
}
