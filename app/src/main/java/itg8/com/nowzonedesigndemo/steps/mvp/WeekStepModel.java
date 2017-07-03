package itg8.com.nowzonedesigndemo.steps.mvp;

/**
 * Created by itg_Android on 7/1/2017.
 */

public class WeekStepModel {
    String weekLabel;
    int stepCount;
    float calBurn;
    float[] stepsCount=new float[7];


    public String getWeekLabel() {
        return weekLabel;
    }

    public void setWeekLabel(String weekLabel) {
        this.weekLabel = weekLabel;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public float getCalBurn() {
        return calBurn;
    }

    public void setCalBurn(float calBurn) {
        this.calBurn = calBurn;
    }

    public float[] getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(float[] stepsCount) {
        this.stepsCount = stepsCount;
    }
}
