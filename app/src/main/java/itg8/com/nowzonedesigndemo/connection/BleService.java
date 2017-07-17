package itg8.com.nowzonedesigndemo.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModel;
import itg8.com.nowzonedesigndemo.common.ProfileModel;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.db.DbHelper;
import itg8.com.nowzonedesigndemo.db.tbl.TblAverage;
import itg8.com.nowzonedesigndemo.db.tbl.TblBreathCounter;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.tosort.RDataManager;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.BleConnectionManager;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.DeviceState;
import itg8.com.nowzonedesigndemo.utility.Helper;
import itg8.com.nowzonedesigndemo.utility.OnStateAvailableListener;
import itg8.com.nowzonedesigndemo.utility.RDataManagerImp;
import itg8.com.nowzonedesigndemo.utility.StateCheckImp;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.STEP_COUNT;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.getArragedData;


public class BleService extends OrmLiteBaseService<DbHelper> implements ConnectionStateListener, RDataManagerListener, OnStateAvailableListener {
    private static final String TAG = BleService.class.getSimpleName();
    private static final String TAGWithFull = BleService.class.getCanonicalName();
    public static final String ACTION_DEVICE_CONNECTED = TAGWithFull + ".ACTION_DEVICE_CONNECTED";
    public static final String ACTION_COUNT_RESULT = TAGWithFull + ".ACTION_COUNT_RESULT";
    public static final String ACTION_STEP_COUNT = TAGWithFull + ".ACTION_STEP_COUNT";
    public static final String ACTION_STATE_ARRIVED = TAGWithFull + ".ACTION_STATE_AVAIL";
    public static final String ACTION_DEVICE = TAGWithFull + ".ACTION_DEVICE";
    private final IBinder mBinder = new LocalBinder();
    private final StateCheckImp stateManager;
    TblBreathCounter counter;
    Dao<TblBreathCounter, Integer> userDao = null;
    private Dao<TblAverage, Integer> avgDao = null;
    private Dao<TblState, Integer> stateDao = null;
    private Dao<TblStepCount, Integer> stepDao = null;
    private BleConnectionManager manager;
    private Calendar calendar;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(getResources().getString(R.string.action_device_disconnect))) {
                if (manager != null) {
                    if(intent.hasExtra(CommonMethod.ENABLE_TO_CONNECT))
                        manager.disconnect();
                    SharePrefrancClass.getInstance(context).savePref(CommonMethod.STATE,DeviceState.DISCONNECTED.name());
                    Intent i=new Intent(context.getResources().getString(R.string.action_data_avail));
                    i.putExtra(CommonMethod.ACTION_GATT_DISCONNECTED,"DISCONNECT");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                }
            }
        }
    };
    private BluetoothManager mBluetoothManager;
    private RDataManager dataManager;
    private TblState tblState;
    private ProfileModel profileModel;


    public BleService() {
        manager = new BleConnectionManager(this);
        stateManager = new StateCheckImp(this, this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BLE Service started");
        dataManager = new RDataManagerImp(this,getApplicationContext());
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.action_device_disconnect)));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        calendar=Calendar.getInstance();
        profileModel=((AppApplication)getApplication()).getProfileModel();
        if(intent!=null && intent.hasExtra(CommonMethod.ENABLE_TO_CONNECT)){
            if(manager!=null){
//                manager.disconnect();

                return START_STICKY;
            }
        }
        try {
            userDao = getHelper().getCountDao();
            avgDao = getHelper().getAvgDao();
            stateDao = getHelper().getStateDao();
            stepDao = getHelper().getStepDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String address = null, name = null;
        if (intent!=null && intent.hasExtra(CommonMethod.DEVICE_ADDRESS)) {
            address = intent.getStringExtra(CommonMethod.DEVICE_ADDRESS);
            name = intent.getStringExtra(CommonMethod.DEVICE_NAME);
        }
        if (initialize() && address != null && name != null) {
            selectedDevice(address, name);
        } else {
            reconnectBleDevice();
        }
        return START_STICKY;
    }


    public void selectedDevice(String adress, String name) {
        sendToConnect(adress, name);
    }


    private void reconnectBleDevice() {
        if (manager == null)
            manager = new BleConnectionManager(this);
        checkAvailableDeviceToConnect();
    }

    private void checkAvailableDeviceToConnect() {
        if (SharePrefrancClass.getInstance(getApplicationContext()).hasSPreference(CommonMethod.DEVICE_ADDRESS) &&
                SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.DEVICE_ADDRESS)!=null) {
            sendToConnect(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.DEVICE_ADDRESS), "name");
        }
    }

    private void sendToConnect(String address, String name) {
        if (manager != null)
            manager.selectedDevice(address, name);
    }


    @Override
    public void onDeviceConnected(String address) {
        SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.DEVICE_ADDRESS, address);
        SharePrefrancClass.getInstance(getApplicationContext()).setPrefrance(CommonMethod.CONNECTED, true);
        sendBroadcast(ACTION_DEVICE_CONNECTED);
    }

    private void sendBroadcast(String action) {
        Log.d(TAG, "SendBroadcast");
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /**
     * this method use to send broadcast for each key feature with action defined intent
     *
     * @param key  KEY FOR INTENT DATA
     * @param data ORIGINAL DATA
     */
    private void sendBroadcast(String key, Object data) {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        if (data instanceof DataModel){
            intent.putExtra(key, ((DataModel) data).getPressure());
            intent.putExtra(CommonMethod.ACTION_DATA_LONG,((DataModel) data).getTimestamp());
    }
        else if (data instanceof Integer) {
            intent.putExtra(key, (int) data);
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void onDeviceDiscovered() {

    }

    @Override
    public void onDeviceConnectionChanged() {

    }


    @Override
    public void onDataAvail(byte[] data) {
        //        SharePrefrancClass.getInstance(this).savePref(CommonMethod.TIMESTAMP, Calendar.getInstance().getTimeInMillis());

        dataManager.onRawDataModel(getArragedData(data), getApplicationContext());

//        Log.d(TAG, "value : " + getArragedData(data).getPressure() + " , " + " timestamp: " + CommonMethod.getTimeFromTMP(getArragedData(data).getTimestamp()));

    }


    @Override
    public void currentState(DeviceState state) {
        Log.d(TAG, "state is: " + state.name());
        SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.STATE, state.name());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        manager.setBluetoothAdapter(mBluetoothAdapter);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        manager.disconnect();
        startService(new Intent(this, BleService.class));
        Log.d(TAG, "On destroy  called");
    }

    @Override
    public void connectGatt(BluetoothDevice device, BluetoothGattCallback callback) {
//        manager.disconnect();
        manager.setBluetoothGatt(device.connectGatt(getApplicationContext(), true, callback));

    }

    @Override
    public void onFail(DeviceState state, int status) {
        Log.d(TAG, "Failed:" + state.name() + " status code: " + status);
    }

    @Override
    public void onDataProcessed(DataModel dataModel) {
        sendBroadcast(CommonMethod.ACTION_DATA_AVAILABLE, dataModel);

    }
    private double dLast;
    private float a = 0.96f;
    private static final double PI_MIN = -8.02d;
    private static final double PI_MAX = 8.02d;
    private static final double MIN_PRESSURE = 1100;
    private static final double MAX_PRESSURE = 8100;

    private double calculateProportion(double pressure) {
//        return (-0.02+(1.02*((pressure-(lastMax-500))/(lastMax-(lastMax-500)))));
//        double d=(double) Math.round((CONST_1+(CONST_2*((pressure-(lastMin))/(lastMax-lastMin)))) * 1000000000000000000d) / 1000000000000000000d;
//        s(i)=a*y(i)+(1-a)*s(i-1)


        double d = a * pressure + ((1 - a) * dLast);
//        double d=pressure;
//        rolling.add(pressure);
//        d=rolling.getaverage();
        dLast = d;
        // Log.d(TAG,"ds:"+d);
//        return (PI_MIN + ((PI_MAX-PI_MIN) * ((d - (lastMin)) / (lastMax - lastMin))));

//        update(pressure);
//        Log.d(TAG, String.valueOf(var()));

        return (PI_MIN + ((PI_MAX - PI_MIN) * ((d - (MIN_PRESSURE)) / (MAX_PRESSURE - MIN_PRESSURE))));
    }


    /**
     * @param count     breath count
     * @param timestamp last timestamp from list
     */
    @Override
    public void onCountAvailable(int count, long timestamp) {
        sendCountBroadcast(count, timestamp);
        stateManager.calculateNewBreathAvgForState(timestamp, userDao);
        checkStateOfMind(count,timestamp);
    }

    private void checkStateOfMind(int count, long timestamp) {
        int avgCount = SharePrefrancClass.getInstance(getApplicationContext()).getIPreference(CommonMethod.USER_CURRENT_AVG);
        if(avgCount<=0)
            return;
        List<TblBreathCounter> breathCounters = null;
        try {
            QueryBuilder<TblBreathCounter, Integer> builder = userDao.queryBuilder().limit(2L).orderBy(TblBreathCounter.FIELD_NAME_ID, false);
            breathCounters = userDao.query(builder.prepare());
            Log.d(TAG,"breathcounters table data:"+new Gson().toJson(breathCounters));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        int mLastOneCount = 0;
        int mSecondLastCount = 0;
        if (breathCounters != null)
            if (breathCounters.size() >= 2) {
                mLastOneCount = breathCounters.get(0).getCount();
                mSecondLastCount = breathCounters.get(1).getCount();
            }


        if (mLastOneCount <= 0 && mSecondLastCount <= 0) {
            return;
        }
        int newCalmCheck = avgCount - 4;
        int newStressCheck = avgCount + 4;

        if (mLastOneCount <= newCalmCheck && mSecondLastCount <= newCalmCheck && count <= newCalmCheck) {
            sendBroadcastState(BreathState.CALM,count,timestamp);
        } else if (mLastOneCount >= newStressCheck && mSecondLastCount >= newStressCheck && count >= newStressCheck) {
            sendBroadcastState(BreathState.STRESS, count, timestamp);
        } else{
            if(mLastOneCount == count && mSecondLastCount == count) {
                sendBroadcastState(BreathState.FOCUSED, count, timestamp);
            }
        }
    }

    private void sendBroadcastState(BreathState state, int count, long timestamp) {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        intent.putExtra(ACTION_STATE_ARRIVED, state);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        saveStateToDb(state,count,timestamp);
    }

    private void saveStateToDb(BreathState state, int count, long timestamp) {
        /**
         * We will uncomment it when user needs data to be group as per state.
         * TODO uncomment for grouping state from db
         */
//        QueryBuilder<TblState, Integer> builder = stateDao.queryBuilder().limit(1L).orderBy(TblState.FIELD_ID, false);
//        List<TblState> tblStates = null;
//        try {
//            tblStates = stateDao.query(builder.prepare());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        TblState tblState;
//        if (tblStates!=null && tblStates.size()>0) {
//            tblState = tblStates.get(0);
//            if(!state.name().equalsIgnoreCase(tblState.getState())){
//                tblState.setTimestampEnd(timestamp);
//                try {
//                    stateDao.update(tblState);
//                    tblState=new TblState();
//                    tblState.setTimestampStart(timestamp);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }else
//            {
//
//            }
//        }
//        else {
//            tblState=new TblState();
//            tblState.setTimestampStart(timestamp);
//        }
        tblState=new TblState();
        String currentDate = Helper.getCurrentDate();
        tblState.setTimestampEnd(timestamp);
        tblState.setCount(count);
        tblState.setDate(currentDate);
        tblState.setState(state.name());
        try {
            stateDao.create(tblState);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStepCountReceived(int step) {
        storeStepToDb(step);
        SharePrefrancClass.getInstance(getApplicationContext()).setIPreference(STEP_COUNT,step);
        sendStepBroadcast(ACTION_STEP_COUNT, step);
    }

    private void storeStepToDb(int step) {
        if(profileModel==null){
            profileModel=((AppApplication)getApplication()).getProfileModel();
        }
        Observable.create((ObservableOnSubscribe<Void>) e->{
            calendar.setTimeInMillis(System.currentTimeMillis());
            List<TblStepCount> countList=stepDao.queryBuilder().where().eq(TblStepCount.FIELD_DATE,calendar.getTime()).query();
            TblStepCount count;
            boolean fi=false;
            if(countList.size()>0) {
                count = countList.get(countList.size() - 1);
                fi=true;
            }else
                count=new TblStepCount();
            count.setDate(calendar.getTime());
            count.setSteps(step);
            count.setCalBurn(Helper.calculateCalBurnByStepCount(step,profileModel));
            count.setGoal(SharePrefrancClass.getInstance(getApplicationContext()).getIPreference(CommonMethod.GOAL));
            if(!fi)
                stepDao.create(count);
            else
                stepDao.update(count);

        }).subscribeOn(Schedulers.io())
        .subscribe(new Observer<Void>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Void aLong) {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,e.getMessage(),e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void sendStepBroadcast(String action, int step) {
        sendBroadcast(action, step);
    }

    private void sendCountBroadcast(int count, long timestamp) {
//        intent=new Intent(ACTION_COUNT_RESULT);
//        intent.putExtra(CommonMethod.BPM_COUNT,count);

//        if(count>) {
            sendBroadcast(CommonMethod.BPM_COUNT, count);
            storeToDb(count, timestamp);
//        }
    }

    /**
     * after getting count from <a href="onCountAvailable">onCountAvailable</a> and store to db
     *
     * @param count     breathccount to store in databse
     * @param timestamp timestamp in long
     */
    private void storeToDb(int count, long timestamp) {
        // Retrieve the first source with data
        counter = new TblBreathCounter();
        counter.setCount(count);
        counter.setTimestamp(timestamp);
        Observable.create((ObservableOnSubscribe<TblBreathCounter>) e -> {
            e.onNext(counter);
            e.onComplete();
        })
                .subscribe(new Observer<TblBreathCounter>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TblBreathCounter tblBreathCounter) {
                        try {
                            userDao.create(tblBreathCounter);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public void onStateAvailable(TblAverage model) {
        if (avgDao != null && model != null) {
            Log.d(TAG,"AvgDao in save");
            SharePrefrancClass.getInstance(getApplicationContext()).setIPreference(CommonMethod.USER_CURRENT_AVG, model.getAverage());
            try {
                int c=avgDao.create(model);
                Log.d(TAG,"AvgDao saved: "+c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //TODO Notification: create notification for average here


    }

    @Override
    public void onSleepInterrupted(long timestamp) {

    }

    @Override
    public void onStartWakeupSevice() {
        Log.d(TAG,"notification alarm start active");
        SharePrefrancClass.getInstance(getApplicationContext()).clearPref(CommonMethod.SLEEP_STARTED);
//        Intent intent=new Intent(getApplicationContext(),.class);
//        startActivity(intent);
    }

    public class LocalBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }
}
