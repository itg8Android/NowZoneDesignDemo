package itg8.com.nowzonedesigndemo.home.mvp;

import android.content.Context;
import android.content.IntentFilter;
import java.util.Random;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.utility.BreathState;


public class BreathPresenterImp implements BreathPresenter, BreathPresenter.BreathFragmentModelListener {


    private BreathView view;
    private BreathFragmentModel model;
    private Context context;
//    private Runnable mTimer2;
//    private Handler mHandler;
    private double graph2LastXValue = 5d;


    public BreathPresenterImp(BreathView view) {
        this.view = view;
        model = new BreathModelImp(this);
    }

    @Override
    public void startShowingDevicesList() {
        if(view!=null){
            view.onStartDeviceScanActivity();
        }
    }

    @Override
    public void onStateReceived(BreathState state) {
        if(checkNotNull())
            view.onBreathingStateAvailable(state);
    }

    @Override
    public void onStateTimeReceived(StateTimeModel stateTimeModel) {
        if(checkNotNull())
            view.onStateTimeHistoryReceived(stateTimeModel);
    }

    @Override
    public void onCreate() {
        if(model.getReceiver()!=null && context!=null){
            context.registerReceiver(model.getReceiver(),new IntentFilter(context.getResources().getString(R.string.action_data_avail)));
        }
        model.onInitStateTime();
//        mTimer2 = new Runnable() {
//            @Override
//            public void run() {
//                graph2LastXValue += 1d;
//                view.onPressureDataAvail(getRandom());
//                mHandler.postDelayed(this, 200);
//            }
//        };
//        mHandler.postDelayed(mTimer2, 1000);
    }



    @Override
    public void onPause() {

    }

    @Override
    public void passContext(Context context) {
        this.context=context;
        model.initDB(context);
        model.checkBLEConnected(context);
    }



    @Override
    public void onAttach() {
      //  model.check
    }

    @Override
    public void onDetach() {
        if(model.getReceiver()!=null && context!=null){
            context.unregisterReceiver(model.getReceiver());
        }
        model.onDestroy();
        context=null;
        view=null;
    }

    @Override
    public void onInitTimeHistory() {
        model.onInitStateTime();
    }

    @Override
    public void onPressureReceived(double pressure) {
        if (checkNotNull()) {
            view.onPressureDataAvail(pressure);
        }
    }

    @Override
    public void onCountReceived(int intExtra) {
        if(intExtra>0 && checkNotNull()){
            view.onBreathCountAvailable(intExtra);
        }
    }



    @Override
    public void onStepReceived(int intExtra) {
        if(intExtra>0 && checkNotNull())
            view.onStepCountReceived(intExtra);
    }

    private boolean checkNotNull() {
        return view != null && context!=null;
    }

    private double mLastRandom = 2;
    private Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }

    @Override
    public void initGraphData() {
//        List<Line> lines = new ArrayList<>();
//        Line line = new Line();
//        line.setHasLines(true);
//        line.setHasPoints(false);
//        line.setCubic(true);
//        line.setColor(Color.BLUE);
//        lines.add(line);
//        LineChartData data = new LineChartData(lines);
//        data.setAxisXBottom(null);
//        data.setAxisYLeft(null);
//        data.setBaseValue(Float.NEGATIVE_INFINITY);
//        if(view!=null)
//            view.onLineChartDataInit(data);
    }
}

