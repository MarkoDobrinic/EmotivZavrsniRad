package thread;

import model.EmotivData;

import java.util.Random;

/**
 * Created by RedShift on 30.8.2016..
 */
public class DeviceReader implements Runnable{

    private DataCallback callback;

    @Override
    public void run() {
        if (callback == null) {
            throw new IllegalArgumentException("Callback not set!");
        }
        for (int i = 0; i < 120; i++) {

            callback.onData(getRandom(i));

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //TODO
    }

    public void setCallback(DataCallback callback) {
        this.callback = callback;
    }

    public EmotivData getRandom(int time){

        Random random = new Random();
        double randomAlpha = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomBetaHigh = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomBetaLow = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomGamma = 0.1 + (2 - 0.1) * random.nextDouble();
        double randomTheta = 0.1 + (2 - 0.1) * random.nextDouble();

        EmotivData emotivData = new EmotivData();

        emotivData.setAlpha(randomAlpha);
        emotivData.setBeta_high(randomBetaHigh);
        emotivData.setBeta_low(randomBetaLow);
        emotivData.setGamma(randomGamma);
        emotivData.setTheta(randomTheta);

        emotivData.setTime(time);
        //TODO

        return emotivData;
    }
}
