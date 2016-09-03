package device;

import model.EmotivData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Created by RedShift on 2.9.2016..
 */

//real implementation
public class DeviceServiceImpl implements DeviceService {
    @Override
    public EmotivData readNodeData(int nodeId, int time) {
        throw new NotImplementedException();
    }

    @Override
    public List<EmotivData> readData(int time) {
        throw new NotImplementedException();
    }
}
