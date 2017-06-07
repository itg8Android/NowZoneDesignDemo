package itg8.com.nowzonedesigndemo.connection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itg_Android on 2/21/2017.
 */

public class DeviceModel implements Parcelable {
    String name;
    String address;
    int rssi;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeInt(this.rssi);
    }

    public DeviceModel() {
    }

    protected DeviceModel(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.rssi = in.readInt();
    }

    public static final Creator<DeviceModel> CREATOR = new Creator<DeviceModel>() {
        @Override
        public DeviceModel createFromParcel(Parcel source) {
            return new DeviceModel(source);
        }

        @Override
        public DeviceModel[] newArray(int size) {
            return new DeviceModel[size];
        }
    };
}
