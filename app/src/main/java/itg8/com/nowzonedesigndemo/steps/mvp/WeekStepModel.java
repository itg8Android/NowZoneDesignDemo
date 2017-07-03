package itg8.com.nowzonedesigndemo.steps.mvp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itg_Android on 7/1/2017.
 */

public class WeekStepModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weekLabel);
        dest.writeInt(this.stepCount);
        dest.writeFloat(this.calBurn);
        dest.writeFloatArray(this.stepsCount);
    }

    public WeekStepModel() {
    }

    protected WeekStepModel(Parcel in) {
        this.weekLabel = in.readString();
        this.stepCount = in.readInt();
        this.calBurn = in.readFloat();
        this.stepsCount = in.createFloatArray();
    }

    public static final Parcelable.Creator<WeekStepModel> CREATOR = new Parcelable.Creator<WeekStepModel>() {
        @Override
        public WeekStepModel createFromParcel(Parcel source) {
            return new WeekStepModel(source);
        }

        @Override
        public WeekStepModel[] newArray(int size) {
            return new WeekStepModel[size];
        }
    };
}
