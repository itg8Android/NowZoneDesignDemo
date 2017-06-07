package itg8.com.nowzonedesigndemo.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Trigger;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.connection.BleService;
import itg8.com.nowzonedesigndemo.sanning.ScanDeviceActivity;
import itg8.com.nowzonedesigndemo.utility.DeviceState;
import itg8.com.nowzonedesigndemo.utility.ServiceOnCheck;


public abstract class BaseActivity<T> extends AppCompatActivity {
    public static final String TAG_CLASS_BASE = BaseActivity.class.getCanonicalName();
    public boolean deviceDisconnected;
    String TAG = BaseActivity.class.getSimpleName();
    private FirebaseJobDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(BaseActivity.this));
//        initDispatcher();
        startService(new Intent(this, BleService.class));
    }

    private void initDispatcher() {
        dispatcher.mustSchedule(dispatcher.newJobBuilder().setService(ServiceOnCheck.class).setTag(TAG_CLASS_BASE)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(5, 30))
                .build());
    }

    public void checkDeviceConnection(View v) {
//        long currentTimeStamp= Calendar.getInstance().getTimeInMillis();
//        long oldTimeStamp= SharePrefrancClass.getInstance(this).getLPref(CommonMethod.TIMESTAMP);
//        if(currentTimeStamp-oldTimeStamp>120000){
//            deviceDisconnected=true;
//            checkDeviceDisconnected(v);
//        }
        String state = SharePrefrancClass.getInstance(this).getPref(CommonMethod.STATE);
        if (state == null || state.equalsIgnoreCase(DeviceState.DISCONNECTED.name())) {
            deviceDisconnected = true;
            checkDeviceDisconnected(v);
        }
    }

    public boolean isDeviceDisconnected() {
        return deviceDisconnected;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void checkDeviceDisconnected(View v) {
        Log.d(TAG, "DISCONNECTED ALREADY");
        if (deviceDisconnected) {
            Snackbar snackbar = Snackbar.make(v, R.string.dialog_disconnected_device, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharePrefrancClass.getInstance(getBaseContext()).clearPref(CommonMethod.CONNECTED);
                    SharePrefrancClass.getInstance(getBaseContext()).clearPref(CommonMethod.DEVICE_ADDRESS);
                    startActivity(new Intent(getBaseContext(), ScanDeviceActivity.class));
                    finish();
                }
            });
            snackbar.show();
        }
    }

    public String getClassName(Class<T> tClass){
        return tClass.getSimpleName();
    }
}
