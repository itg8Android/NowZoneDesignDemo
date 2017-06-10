package itg8.com.nowzonedesigndemo.utility;

import android.content.Context;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import itg8.com.nowzonedesigndemo.common.CommonMethod;


public class Helper {

    public static SimpleDateFormat dateFormat=new SimpleDateFormat(CommonMethod.DATE_FORMAT, Locale.getDefault());

    public static boolean checkBLE(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getCurrentDate() {
        String newDate="";
        try {
            newDate=dateFormat.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }
}
