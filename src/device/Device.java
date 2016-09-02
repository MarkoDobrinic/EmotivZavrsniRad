package device;

import model.EmotivData;

import java.util.List;

/**
 * Created by RedShift on 2.9.2016..
 */
public interface Device {

    EmotivData readNodeData(int nodeId, int time);

    List<EmotivData> readData(int time);
}
