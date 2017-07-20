package itg8.com.nowzonedesigndemo.utility;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import itg8.com.nowzonedesigndemo.connection.ConnectionStateListener;
import itg8.com.nowzonedesigndemo.exception.StringEmptyException;
import itg8.com.nowzonedesigndemo.tosort.ConnectionManager;
import timber.log.Timber;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.BYTE_ARRAY_ON;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.CLIENT_CHARACTERISTIC_CONFIG;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.DATA_ENABLE;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.SENSOR_ON_OFF;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.TEMP_SERVICE_UUID;


/**
 * Created by itg_Android on 2/28/2017.
 */

public class BleConnectionManager implements ConnectionManager {

//    private static final int ERR = 0;
    private static final String TAG = BleConnectionManager.class.getSimpleName();
    private final ConnectionStateListener listener;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private String address;


    private Queue<BluetoothGattDescriptor> descriptorWriteQueue = new LinkedList<>();
    private BluetoothGattCallback callback;
    private Queue<BluetoothGattCharacteristic> characteristicReadQueue = new LinkedList<BluetoothGattCharacteristic>();
    private int retryDescover=1;
    private  boolean connecting;

    public BleConnectionManager(ConnectionStateListener listener) {
        if (listener == null) {
            throw new NullPointerException("You need to implement ConnectionStateListener in caller service...");
        }
        this.listener = listener;
        initCallback();
    }

