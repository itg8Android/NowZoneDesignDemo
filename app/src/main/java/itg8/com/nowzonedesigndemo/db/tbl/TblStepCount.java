package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static itg8.com.nowzonedesigndemo.db.tbl.TblStepCount.FIELD_CAL_BURN;
import static itg8.com.nowzonedesigndemo.db.tbl.TblStepCount.FIELD_STEPS;

/**
 * Created by itg_Android on 6/20/2017.
 */
@DatabaseTable(tableName = TblStepCount.TABLE_NAME)
public class TblStepCount implements Parcelable {
    public static final String TABLE_NAME = "TABLE_STEP";
    public static final String  FILED_ID="id";
    public static final String FIELD_DATE="date";
    public static final String FIELD_STEPS="steps";
    public static final String FIELD_CAL_BURN="calburn";

    @DatabaseField(columnName = FILED_ID,generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_DATE)
    private String date;

    @DatabaseField(columnName = FIELD_STEPS)
    private int steps;

    @DatabaseField(columnName = FIELD_CAL_BURN)
    private double calBurn;

    public TblStepCount() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getCalBurn() {
        return calBurn;
    }

    public void setCalBurn(double calBurn) {
        this.calBurn = calBurn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.date);
        dest.writeInt(this.steps);
        dest.writeDouble(this.calBurn);
    }

    protected TblStepCount(Parcel in) {
        this.id = in.readLong();
        this.date = in.readString();
        this.steps = in.readInt();
        this.calBurn = in.readDouble();
    }

    public static final Parcelable.Creator<TblStepCount> CREATOR = new Parcelable.Creator<TblStepCount>() {
        @Override
        public TblStepCount createFromParcel(Parcel source) {
            return new TblStepCount(source);
        }

        @Override
        public TblStepCount[] newArray(int size) {
            return new TblStepCount[size];
        }
    };
}
