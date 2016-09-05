package thread;

import device.DeviceService;
import device.DeviceServiceMock;

/**
 * Created by RedShift on 30.8.2016..
 */
public class DeviceReader implements Runnable {

    private DataCallback callback;
    private DeviceService deviceService = new DeviceServiceMock();
    private int readLength;
    private int threadSleep;


    //private DeviceService deviceService = new DeviceServiceImpl();
    private volatile boolean running = true;

    public void terminate() {
        this.running = false;
    }

    @Override
    public void run() {
        if (callback == null) {
            throw new IllegalArgumentException("Callback not set!");
        }
        this.running = true;
        for (int i = 0; i < readLength && this.running; i++) {
            callback.onData(deviceService.readData(i));

            try {
                Thread.sleep(threadSleep);
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

    public int getReadLength() {
        return readLength;
    }

    public void setReadLength(int readLength) {
        this.readLength = readLength;
    }

    public int getThreadSleep() {
        return threadSleep;
    }

    public void setThreadSleep(int threadSleep) {
        this.threadSleep = threadSleep;
    }

}