    private void initCallback() {
        callback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    listener.onDeviceConnected(address);
                    listener.currentState(DeviceState.CONNECTED);
                    connecting=false;
                    Log.d(TAG, "Connected to GATT Server");
                    Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                    if (discoverServices()) {
                        listener.currentState(DeviceState.DISCOVERING);
                    }else
                    {
                        Log.d(TAG,"Fail to descover service: Error "+status );
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    listener.currentState(DeviceState.DISCONNECTED);
                    Log.i(TAG, "Disconnected from GATT server.");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.w(TAG, "onServicesDiscovered");
                    listener.currentState(DeviceState.DISCOVERED);
                    if (configureServices()) {
                        Log.d(TAG, "write successful");
                    }
                } else {
                    failWithReason(DeviceState.DISCOVER_FAIL, status);
                    Log.w(TAG, "onServicesDiscovered fail received: " + status);
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    listener.currentState(DeviceState.READ);
                } else {
                    failWithReason(DeviceState.READ_FAIL, status);
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Logs.d("Characteristics written:"+characteristic.getValue());
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(TAG, "DescriptorWrite Called");

                if (writeCharacteristics(TEMP_SERVICE_UUID, SENSOR_ON_OFF)) {
                    listener.currentState(DeviceState.WRITE);
                } else {
                    failWithReason(DeviceState.WRITE_FAIL, status);
                }
                if(descriptorWriteQueue!=null && descriptorWriteQueue.size()>0)
                    descriptorWriteQueue.poll();
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            Log.d(TAG,"characteristics int value: "+ characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, ERR).intValue());
                dataReceived(characteristic.getValue());
            }
        };
    }

    private boolean discoverServices() {
        if (mBluetoothGatt == null)
            throw new NullPointerException("mBluetoothGatt still null check it");
        return mBluetoothGatt.discoverServices();
    }


    /**
     * This will help us to get data from onChangeCharacteristics
     *pass to BleService
     * @param value =data;
     */
    private void dataReceived(byte[] value) {
//            Log.d(TAG, "data received:" + Helper.bytesToHex(value));
//        createFile(value.toString());
            listener.onDataAvail(value);
    }

    private void createFile(String log) {
        File completeFileStructure = new File(Environment.getExternalStorageDirectory()+File.separator+"nowzone","raw data1.txt");
        try {
            FileWriter fWriter;
            if(completeFileStructure.exists()) {
                fWriter = new FileWriter(completeFileStructure, true);
                fWriter.append(log).append("\n");
            }else {
                fWriter = new FileWriter(completeFileStructure, true);
                fWriter.write(log);
            }
            fWriter.flush();
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean writeCharacteristics(UUID serviceUUID, UUID charactUUID) throws NullPointerException {

        BluetoothGattService s = mBluetoothGatt.getService(UUID.fromString(serviceUUID.toString()));
        if (s == null) {
            throw new NullPointerException("BluetoothGattService Is null ");
        }
        BluetoothGattCharacteristic gattCharacteristic = s.getCharacteristic(UUID.fromString(charactUUID.toString()));
        if (gattCharacteristic == null) {
            throw new NullPointerException("BluetoothGattCharacteristic Is null ");
        }

        return writeCharacteristic(gattCharacteristic, BYTE_ARRAY_ON);

    }

    
    private boolean writeCharacteristic(BluetoothGattCharacteristic gattCharacteristic, byte[] value) {
        gattCharacteristic.setValue(value);
        return mBluetoothGatt.writeCharacteristic(gattCharacteristic);
    }

    private void failWithReason(DeviceState state, int status) {
        listener.currentState(state);
        listener.onFail(state, status);
    }

    @Override
    public void onDistroy() {
        mBluetoothGatt.disconnect();
        mBluetoothAdapter = null;
    }

    @Override
    public void selectedDevice(String address, String name) {
        Log.d(TAG, "Selected device Name: " + name);
        try {
            connectToDevice(address);
        } catch (StringEmptyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if(mBluetoothGatt!=null){

            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            callback=null;
            mBluetoothGatt=null;
//            mBluetoothGatt=null;
        }
    }

    private void connectToDevice(String address) throws StringEmptyException {
        if (address != null && !TextUtils.isEmpty(address)) {
            connect(address);
            this.address = address;
        } else {
            throw new StringEmptyException("not having valid address");
        }
    }


    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    private boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        /*
        // Previously connected device.  Try to reconnect.
        // When we change device.connect(context, autoconnect, callback) autoconnect to false, we cannot use below method there.
        // Device acting strange when changing device autoconnect to true. It connect to only some device. So decided to change autoconnect
        // to false and commenting below code
        */
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
////            mBluetoothGatt.disconnect();
////            mBluetoothGatt.close();
//
//            if (mBluetoothGatt.connect()) {
//                Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//                return true;
//            } else {
//                return false;
//            }
//        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        Log.d(TAG, "Trying to create a new connection.");

        mBluetoothDeviceAddress = address;
        if(callback==null)
            initCallback();
        if(!connecting) {
            listener.connectGatt(device, callback);
            connecting = true;
        }

            return true;
    }


    /**
     * Configures a list of services for notification
     *
     * @return true if discovers all the services offered by the DAQ and
     * configuration is successful
     * @param
     */
    public boolean configureServices() {
        List<BluetoothGattService> gattServices = getSupportedGattServices();
        if (gattServices.isEmpty()) {
            return false;
        }
        // We have a list of services
        // Iterate through the list of services
        for (BluetoothGattService gattService : gattServices) {
            // One more service found
            // Enable notification on characteristic status change if supported
            // Get Characteristics of the service
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

                //   Log.d(TAG, "Service " + gattService.getUuid().toString() + " , Characteristics " + gattCharacteristic.getUuid().toString());
                // One more characteristic UUID to check
                if (DATA_ENABLE.toString().contentEquals(gattCharacteristic.getUuid().toString())) {
                    // Characteristic found requires notification
                    int charaProp = gattCharacteristic.getProperties();
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        // Characteristic Supports Notify

                        setCharacteristicNotification(gattCharacteristic, true); // Enable Notification on this characteristic
                    }
                }
            }
        }
        return true;
    }


    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) {
            Log.d(TAG,"BluetoothGatt is null");
            return null;
        }

        return mBluetoothGatt.getServices();
    }


    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     * @return true on success
     */
    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                 boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        if (mBluetoothGatt.setCharacteristicNotification(characteristic, enabled)) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                //put the descriptor into the write queue
                descriptorWriteQueue.add(descriptor);
                //if there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                if (descriptorWriteQueue.size() == 1) {
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
                return true;
            }
        }
        return false;
    }


    public void setBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }


    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        if(mBluetoothGatt==null)
            this.mBluetoothGatt = bluetoothGatt;
    }
}
