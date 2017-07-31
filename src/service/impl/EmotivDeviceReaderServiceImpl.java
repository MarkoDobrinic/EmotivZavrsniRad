package service.impl;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import device.DeviceService;
import device.DeviceServiceImpl;
import javafx.application.Platform;
import model.EmotivContext;
import model.EmotivData;
import service.EmotivDeviceReaderService;
import thread.EmotivDeviceDataCallback;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Homie on 18.12.2016..
 */
public class EmotivDeviceReaderServiceImpl implements EmotivDeviceReaderService {

    private int userId;

    private EmotivDeviceDataCallback dataCallback;
    private DeviceService deviceService;

    private Pointer eEvent;
    private Pointer eState;

    private boolean running;
    private AtomicInteger timeCounter;

    public EmotivDeviceReaderServiceImpl(){
        this.userId = EmotivContext.LOGGED_USER.getId();

        running = true;
        timeCounter = new AtomicInteger();
        deviceService = new DeviceServiceImpl();
    }

    @Override
    public void run() {
        if (prepareDevice()) {
            System.out.println("Device ready");
            while (running) {
                if (getNextState()) {
                    final List<EmotivData> emotivData = deviceService.readData(timeCounter.getAndIncrement());
                    System.out.println(emotivData);
                    if( dataCallback != null ){
                        dataCallback.onData(emotivData);
                    }

                    try {
                        Thread.sleep(1000 / EmotivContext.TIMELINE_SCALE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new IllegalStateException("Device not prepared.");
        }
    }

    @Override
    public void startCollecting(final EmotivDeviceDataCallback callback) {
        this.dataCallback = callback;
        this.timeCounter.set(0);
    }

    @Override
    public void stopCollecting() {
        this.dataCallback = null;
    }

    @Override
    public void shutdown() {
        this.running = false;
        Edk.INSTANCE.IEE_EngineDisconnect();
        Edk.INSTANCE.IEE_EmoStateFree(eState);
        Edk.INSTANCE.IEE_EmoEngineEventFree(eEvent);
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
}
