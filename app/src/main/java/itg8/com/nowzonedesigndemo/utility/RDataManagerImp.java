package itg8.com.nowzonedesigndemo.utility;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModel;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.tosort.RDataManager;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;

/**
 * This is responsible for calculation uploading and all.
 */

public class RDataManagerImp implements RDataManager, PAlgoCallback,AccelVerifyListener {

    /**
     * this veriable use to define rolling average window. W=30
     */
    private static final int ROLLING_AVG_SIZE = 30;

    /**
     * this will check if packet receiving is completed.
     */
    private static final int PACKET_READY_TO_IMP = 2000;
    private static final String REMINDER_JOB_TAG = RDataManagerImp.class.getCanonicalName();
    private static final int REMINDER_INTERVAL_SECONDS = 1000;
    private static final int SYNC_FLEXTIME_SECONDS = 3000;
    private static final String TAG = RDataManagerImp.class.getSimpleName();
    private final RDataManagerListener listener;
    private final Observer<DataModel> observer;
    List<DataModel> dataStorage = new ArrayList<>();
    Job dataToFileJob;
    CheckAccelImp accelImp;
    /**
     * this will be use to send data from service to store in file.
     */
    FirebaseJobDispatcher dispatcher;
    private Rolling rolling, rolling2;
    private Observable<String> observable;

    public RDataManagerImp(RDataManagerListener listener) {
        this.listener = listener;
        rolling = new Rolling(ROLLING_AVG_SIZE);
        rolling2 = new Rolling(ROLLING_AVG_SIZE);

        accelImp=new CheckAccelImp(this);
        observer= new Observer<DataModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DataModel dataModel) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

//        Subscription subscription = observable
//                .subscribeOn(Schedulers.io())       //observable will run on IO thread.
//                .observeOn(AndroidSchedulers.mainThread())      //Observer will run on main thread.
//                .subscribe(observer);

    }

    @Override
    public void onRawDataModel(DataModel model, Context context) {
        if (model != null) {
            Log.d(RDataManagerImp.class.getSimpleName(), "data received:" + model.getPressure());
            observable=Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                }
            });
            /**
             * Currently we are working on pressure
             */
       //     processForStepCounting(model);

            processModelData(model, context);
        } else {
            Log.d(RDataManagerImp.class.getSimpleName(), "data received: model is null");
        }
    }

    private void processForStepCounting(DataModel model) {
            //TODO We need to check step and activity in this method
            accelImp.onModelAvail(model);
    }

    private void processModelData(DataModel model, Context context) {
        rolling.add(model.getPressure());
        model.setPressure(rolling.getaverage());
        listener.onDataProcessed(model);
        rolling2.add(rolling.getaverage());
        model.setPressure(rolling2.getaverage());
        checkIfDataGatheringCompleted(model, context);
    }

    private synchronized void checkIfDataGatheringCompleted(DataModel model, Context context) {
        dataStorage.add(model);
       // Log.d(TAG,"Size of dataStorage "+dataStorage.size());
        if (dataStorage.size() == PACKET_READY_TO_IMP) {
            Log.d(TAG,"datas  is greater");
            implementStorageProcess(context, dataStorage);
        }
    }

    List<DataModel> tempHolder=new ArrayList<>();

    private void implementStorageProcess(Context context, List<DataModel> dataStorage) {
        tempHolder.clear();
        tempHolder.addAll(dataStorage);
        resetDataStorage(this.dataStorage);
        //We will do that after SAAS TODO SAAS
//        passForFIleStorage(tempHolder, context);

        passForCalculation(tempHolder);

    }

    private void passForFIleStorage(List<DataModel> dataStorage, Context context) {
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        /**
         * Currently we are haulting this TODO uncomment after complete implementaion of breath count
         */
//        FileAsync async=new FileAsync(SharePrefrancClass.getInstance(context).getPref(CommonMethod.STORAGE_PATH),context.getFilesDir().list());
//        async.execute(dataStorage);

//        Bundle bundle=new Bundle();
//        bundle.putString(CommonMethod.FINISHED_DATA, new Gson().toJson(dataStorage));
//        dataToFileJob = dispatcher.newJobBuilder()
//                .setService(ServiceOnCheck.class)
//                .setTag(REMINDER_JOB_TAG)
//                .setLifetime(Lifetime.FOREVER)
//                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(
//                      1,2
//                ))
//                .setExtras(bundle)
//                .setReplaceCurrent(true)
//                .build();

//        dispatcher.schedule(dataToFileJob);
    }

    private void passForCalculation(List<DataModel> dataStorage) {
        Log.d(TAG,"came for calculation");
        AlgoAsync async = new AlgoAsync(this);
        async.execute(dataStorage);
    }

    private void resetDataStorage(List<DataModel> dataStorage) {
        dataStorage.clear();
    }

    @Override
    public void onCountResultAvailable(int count, long timestamp) {
        Log.d(TAG,"bpmCount = "+count);
        listener.onCountAvailable(count, timestamp);
    }

    @Override
    public void onCountFail() {

    }

    @Override
    public void onMotionStarts() {
        Log.d(TAG,"motion: started");
    }

    @Override
    public void onMotionEnds() {
        Log.d(TAG,"motion: finish");
    }

    @Override
    public void onStep(int step) {
    //    Log.d(TAG,"Step count: "+step);
        listener.onStepCountReceived(step);
    }
}
