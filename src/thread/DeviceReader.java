package thread;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import device.DeviceService;
import device.DeviceServiceImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by RedShift on 30.8.2016..
 */
public class DeviceReader implements Runnable {

    private DataCallback callback;
    private DeviceService deviceService;

    private Pointer eEvent;
    private Pointer eState;

    private boolean running;
    private AtomicInteger timeCounter;

    public DeviceReader() {
        running = true;
        timeCounter = new AtomicInteger();
        deviceService = new DeviceServiceImpl();
    }

    @Override
    public void run() {
        if (callback == null) {
            throw new IllegalArgumentException("Callback not set!");
        }

        if (prepareDevice()) {
            System.out.println("Device ready");
            while (running) {
                if (getNextState()) {
                    // System.out.println("State acquired.");
                    callback.onData(deviceService.readData(timeCounter.getAndIncrement()));

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new IllegalStateException("Device not prepared.");
        }
    }

    private boolean getNextState() {
        IntByReference userID = new IntByReference(0);
        int state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);

        // System.out.println("Received state: " + state);
        if (state == EdkErrorCode.EDK_OK.ToInt()) {
            System.out.println("State is OK");

            int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
            Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);

            // Log the EmoState if it has been updated
            if (eventType == Edk.IEE_Event_t.IEE_UserAdded.ToInt())
                if (userID != null) {
                    System.out.println("User added " + userID.toString());
                    return true;
                }
        } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
            System.out.println("Internal error in Emotiv Engine!");
        }
        return true;
    }

    private boolean prepareDevice() {
        eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
        eState = Edk.INSTANCE.IEE_EmoStateCreate();


        if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
            System.out.println("Emotiv Engine start up failed.");
            return false;
        }

        System.out.println("Start receiving Data!");
        System.out.println("Theta, Alpha, Low_beta, High_beta, Gamma");

        return true;
    }

    public void shutdown() {
        this.running = false;
        Edk.INSTANCE.IEE_EngineDisconnect();
        Edk.INSTANCE.IEE_EmoStateFree(eState);
        Edk.INSTANCE.IEE_EmoEngineEventFree(eEvent);
    }

    public void setCallback(DataCallback callback) {
        this.callback = callback;
    }
}
