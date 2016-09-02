package thread;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import controller.BaselineController;
import model.EmotivData;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by RedShift on 30.8.2016..
 */
public class DeviceReader implements Runnable{

    private DataCallback callback;
    private EmotivData emoData = new EmotivData();
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
            callback.onData(getRandom(i));
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


    public EmotivData getRandom(int time){

        Random random = new Random();
        double randomAlpha = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomBetaHigh = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomBetaLow = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomGamma = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomTheta = 0.1 + (2 - 0.1) * random.nextDouble();


        EmotivData emotivData = new EmotivData();

//        double baseBetaHigh =+ emotivData.getAlphaBase();
//        double baseBetaLow =+ emotivData.getAlphaBase();
//        double baseTheta =+ emotivData.getAlphaBase();
//        double baseGamma =+ emotivData.getAlphaBase();

        emotivData.setAlpha(randomAlpha);
        emotivData.setBeta_high(randomBetaHigh);
        emotivData.setBeta_low(randomBetaLow);
        emotivData.setGamma(randomGamma);
        emotivData.setTheta(randomTheta);

        //emotivData.alphaBase = randomAlpha;

        emotivData.setTime(time);
        //TODO

        return emotivData;
    }
}
