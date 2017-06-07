package itg8.com.nowzonedesigndemo.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itg_Android on 3/2/2017.
 */

public class DataModel implements Parcelable {
    double pressure;
    double x;
    double y;
    double z;
    int temprature;
    int battery;
    long timestamp;

    public DataModel(int pressure, int x, int y, int z, int temprature, int battery, long timestamp) {
        this.pressure = pressure;
        this.x = x;
        this.y = y;
        this.z = z;
        this.temprature = temprature;
        this.battery = battery;
        this.timestamp = timestamp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getTemprature() {
        return temprature;
    }

    public void setTemprature(int temprature) {
        this.temprature = temprature;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt((int) this.pressure);
        dest.writeDouble(this.x);
        dest.writeDouble(this.y);
        dest.writeDouble(this.z);
        dest.writeInt(this.temprature);
        dest.writeInt(this.battery);
        dest.writeLong(this.timestamp);
    }

    public DataModel() {
    }

    protected DataModel(Parcel in) {
        this.pressure = in.readInt();
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
        this.temprature = in.readInt();
        this.battery = in.readInt();
        this.timestamp = in.readLong();
    }

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel source) {
            return new DataModel(source);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };
}
