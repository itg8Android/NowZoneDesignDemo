package itg8.com.nowzonedesigndemo.steps;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.steps.stikyheader.StickyRecyclerHeadersAdapter;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

import static itg8.com.nowzonedesigndemo.R.id.textView;

/**
 * Created by Android itg 8 on 5/25/2017.
 */

public class StepsHeaderAdapter extends StepsContentsAdapter<StepsHeaderAdapter.MyViewContentHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {


    private static final String TAG = StepsHeaderAdapter.class.getSimpleName();

    @Override
    public MyViewContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_history_sleep, parent, false);
        MyViewContentHolder holder = new MyViewContentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewContentHolder holder, int position) {
        if (holder instanceof MyViewContentHolder) {

     //  holder.txtMilesValue.setText(getItem(position));

        }
    }

    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return 1;
//            return -1;
        } else {
            return getItem(position).charAt(0);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_header_steps, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        Log.d(TAG,"onBindHeader:"+String.valueOf(getItem(position)));
        textView.setText(String.valueOf(getItem(position)));
      //  holder.itemView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }

    public class MyViewContentHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.txt_miles_value)
        CustomFontTextView txtMilesValue;
        @BindView(R.id.ll_miles)
        RelativeLayout llMiles;
        @BindView(R.id.txt_goals)
        CustomFontTextView txtGoals;
        @BindView(R.id.txt_golas_value)
        CustomFontTextView txtGolasValue;
        @BindView(R.id.txt_avg)
        CustomFontTextView txtAvg;
        @BindView(R.id.txt_avg_value)
        CustomFontTextView txtAvgValue;

        public MyViewContentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

}