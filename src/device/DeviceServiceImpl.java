package device;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import Iedk.EmoState;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import controller.BaselineController;
import model.EmotivData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RedShift on 2.9.2016..
 */

//real implementation
public class DeviceServiceImpl implements DeviceService {

    private BaselineController baselineController;
    private DoubleByReference alpha = new DoubleByReference(0);
    private DoubleByReference low_beta = new DoubleByReference(0);
    private DoubleByReference high_beta = new DoubleByReference(0);
    private DoubleByReference gamma = new DoubleByReference(0);
    private DoubleByReference theta = new DoubleByReference(0);

    @Override
    public EmotivData readNodeData(int nodeId, int time) {
        EmotivData emotivData = new EmotivData();

        do {
            emotivData.setNodeId(nodeId);
            emotivData.setTime(time);

            emotivData.setAlpha(alpha.getValue());
            emotivData.setBetaLow(low_beta.getValue());
            emotivData.setBetaHigh(high_beta.getValue());
            emotivData.setGamma(gamma.getValue());
            emotivData.setTheta(theta.getValue());

        }
        while (EdkErrorCode.EDK_OK.ToInt() != Edk.INSTANCE.IEE_GetAverageBandPowers(0, nodeId, theta, alpha, low_beta, high_beta, gamma));

        return emotivData;
    }


    @Override
    public List<EmotivData> readData(int time) {
        final List<EmotivData> emotivDataList = new ArrayList<>();

        for (int i = 3; i < 17; i++) {
            emotivDataList.add(readNodeData(i, time));
        }
        return emotivDataList;

    }
}
