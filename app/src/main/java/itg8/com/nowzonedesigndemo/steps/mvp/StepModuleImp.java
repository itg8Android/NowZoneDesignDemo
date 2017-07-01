package itg8.com.nowzonedesigndemo.steps.mvp;

import com.j256.ormlite.dao.Dao;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import itg8.com.nowzonedesigndemo.common.BaseModuleOrm;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.utility.Helper;

/**
 * Created by itg_Android on 7/1/2017.
 */

public class StepModuleImp implements StepMVP.StepModule{

    private WeakReference<StepMVP.StepPresenterListener> listener;
    private Dao<TblStepCount, Integer> stepDao;

    @Override
    public void onListenerReady(StepMVP.StepPresenterListener listener) {
        this.listener = new WeakReference<StepMVP.StepPresenterListener>(listener);
    }

    @Override
    public void onDestroy() {
        listener=null;
    }

    @Override
    public void onDaoReady(Dao<TblStepCount,Integer> dao) {
        this.stepDao=dao;
        startCollectingData(dao);
    }

    private void startCollectingData(Dao<TblStepCount, Integer> dao) {
        /**
         * collect todays data
         */
        try {
            List<TblStepCount> counts=dao.query(dao.queryBuilder().where().eq(TblStepCount.FIELD_DATE, Calendar.getInstance().getTime()).prepare());
            if(counts.size()>0 && getListener())
            {
                TblStepCount count=counts.get(0);
                listener.get().onTodaysStepAvailable(count.getGoal(),count.getSteps(),getThisWeekTotal(dao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getThisWeekTotal(Dao<TblStepCount, Integer> dao) {
        int total=0;
        try {
            List<TblStepCount> counts =dao.query(dao.queryBuilder().where().between (TblStepCount.FIELD_DATE,"datetime('now', '-6 days')","datetime('now', 'localtime')").prepare());
            if(counts.size()>0){
                for (TblStepCount tblCount :
                        counts) {
                    total+=tblCount.getSteps();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private boolean getListener() {
        return listener != null;
    }
}
