package model;

import Iedk.Edk;
import Iedk.EdkErrorCode;
import Iedk.EmoState;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import application.EmotivMusicApp;

import java.util.Random;

/**
 * Created by MDobrinic on 26.8.2016..
 */
public class EmotivData {
    public double totalAlpha = 16, totalLowBeta, totalHighBeta, totalGamma, totalTheta;
    public double randomAlpha, randomLowBeta, randomHighBeta, randomGamma, randomTheta;
    public static int signalAF3, signalAF4, signalF7, signalF3, signalF4, signalF8, signalFC5,
            signalFC6, signalT7, signalT8, signalP7, signalP8, signalO1, signalO2, signalCMS, signalDRL;
    public static int batteryStrength, wirelessStrength;

    public static DoubleByReference alpha     = new DoubleByReference(0);
    public static DoubleByReference low_beta  = new DoubleByReference(0);
    public static DoubleByReference high_beta = new DoubleByReference(0);
    public static DoubleByReference gamma     = new DoubleByReference(0);
    public static DoubleByReference theta     = new DoubleByReference(0);



//    public void testData (){
//
//        Random randomGenerator = new Random();
//
//        while (true){
//            if (EmotivMusicApp.killEpocThread) {
//                break;
//            }
//
//            totalAlpha = totalLowBeta = totalHighBeta = totalGamma = totalTheta = 0;
//            for(int j = 3 ; j < 17 ; j++)
//            {
//                randomAlpha = randomGenerator.nextInt(20)+1;
//                randomLowBeta = randomGenerator.nextInt(20)+1;
//                randomHighBeta = randomGenerator.nextInt(20)+1;
//                randomGamma = randomGenerator.nextInt(20)+1;
//                randomTheta = randomGenerator.nextInt(20)+1;
//
//                totalAlpha += randomAlpha;
//                totalLowBeta += randomLowBeta;
//                totalHighBeta += randomHighBeta;
//                totalGamma += randomGamma;
//                totalTheta += randomTheta;
//            }
//        }
//        totalAlpha /= 16;
//        totalLowBeta /= 16;
//        totalHighBeta /= 16;
//        totalGamma /= 16;
//        totalTheta /= 16;
//    }

    public static void epocHeadsetInfo() {
        Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
        Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();
        IntByReference userID = null;
        int state = 0;
        boolean onStateChanged = false;
        boolean readytocollect = false;
        IntByReference batteryLevel = new IntByReference(0);
        IntByReference maxBatteryLevel = new IntByReference(0);

        userID = new IntByReference(0);

        if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") !=
                EdkErrorCode.EDK_OK.ToInt()) {
            System.out.println("Emotiv Engine start up failed.");
            return;
        }

        System.out.println("Time, Wireless Strength, Battery Level, AF3, "
                + "T7, Pz, T8, AF4");

        while (true) {
            state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);

            // New event needs to be handled
            if (state == EdkErrorCode.EDK_OK.ToInt()) {

                int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
                Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);

                switch(eventType)
                {
                    case 0x0010:
                        System.out.println("User added");
                        readytocollect = true;
                        break;
                    case 0x0020:
                        System.out.println("User removed");
                        readytocollect = false; 		//just single connection
                        break;
                    case 0x0040:
                        onStateChanged = true;
                        Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(eEvent, eState);
                        break;
                    default:
                        break;
                }

                if (readytocollect && onStateChanged)
                {
                    float timestamp = EmoState.INSTANCE.IS_GetTimeFromStart(eState);
                    System.out.print(timestamp + ", ");

                    wirelessStrength = EmoState.INSTANCE.IS_GetWirelessSignalStatus(eState);

                    EmoState.INSTANCE.IS_GetBatteryChargeLevel(eState, batteryLevel, maxBatteryLevel);
                    batteryStrength = batteryLevel.getValue();



                    signalAF3 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_AF3.getType());
                    signalAF4 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_AF4.getType());
                    signalF7 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_F7.getType());
                    signalF3 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_F3.getType());
                    signalF4 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_F4.getType());
                    signalF8 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_F8.getType());
                    signalFC5 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_FC5.getType());
                    signalFC6 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_FC6.getType());
                    signalT7 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_T7.getType());
                    signalT8 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_T8.getType());
                    signalP7 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_P7.getType());
                    signalP8 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_P8.getType());
                    signalO1 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_O1.getType());
                    signalO2 = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_O2.getType());
                    signalCMS = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_CMS.getType());
                    signalDRL = EmoState.INSTANCE.IS_GetContactQuality(eState, EmoState.IEE_InputChannels_t.IEE_CHAN_DRL.getType());

                }
            } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                System.out.println("Internal error in Emotiv Engine!");
                break;
            }
        }

        Edk.INSTANCE.IEE_EngineDisconnect();
        System.out.println("Disconnected!");
    }

}


