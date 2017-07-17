package itg8.com.nowzonedesigndemo.tosort;

import android.content.Context;

import itg8.com.nowzonedesigndemo.common.DataModel;


/**
 * Created by itg_Android on 3/7/2017.
 */

public interface RDataManager {
    void onRawDataModel(DataModel model, Context applicationContext);
    void onSleepStarted(boolean b);
    void onStartAlarmTime(long startAlarm);
    void onEndAalrmTime(long endAlarm);
}
