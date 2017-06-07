package itg8.com.nowzonedesigndemo.utility;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.DataModel;


class CheckAccelImp {

    private static final String TAG = CheckAccelImp.class.getSimpleName();
    private static final double THRESHOLD = 92000;
    private static final int STABLE = 65;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    private AccelVerifyListener listener;
    private Observer observer;
    private double x2, y2, z2;
    private double vector;
    //Digital sum filter variables
    private int[] pedi_fil_x = new int[4];


    //TODO
    private int[] pedi_fil_y = new int[4];
    private int[] pedi_fil_z = new int[4];
    private int pedi_fil_index;
    //Steps parameter variables
    private int pedi_sampling_counter;
    private int[] pedi_max = new int[3];
    private int[] pedi_min = new int[3];
    private int[] pedi_axis_result = new int[3];
    private int pedi_result;
    private int[] pedi_p2p = new int[3];
    private int[] pedi_ths = new int[3];
    private int pedi_new_fixed, pedi_old_fixed, pedi_precision;
    private int pedi_threshold;
    private int pedi_refresh_frequency;
    //Time window variables
    private int pedi_bad_flag;
    private int pedi_interval = 0;
    private int pedi_step_counter;
    private int i;
    private int axisIndex;
    private int lastStepVal = 0;
    private long lastTMP = 0;
    private double degconvert = 57.29;
    private double pitch;
    private double roll;


    public CheckAccelImp(final AccelVerifyListener listener) {
        this.listener = listener;
        initPedometer();

        observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer model) {
//                if (model)
//                    listener.onMotionStarts();
//                else
//                    listener.onMotionEnds();
                listener.onStep(model);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

    }

    private Observable<Integer> checkMovement(DataModel model) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                roll = Math.atan2(model.getY(), Math.sqrt((model.getX() * model.getX()) + (model.getZ() * model.getZ()))) * degconvert;
                pitch = Math.atan2(-model.getX(), Math.sqrt((model.getY() * model.getY()) + (model.getZ() * model.getZ()))) * degconvert;

              //  Log.d(TAG, "value ---" + model.getX() + " " + model.getY() + " " + model.getZ() + " roll " + roll + " pitch " + pitch);
//                double vector = calculateVector(model.getX(), model.getY(), model.getZ());
//                Log.d(TAG, "vector: " + vector);

                e.onNext(updateStepParameter((int) model.getX(), (int) model.getY(), (int) model.getZ(), model.getTimestamp()));

//                if (updateStepParameter((int) model.getX(), (int) model.getY(), (int) model.getZ()) != lastStepVal){
                lastStepVal = pedi_step_counter;
            }
        });
    }

//    private double calculateVector(double x, double y, double z) {
//        Log.v(TAG, "axis: x:" + x + " y:" + y + " z:" + z);
//        x2 = x * x;
//        y2 = y * y;
//        z2 = z * z;
//        vector = x2 + y2 + z2;
//        return Math.atan2(x, y) * 180 / 2.14f;
//    }

    void onModelAvail(DataModel model) {
        checkMovement(model).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void pedi_fil_init() {

        for (i = 0; i < 4; i++) {
            pedi_fil_x[i] = 0;
            pedi_fil_y[i] = 0;
            pedi_fil_z[i] = 0;
        }
        pedi_fil_index = 0;
    }

    private void pedi_fil_update(int x, int y, int z) {

        pedi_fil_x[pedi_fil_index] = x;
        pedi_fil_y[pedi_fil_index] = y;
        pedi_fil_z[pedi_fil_index] = z;

        pedi_axis_result[X] = 0;
        pedi_axis_result[Y] = 0;
        pedi_axis_result[Z] = 0;
        for (i = 0; i < 4; i++) {
            pedi_axis_result[X] += pedi_fil_x[i];
            pedi_axis_result[Y] += pedi_fil_y[i];
            pedi_axis_result[Z] += pedi_fil_z[i];
        }
        pedi_fil_index++;
        pedi_fil_index = pedi_fil_index >= 4 ? pedi_fil_index - 4 : pedi_fil_index;
    }


    private void updateTimeWindow(long timestamp) {

        //Depending on the interval update accordingly and flag bad steps
//        if(lastTMP!=0) {

        if (timestamp - lastTMP > 300) {
            pedi_step_counter++;
            lastTMP = timestamp;
        }
//        }else {
//            pedi_step_counter++;
//            lastTMP=timestamp;
//        }
    }

    private void initPedometer() {
        //Steps parameter variables
        pedi_sampling_counter = 0;
        for (i = 0; i < 3; i++) {
            pedi_max[i] = 0;
            pedi_min[i] = 0;
            pedi_axis_result[i] = 0;
            pedi_p2p[i] = 0;
            pedi_ths[i] = 0;
        }
        pedi_result = 0;
        pedi_new_fixed = 0;
        pedi_old_fixed = 0;
        pedi_precision = 0;
        pedi_threshold = 0;
        pedi_refresh_frequency = 50;

        pedi_fil_init();

        //Time window variables
        pedi_bad_flag = 0;
        pedi_step_counter = 0;
    }

//convert the 2 hex values into int for easier processing

    private int updateStepParameter(int x, int y, int z, long timestamp) {

	/*Implemented*/
        //Save the 3-axis samples
        pedi_fil_update(x, y, z);

        //Find 3-axis max and min
        for (i = 0; i < 3; i++) {
            pedi_max[i] = pedi_axis_result[i] > pedi_max[i] ? pedi_axis_result[i] : pedi_max[i];
            pedi_min[i] = pedi_axis_result[i] < pedi_min[i] ? pedi_axis_result[i] : pedi_min[i];
        }

        //Sampling counter inc
        pedi_sampling_counter++;

        if (pedi_sampling_counter >= pedi_refresh_frequency) {
            //Reset Sampling
            pedi_sampling_counter = 0;

            //compute peak to peak value and dc value (dynamic threshold) for each axis
            pedi_p2p[X] = pedi_max[X] - pedi_min[X];
            pedi_p2p[Y] = pedi_max[Y] - pedi_min[Y];
            pedi_p2p[Z] = pedi_max[Z] - pedi_min[Z];

            pedi_ths[X] = (pedi_max[X] + pedi_min[X]) / 2;
            pedi_ths[Y] = (pedi_max[Y] + pedi_min[Y]) / 2;
            pedi_ths[Z] = (pedi_max[Z] + pedi_min[Z]) / 2;
        }

        //Check if new result exceeds precision
        //TBI

        //Update left-shift register
        pedi_old_fixed = pedi_new_fixed;
        pedi_new_fixed = pedi_result;

        //Find axis who's acceleration is largest i.e. highest max (alt. use p2p)
        axisIndex = pedi_max[X] > pedi_max[Y] ? X : Y;
        axisIndex = pedi_max[Z] > pedi_max[axisIndex] ? Z : axisIndex;

        //Set Threshold value
        pedi_threshold = pedi_ths[axisIndex];

        //Decide if a step was taken
        if (pedi_old_fixed > pedi_threshold && pedi_threshold > pedi_new_fixed) {
            updateTimeWindow(timestamp);
        }

        //Save the result of the highest axis
        pedi_result = pedi_axis_result[axisIndex];
        return pedi_step_counter;
    /*Tested*/
    }





}
