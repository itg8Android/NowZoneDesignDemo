package itg8.com.nowzonedesigndemo.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Android itg 8 on 4/20/2017.
 */

public class CommonMethod {


    public static final String USER_CURRENT_AVG = "USER_CURRENT_AVG";


    public static final String SAVEDAYS = "SAVEDAYS";


    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATE_FORMAT_WITH_TIME = "hh:mm a";

    public static final String SAVEALARMTIME = "SAVEALARMTIME";

    public static final String AVG_MILE_BY_HEIGHT = "avgKmHeight";
    public static final String STEP_COUNT = "stepCount";
    public static final String ENABLE_TO_CONNECT = "enableToConnect";
    public static final String GOAL = "stepGoal";
    public static final String START_ALARM_TIME = "START_ALARM_TIME";
    public static final String END_ALARM_TIME = "END_ALARM_TIME";
    public static final String ALARM_FROMTIMEPICKER = "ALARM_FROMTIMEPICKER";
    public static final String ACTION_ALARM_NOTIFICATION = "ACTION_ALARM_NOTIFICATION";
    public static final String SLEEP_STARTED = "sleepStarted";
    public static final String STEP_GOAL = "stepGoal";


    private static Typeface typeface;
    public static String FROMWEEk="from_week";
    public static final String SELECTED_DEVICE = "SELECTED_DEVICE";
    public static final String ACTION_GATT_CONNECTED = "DEVICE_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "DEVICE_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED="ACTION_GATT_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE="ACTION_DATA_AVAILABLE";
    public final static UUID DATA_ENABLE = 				UUID.fromString("0000ffa3-0000-1000-8000-00805f9b34fb");
    public final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final int JOB_CONNECT_DEVICE = 1;

    public static final byte[] BYTE_ARRAY_ON = new byte[]{01};
    public static final byte[] BYTE_ARRAY_OFF = new byte[]{00};
    // DAQ Specific UUIDs
    public final static UUID TEMP_SERVICE_UUID = 			UUID.fromString("0000ffa0-0000-1000-8000-00805f9b34fb");
    // BLE UUIDs
    public final static UUID SENSOR_ON_OFF = 			UUID.fromString("0000ffa1-0000-1000-8000-00805f9b34fb");
    public static final String SHARED = "NZPREF";
    public static final String DEVICE_ADDRESS = "device_address";

    public static final String DEVICE_NAME = "device_name";
    public static final String CONNECTED = "connected";
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final String TIMESTAMP = "time";
    public static final String STATE = "state" ;
    public static final String BPM_COUNT = "bpmCount";
    public static final String FINISHED_DATA = "finishedData";
    public static final String STORAGE_PERM = "storage_perm";
    public static final String STORAGE_PATH = "storage_path";
    public static final String ALARM_END_TIME = "AlarmEndTime";


    public static Typeface setFontOpenSansSemiBold(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        return typeface;
    }public static Typeface setFontRobotoLight(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
        return typeface;
    }


    public static DataModel getArragedData(byte[] data){
        DataModel model=null;
        if (data != null && data.length > 0) {
            byte value1 = data[8]; //Convert to double [Higher bit of pressure]
            byte value2 = data[9]; //Convert to double [Lower bit of pressure]
            byte xHValue = data[0];
            byte xLValue = data[1];
            byte yHValue = data[2];
            byte yLValue = data[3];
            byte zHValue = data[4];
            byte zLValue = data[5];
            byte valueBattery1 = data[6];
            byte valueBattery2 = data[7];

//            String batteryBytes = new byte[]{valueBattery1,valueBattery2};
//            String pressure= bytesToHex(new byte[]{value1,value2});
//            String valueForX=bytesToHex(new byte[]{xHValue,xLValue});
//            String valueForY=bytesToHex(new byte[]{yHValue,yLValue});
//            String valueForZ=bytesToHex(new byte[]{zHValue,zLValue});
            model=new DataModel();
            try {
                model.setPressure(bytesToHex(new byte[]{value1,value2}));
                model.setX(bytesToHex(new byte[]{xHValue,xLValue}));
                model.setY(bytesToHex(new byte[]{yHValue,yLValue}));
                model.setZ(bytesToHex(new byte[]{zHValue,zLValue}));
                model.setBattery(bytesToHex(new byte[]{valueBattery1,valueBattery2}));
                model.setTimestamp(Calendar.getInstance(Locale.getDefault()).getTimeInMillis());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    public static int bytesToHex(byte[] bytes) throws NumberFormatException {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return convertHexToInt(new String(hexChars));
    }

    private static int convertHexToInt(String s) {
        return Integer.parseInt(s,16);
    }

    public static List<Map<Integer, Double>> countBPM(List<DataModel> models, double delta){
        List<Double> data=new ArrayList<>();
        for (DataModel model :
                models) {
            data.add(model.getPressure());
        }
        return peak_detection(data,delta);
    }

    public static <U> List<Map<U, Double>> peak_detection(List<Double> values, Double delta, List<U> indices) {
//        assert (indices != null);
//        assert (values.size() != indices.size());

        LinkedHashMap<U, Double> maxima = new LinkedHashMap<U, Double>();
        LinkedHashMap<U, Double> minima = new LinkedHashMap<U, Double>();
        List<Map<U, Double>> peaks = new ArrayList<Map<U, Double>>();
        peaks.add(maxima);
        peaks.add(minima);

        Double maximum = null;
        Double minimum = null;
        U maximumPos = null;
        U minimumPos = null;

        boolean lookForMax = true;

        Integer pos = 0;
        for (Double value : values) {
            if (maximum == null || value > maximum) {
                maximum = value;
                maximumPos = indices.get(pos);
            }

            if (minimum == null || value < minimum) {
                minimum = value;
                minimumPos = indices.get(pos);
            }

            if (lookForMax) {
                if (value < maximum - delta) {
                    maxima.put(maximumPos, value);
                    minimum = value;
                    minimumPos = indices.get(pos);
                    lookForMax = false;
                }
            } else {
                if (value > minimum + delta) {
                    minima.put(minimumPos, value);
                    maximum = value;
                    maximumPos = indices.get(pos);
                    lookForMax = true;
                }
            }

            pos++;
        }

        return peaks;
    }

    public static List<Map<Integer, Double>> peak_detection(List<Double> values, Double delta) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            indices.add(i);
        }

        return peak_detection(values, delta, indices);
    }

    public static String getTimeFromTMP(long timeStamp) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String ss="";
        try {
            ss=dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }



}
