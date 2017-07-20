package model;

import application.EmotivMusicApp;
import dao.EmotivDao;
import service.EmotivDeviceReaderService;
import service.EmotivDeviceStatusService;

/**
 * Created by RedShift on 3.9.2016..
 */
public final class EmotivContext {

    public static EmotivMusicApp APP;
    public static EmotivDao DAO;
    public static EmotivUser LOGGED_USER;
    public static EmotivBaseline BASELINE;
    public static EmotivTest TEST;

    public static EmotivDeviceReaderService DEVICE_READER_SERVICE;
    public static EmotivDeviceStatusService DEVICE_STATUS_SERVICE;

    public static int TIMELINE_SCALE = 4; // 4 times in second
    public static double TIMELINE_MIN_CAP = 0;
    public static double TIMELINE_MAX_CAP = 4;



    private EmotivContext() {
    }
}
