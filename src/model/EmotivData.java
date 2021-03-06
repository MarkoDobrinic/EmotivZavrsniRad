package model;

/**
 * Created by MDobrinic on 26.8.2016..
 */
public class EmotivData {

    private Double alpha;
    private Double betaLow;
    private Double betaHigh;
    private Double gamma;
    private Double theta;
    private Integer time;


    private int nodeId;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Double getAlpha() {
        return getLimited(alpha);
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Double getBetaLow() {
        return getLimited(betaLow);
    }

    public void setBetaLow(Double betaLow) {
        this.betaLow = betaLow;
    }

    public Double getBetaHigh() {
        return getLimited(betaHigh);
    }

    public void setBetaHigh(Double betaHigh) {
        this.betaHigh = betaHigh;
    }

    public Double getGamma() {
        return getLimited(gamma);
    }

    public void setGamma(Double gamma) {
        this.gamma = gamma;
    }

    public Double getTheta() {
        return getLimited(theta);
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

    private Double getLimited(Double input) {
        if (input < EmotivContext.TIMELINE_MIN_CAP) {
            return EmotivContext.TIMELINE_MIN_CAP;
        }
        if (input > EmotivContext.TIMELINE_MAX_CAP) {
            return EmotivContext.TIMELINE_MAX_CAP;
        }
        return input;
    }

    @Override
    public String toString() {
        return "EmotivData{" +
                "nodeId=" + nodeId +
                ", time=" + time +
                ", alpha=" + alpha +
                ", betaLow=" + betaLow +
                ", betaHigh=" + betaHigh +
                ", gamma=" + gamma +
                ", theta=" + theta +
                '}';
    }
}


