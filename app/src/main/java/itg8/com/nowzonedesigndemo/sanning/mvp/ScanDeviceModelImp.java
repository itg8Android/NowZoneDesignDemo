package itg8.com.nowzonedesigndemo.sanning.mvp;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * Created by itg_Android on 2/21/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScanDeviceModelImp implements ScanDeviceModel {


    private static final String TAG = ScanDeviceModelImp.class.getSimpleName();
    private ScanDeviceModelListener listener;
    private ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result) {
//                super.onScanResult(callbackType, result);
            Log.d(TAG, "Scan result got " + result);
            listener.onScanResult(callbackType, result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
//                super.onBatchScanResults(results);
            Log.d(TAG, "BatchScanResult got " + results);
            listener.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
//                super.onScanFailed(errorCode);
            Log.d(TAG, "Scan failed  " + errorCode);
            listener.onScanFailed(errorCode);
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLeScanCall(BluetoothLeScanner scanner, final ScanDeviceModelListener listener) {
        Log.d(TAG, "Scanner arrived " + scanner);
        this.listener = listener;

        scanner.startScan(callback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStopScanning(BluetoothLeScanner scanner) {
        scanner.stopScan(callback);
    }
}
