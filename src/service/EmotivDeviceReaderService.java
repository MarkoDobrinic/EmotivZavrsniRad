package service;

import thread.EmotivDeviceDataCallback;

/**
 * Created by Homie on 18.12.2016..
 */
public interface EmotivDeviceReaderService extends Runnable {

    void shutdown();

    void startCollecting(final EmotivDeviceDataCallback callback);

    void stopCollecting();

}
