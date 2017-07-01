package itg8.com.nowzonedesigndemo.utility;

import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModel;



class AlgoAsync extends AsyncTask<List<DataModel>,Void,Integer> {


    private static final double DELTA = 2.0;
    private static final String TAG = AlgoAsync.class.getSimpleName();
    private static final long ONE_MINUTE = 60000;
    private PAlgoCallback callback;
    private int bpmInMinute;

    AlgoAsync(PAlgoCallback callback) {
        this.callback = callback;
    }

    @SafeVarargs
    @Override
    protected final Integer doInBackground(List<DataModel>... lists) {
        List<Map<Integer, Double>> data = CommonMethod.countBPM(lists[0], DELTA);
     //   Log.d(TAG,"size : "+data.size());
        if(data.size()>0) {
            int count= Math.max(data.get(0).size(),data.get(1).size());
            List<DataModel> listModel = lists[0];
            if (listModel.size() > 0) {
                long timeTaken = listModel.get(listModel.size() - 1).getTimestamp() - listModel.get(0).getTimestamp();
                if (timeTaken > 1000) {
                    bpmInMinute = (int) ((ONE_MINUTE * count) / timeTaken);
//                    callback.onCountResultAvailable(bpmInMinute, listModel.get(listModel.size() - 1).getTimestamp());
                    /**
                     * as we cakculated that a count in one minute is 2000. so we will directly send count
                     */
                    callback.onCountResultAvailable(count, listModel.get(listModel.size() - 1).getTimestamp());
//                    callback.onCountResultAvailable(count, listModel.get(listModel.size() - 1).getTimestamp());
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}