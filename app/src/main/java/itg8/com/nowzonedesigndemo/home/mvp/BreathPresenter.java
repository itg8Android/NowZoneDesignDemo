package itg8.com.nowzonedesigndemo.home.mvp;

import android.content.Context;

/**
 * Created by itg_Android on 3/2/2017.
 */

public interface BreathPresenter {
    void onCreate();
    void onPause();
    void passContext(Context context);
    void onAttach();
    void onDetach();

    void initGraphData();

    public interface BreathFragmentModelListener{
        void onPressureReceived(double pressure);

        void onCountReceived(int intExtra);

        void onStepReceived(int intExtra);

        void startShowingDevicesList();
    }
}
