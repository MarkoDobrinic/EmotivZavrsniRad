package model;

import application.EmotivMusicApp;
import dao.EmotivDao;

/**
 * Created by RedShift on 3.9.2016..
 */
public final class EmotivContext {

    public static EmotivMusicApp APP;
    public static EmotivDao DAO;
    public static EmotivUser LOGGED_USER;
    public static EmotivBaseline BASELINE;


    private EmotivContext() {
    }


}
