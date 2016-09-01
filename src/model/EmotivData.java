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

    private Double alpha;
    private Double beta_low;
    private Double beta_high;
    private Double gamma;
    private Double theta;
    private Integer time;

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Double getBeta_low() {
        return beta_low;
    }

    public void setBeta_low(Double beta_low) {
        this.beta_low = beta_low;
    }

    public Double getBeta_high() {
        return beta_high;
    }

    public void setBeta_high(Double beta_high) {
        this.beta_high = beta_high;
    }

    public Double getGamma() {
        return gamma;
    }

    public void setGamma(Double gamma) {
        this.gamma = gamma;
    }

    public Double getTheta() {
        return theta;
    }

    public void setTheta(Double theta) {
        this.theta = theta;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "EmotivData{" +
                "alpha=" + alpha +
                ", time=" + time +
                '}';
    }
}


