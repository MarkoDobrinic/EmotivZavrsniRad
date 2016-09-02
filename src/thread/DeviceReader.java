package thread;

import device.Device;
import device.DeviceBridgeMock;
import model.EmotivData;

import java.util.Random;

/**
 * Created by RedShift on 30.8.2016..
 */
public class DeviceReader implements Runnable{

    private DataCallback callback;
    private Device device = new DeviceBridgeMock();
    private volatile boolean running = true;

    public void terminate(){
        this.running = false;
    }

    @Override
    public void run() {
        if (callback == null) {
            throw new IllegalArgumentException("Callback not set!");
        }
        this.running = true;
        for (int i = 0; i < 120 && this.running;  i++) {
            callback.onData(device.readData(i));
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.running = false;
            }
        }
        //TODO
    }


    public void setCallback(DataCallback callback) {
        this.callback = callback;
    }

    public void readDeviceState(){

    }

}
