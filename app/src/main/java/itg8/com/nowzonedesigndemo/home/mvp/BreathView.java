package itg8.com.nowzonedesigndemo.home.mvp;



public interface BreathView {
    void onPressureDataAvail(double pressure);
    void onDeviceConnected();
    void onDeviceDisconnected();

//    void onLineChartDataInit(LineChartData data);

    void onBreathCountAvailable(int intExtra);

    void onStepCountReceived(int intExtra);

    void onStartDeviceScanActivity();
}
