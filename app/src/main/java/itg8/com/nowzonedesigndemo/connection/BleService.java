package itg8.com.nowzonedesigndemo.connection;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModel;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.db.DbHelper;
import itg8.com.nowzonedesigndemo.db.tbl.TblBreathCounter;
import itg8.com.nowzonedesigndemo.tosort.RDataManager;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.BleConnectionManager;
import itg8.com.nowzonedesigndemo.utility.DeviceState;
import itg8.com.nowzonedesigndemo.utility.RDataManagerImp;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.getArragedData;


public class BleService extends Service implements ConnectionStateListener, RDataManagerListener {
    private static final String TAG = BleService.class.getSimpleName();
    private static final String TAGWithFull = BleService.class.getCanonicalName();
    public static final String ACTION_DEVICE_CONNECTED = TAGWithFull + ".ACTION_DEVICE_CONNECTED";
    public static final String ACTION_COUNT_RESULT = TAGWithFull + ".ACTION_COUNT_RESULT";
    public static final String ACTION_STEP_COUNT = TAGWithFull +".ACTION_STEP_COUNT";
    private final IBinder mBinder = new LocalBinder();
    TblBreathCounter counter;
    DbHelper helper = new DbHelper(this);
    Dao<TblBreathCounter, Integer> userDao = null;
    private BleConnectionManager manager;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(getResources().getString(R.string.action_device_disconnect))) {
                if (manager != null) {
                    manager.disconnect();
                }
            }
        }
    };
    private BluetoothManager mBluetoothManager;
    private RDataManager dataManager;


    public BleService() {
        manager = new BleConnectionManager(this);
        dataManager = new RDataManagerImp(this);
        try {
            userDao = helper.getUserDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"BLE Service started");
        registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.action_device_disconnect)));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String address = null, name = null;
        if (intent != null && intent.hasExtra(CommonMethod.DEVICE_ADDRESS)) {
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
        if (SharePrefrancClass.getInstance(this).hasSPreference(CommonMethod.DEVICE_ADDRESS)) {
            sendToConnect(SharePrefrancClass.getInstance(this).getPref(CommonMethod.DEVICE_ADDRESS), "name");
        }
    }

    private void sendToConnect(String address, String name) {
        if (manager != null)
            manager.selectedDevice(address, name);
    }


    @Override
    public void onDeviceConnected(String address) {
        SharePrefrancClass.getInstance(this).savePref(CommonMethod.DEVICE_ADDRESS, address);
        SharePrefrancClass.getInstance(this).setPrefrance(CommonMethod.CONNECTED, true);
        sendBroadcast(ACTION_DEVICE_CONNECTED);
    }

    private void sendBroadcast(String action) {
        Log.d(TAG, "SendBroadcast");
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * this method use to send broadcast for each key feature with action defined intent
     *
     * @param key  KEY FOR INTENT DATA
     * @param data ORIGINAL DATA
     */
    private void sendBroadcast(String key, Object data) {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        if (data instanceof DataModel)
            intent.putExtra(key, (DataModel) data);
        else if (data instanceof Integer) {
                intent.putExtra(key, (int) data);
        }
        sendBroadcast(intent);
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
        SharePrefrancClass.getInstance(this).savePref(CommonMethod.STATE, state.name());
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
        unregisterReceiver(receiver);
        startService(new Intent(this, BleService.class));
        Log.d(TAG, "On destroy  called");
    }

    @Override
    public void connectGatt(BluetoothDevice device, BluetoothGattCallback callback) {
        manager.setBluetoothGatt(device.connectGatt(this, true, callback));

    }

    @Override
    public void onFail(DeviceState state, int status) {
        Log.d(TAG, "Failed:" + state.name() + " status code: " + status);
    }

    @Override
    public void onDataProcessed(DataModel dataModel) {
        sendBroadcast(CommonMethod.ACTION_DATA_AVAILABLE, dataModel);

    }

    /**
     * @param count
     * @param timestamp
     */
    @Override
    public void onCountAvailable(int count, long timestamp) {
        sendCountBroadcast(count, timestamp);
    }

    @Override
    public void onStepCountReceived(int step) {
        sendStepBroadcast(ACTION_STEP_COUNT,step);
    }

    private void sendStepBroadcast(String action, int step) {
        sendBroadcast(action,step);
    }

    private void sendCountBroadcast(int count, long timestamp) {
//        intent=new Intent(ACTION_COUNT_RESULT);
//        intent.putExtra(CommonMethod.BPM_COUNT,count);
        sendBroadcast(CommonMethod.BPM_COUNT, count);
        storeToDb(count, timestamp);
    }

    /**
     * after getting count from <a href="onCountAvailable">onCountAvailable</a> and store to db
     *
     * @param count
     * @param timestamp
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

    public class LocalBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }
}
