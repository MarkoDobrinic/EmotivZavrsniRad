package thread;

import model.EmotivData;

import java.util.List;

/**
 * Created by RedShift on 30.8.2016..
 */
public interface EmotivDeviceDataCallback {
    void onData(List<EmotivData> data);
}
