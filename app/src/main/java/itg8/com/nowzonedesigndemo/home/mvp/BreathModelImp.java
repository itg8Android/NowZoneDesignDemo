package itg8.com.nowzonedesigndemo.home.mvp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModel;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.utility.DeviceState;

import static itg8.com.nowzonedesigndemo.connection.BleService.ACTION_STEP_COUNT;

/**
 * Created by itg_Android on 3/2/2017.
 */

public class BreathModelImp implements BreathFragmentModel {

    private BreathPresenter.BreathFragmentModelListener listener;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if(intent.hasExtra(CommonMethod.ACTION_DATA_AVAILABLE)){
                    DataModel model = intent.getParcelableExtra(CommonMethod.ACTION_DATA_AVAILABLE);
                    listener.onPressureReceived(model.getPressure());
                }
                if(intent.hasExtra(CommonMethod.BPM_COUNT)){
                    listener.onCountReceived(intent.getIntExtra(CommonMethod.BPM_COUNT,0));
                }if(intent.hasExtra(ACTION_STEP_COUNT)){
                    listener.onStepReceived(intent.getIntExtra(ACTION_STEP_COUNT,0));
                }
            }
        }
    };

    BreathModelImp(BreathPresenter.BreathFragmentModelListener listener) {
        this.listener = listener;
    }

    @Override
    public void checkBLEConnected(Context context) {
        if(context!=null){
            String state =SharePrefrancClass.getInstance(context).getPref(CommonMethod.STATE);
            if(state==null || state.equalsIgnoreCase(DeviceState.DISCONNECTED.name()))
            {
                listener.startShowingDevicesList();
            }
        }
    }

    @Override
    public BroadcastReceiver getReceiver() {
        return receiver;
    }




}
