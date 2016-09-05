package model;

/**
 * Created by RedShift on 3.9.2016..
 */
public class EmotivTestMeasure {

    private int id;
    private int baselineId;
    private int nodeId;
    private int testId;

    private Double alpha;
    private Double betaLow;
    private Double betaHigh;
    private Double gamma;
    private Double theta;


    public int getId() {
        return id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(int baselineId) {
        this.baselineId = baselineId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Double getBetaLow() {
        return betaLow;
    }

    public void setBetaLow(Double betaLow) {
        this.betaLow = betaLow;
    }

    public Double getBetaHigh() {
        return betaHigh;
    }

    public void setBetaHigh(Double betaHigh) {
        this.betaHigh = betaHigh;
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

    @Override
    public String toString() {
        return "EmotivTestMeasure{" +
                "id=" + id +
                ", baselineId=" + baselineId +
                ", nodeId=" + nodeId +
                ", testId=" + testId +
                ", alpha=" + alpha +
                ", betaLow=" + betaLow +
                ", betaHigh=" + betaHigh +
                ", gamma=" + gamma +
                ", theta=" + theta +
                '}';
    }
}
