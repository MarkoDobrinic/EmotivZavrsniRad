package device;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import model.EmotivData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RedShift on 2.9.2016..
 */

//real implementation
public class DeviceServiceImpl implements DeviceService {

    Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
    Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();

    IntByReference userID = null;
    boolean ready = false;
    int state = 0;

    Edk.IEE_DataChannels_t dataChannel;


    @Override
    public EmotivData readNodeData(int nodeId, int time) {

        userID = new IntByReference(0);
        EmotivData emotivData = new EmotivData();

        if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
                .ToInt()) {
            System.out.println("Emotiv Engine start up failed.");
            /***
             * ovdje provjeriti što treba vraćati
             */
            return null;
        }

        System.out.println("Start receiving Data!");
        System.out.println("Theta, Alpha, Low_beta, High_beta, Gamma");

        while (true) {
            state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);

            //New event needs to be handled
            if (state == EdkErrorCode.EDK_OK.ToInt()) {
                int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
                Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);

                // Log the EmoState if it has been updated
                if (eventType == Edk.IEE_Event_t.IEE_UserAdded.ToInt())
                    if (userID != null) {
                        System.out.println("User added");
                        ready = true;
                    }
            } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                System.out.println("Internal error in Emotiv Engine!");
                break;
            }


            if (ready) {

                DoubleByReference alpha = new DoubleByReference(0);
                DoubleByReference low_beta = new DoubleByReference(0);
                DoubleByReference high_beta = new DoubleByReference(0);
                DoubleByReference gamma = new DoubleByReference(0);
                DoubleByReference theta = new DoubleByReference(0);

                int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(),
                        nodeId, theta, alpha, low_beta, high_beta, gamma);
                if (result == EdkErrorCode.EDK_OK.ToInt()) {

                    emotivData.setNodeId(nodeId);
                    emotivData.setTime(time);

                    emotivData.setAlpha(alpha.getValue());
                    emotivData.setBetaHigh(high_beta.getValue());
                    emotivData.setBetaLow(low_beta.getValue());
                    emotivData.setGamma(gamma.getValue());
                    emotivData.setTheta(theta.getValue());

                    //System.out.printf("emoData: " + emotivData.getAlpha() + ", " + emotivData.getBetaHigh());

                }
            }
        }


        return emotivData;

        // throw new NotImplementedException();
    }

    @Override
    public List<EmotivData> readData(int time) {
        List<EmotivData> emotivDataList = new ArrayList<>();

        for (int i = 3; i < 17; i++) {

            emotivDataList.add(readNodeData(i, time));
        }

        Edk.INSTANCE.IEE_EngineDisconnect();
        Edk.INSTANCE.IEE_EmoStateFree(eState);
        Edk.INSTANCE.IEE_EmoEngineEventFree(eEvent);
        System.out.println("Disconnected!");

        return emotivDataList;
    }
}
