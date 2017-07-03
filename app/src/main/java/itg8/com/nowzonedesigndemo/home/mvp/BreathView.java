package itg8.com.nowzonedesigndemo.home.mvp;


import itg8.com.nowzonedesigndemo.utility.BreathState;

public interface BreathView {
    void onPressureDataAvail(double pressure);
    void onDeviceConnected();
    void onDeviceDisconnected();

//    void onLineChartDataInit(LineChartData data);

    void onBreathCountAvailable(int intExtra);

    void onStepCountReceived(int intExtra);

    void onStartDeviceScanActivity();

    void onBreathingStateAvailable(BreathState state);


    void onStateTimeHistoryReceived(StateTimeModel stateTimeModel);

    void onRemoveSnackbar();

}
