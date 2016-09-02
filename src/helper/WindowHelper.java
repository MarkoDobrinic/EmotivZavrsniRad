package helper;

import application.EmotivMusicApp;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Created by RedShift on 2.9.2016..
 */
public final class WindowHelper {

    private WindowHelper() {
    }

    public static void centerWindow(){
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        EmotivMusicApp.mainApp.primaryStage.setX((primScreenBounds.getWidth() - EmotivMusicApp.mainApp.primaryStage.getWidth()) / 2);
        EmotivMusicApp.mainApp.primaryStage.setY((primScreenBounds.getHeight() - EmotivMusicApp.mainApp.primaryStage.getHeight()) / 2);
    }
}
