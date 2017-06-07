package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static itg8.com.nowzonedesigndemo.db.tbl.TblState.FIELD_STATE;

/**
 * Created by itg_Android on 6/7/2017.
 */
@DatabaseTable(tableName = TblState.TABLE_STATE)
public class TblState implements Parcelable {

    public static final String TABLE_STATE = "TABLE_STATE";
    private static final String FIELD_ID="id";
    private static final String FIELD_STATE="state";
    private static final String FIELD_DATE="date";
    private static final String FIELD_COUNT="count";
    private static final String FIELD_TIMESTAMP="timestamp";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_STATE)
    private int state;

    @DatabaseField(columnName = FIELD_DATE)
    private String date;

    @DatabaseField(columnName = FIELD_COUNT)
    private int count;

    @DatabaseField(columnName = FIELD_TIMESTAMP)
    private long timestamp;



    public TblState() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
        dest.writeLong(this.id);
        dest.writeInt(this.state);
        dest.writeString(this.date);
        dest.writeInt(this.count);
        dest.writeLong(this.timestamp);
    }

    protected TblState(Parcel in) {
        this.id = in.readLong();
        this.state = in.readInt();
        this.date = in.readString();
        this.count = in.readInt();
        this.timestamp = in.readLong();
    }

    public static final Creator<TblState> CREATOR = new Creator<TblState>() {
        @Override
        public TblState createFromParcel(Parcel source) {
            return new TblState(source);
        }

        @Override
        public TblState[] newArray(int size) {
            return new TblState[size];
        }
    };
}
