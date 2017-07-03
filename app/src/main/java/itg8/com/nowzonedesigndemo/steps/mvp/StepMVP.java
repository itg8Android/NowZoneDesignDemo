package itg8.com.nowzonedesigndemo.steps.mvp;

import com.j256.ormlite.dao.Dao;

import java.util.List;

import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;

/**
 * Created by itg_Android on 7/1/2017.
 */

public class StepMVP {
    public interface StepView{
        void onTodaysStepAvailable(int goal, int covered, int weekTotal);
        void onWeekStep(List<WeekStepModel> weekStepModelList);
        void onMonthStep(List<MonthSteps> monthStepsList);
        void onDaoUnableToConnect(String message);
    }

    public interface StepPresenter{
        void onStart();
        void onStop();

        void onTodaysStepReady();

        void onWeekDayReady();
    }

    public interface StepPresenterListener{
        void onTodaysStepAvailable(int goal, int covered, int weekTotal);
        void onWeekStep(List<WeekStepModel> weekStepModelList);
        void onMonthStep(List<MonthSteps> monthStepsList);
        void onDaoError(String msg);
    }

    public interface StepModule{
        void onListenerReady(StepPresenterListener listener);
        void onDestroy();
        void onDaoReady(Dao<TblStepCount,Integer> dao);
        void onTodaysFragmentLoaded();

        void onWeeksFragmentLoaded();
    }

}
