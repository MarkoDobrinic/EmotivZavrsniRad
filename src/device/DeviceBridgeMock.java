package device;

import model.EmotivData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by RedShift on 2.9.2016..
 */
public class DeviceBridgeMock implements Device{

    @Override
    public EmotivData readNodeData(int nodeId, int time) {

        Random random = new Random();

            double randomAlpha = 0.1 + (2 - 0.1) * random.nextDouble();
            double randomBetaHigh = 0.1 + (2 - 0.1) * random.nextDouble();
            double randomBetaLow = 0.1 + (2 - 0.1) * random.nextDouble();
            double randomGamma = 0.1 + (2 - 0.1) * random.nextDouble();
            double randomTheta = 0.1 + (2 - 0.1) * random.nextDouble();

            EmotivData emotivData = new EmotivData();
            emotivData.setNodeId(nodeId);
            emotivData.setTime(time);

            emotivData.setAlpha(randomAlpha);
            emotivData.setBetaHigh(randomBetaHigh);
            emotivData.setBetaLow(randomBetaLow);
            emotivData.setGamma(randomGamma);
            emotivData.setTheta(randomTheta);

        return emotivData;
    }

    @Override
    public List<EmotivData> readData(int time) {
        List<EmotivData> emotivDataList = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            emotivDataList.add(readNodeData(i, time));
        }
        return emotivDataList;
    }
}
