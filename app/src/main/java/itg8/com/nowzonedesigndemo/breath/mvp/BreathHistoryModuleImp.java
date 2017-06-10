package itg8.com.nowzonedesigndemo.breath.mvp;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.DBModule;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import timber.log.Timber;

/**
 * Created by itg_Android on 6/10/2017.
 */

public class BreathHistoryModuleImp extends DBModule implements BreathHistoryMVP.BreathHostoryModule {

    public boolean dbInit;
    private boolean stateDaoInit=false;

    public BreathHistoryModuleImp(Context context) {
        super(context);
        onRequestStateDao();
    }

    @Override
    public void onBreathHistoryListener(BreathHistoryMVP.BreathHistoryListener listener) {
        this.listener=listener;
        if(stateDaoInit)
            createObserver();
    }

    private List<TblState> listAllDataFrom() {

        return null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void initStateDao() {
        stateDaoInit=true;
    }

    @Override
    public void initStateDao(boolean b, String issue) {
        listener.onConnectionFailed(issue);
    }

    private void createObserver() {
        Observable.create((ObservableOnSubscribe<List<TblState>>) e -> {
            e.onNext(listAllDataFrom());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(o -> listener.onListAvailable(o), Throwable::printStackTrace,() -> Timber.d("CREATED"));
    }
}
