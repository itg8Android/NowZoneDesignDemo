package itg8.com.nowzonedesigndemo.steps;


import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.txt_week)
    CustomFontTextView txtWeek;
    @BindView(R.id.img_graph)
    ImageView imgGraph;
    @BindView(R.id.txt_steps)
    CustomFontTextView txtSteps;
    @BindView(R.id.txt_steps_value)
    CustomFontTextView txtStepsValue;
    @BindView(R.id.linear_steps_count)
    LinearLayout linearStepsCount;
    @BindView(R.id.txt_time)
    CustomFontTextView txtTime;
    @BindView(R.id.ll_calories)
    LinearLayout llCalories;
    @BindView(R.id.chart)
    LineChartView chart;
    Unbinder unbinder;

    private final String[] mLabels = {"Mon", "Thu", "Wed", "Thus", "Fri", "Sat", "Sun"};

    private final float[] mValues = {2000f, 1800f, 2300f, 7000f, 1200f, 1010f, 2305f};
    @BindView(R.id.txt_cal)
    CustomFontTextView txtCal;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Tooltip mTip;


    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance() {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        LineSet dataset = new LineSet(mLabels, mValues);
//        for(int i=0; i<mLabels.length || i<mValues.length; i++){
//            dataset.addPoint(mLabels[i],mValues[i]);
//        }
        dataset.setSmooth(true);
        dataset.setColor(Color.WHITE);
        dataset.setDotsColor(Color.BLUE);


        Paint thresPaint = new Paint();
        thresPaint.setColor(Color.parseColor("#00ADFA"));
        thresPaint.setStyle(Paint.Style.STROKE);
        thresPaint.setAntiAlias(true);
        thresPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
        thresPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        chart.setValueThreshold(5000f, 5000f, thresPaint);

        chart.setAxisBorderValues(200f, 7000f, 2000);
        // Tooltip
        mTip = new Tooltip(getActivity(), R.layout.linechart_three_tooltip, R.id.value);

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(58), (int) Tools.fromDpToPx(25));
        mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

        mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);


        mTip.setPivotX(Tools.fromDpToPx(65) / 2);
        mTip.setPivotY(Tools.fromDpToPx(25));

        chart.addData(dataset);
        chart.setTooltips(mTip);
        chart.show();

        chart.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex, Rect rect) {
                chart.dismissAllTooltips();
                mTip.prepare(rect, mValues[entryIndex]);
                chart.showTooltip(mTip, true);
                showDetail(entryIndex);
            }
        });
        return view;
    }

    private void showDetail(int setIndex) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        txtStepsValue.setText(String.valueOf(formatter.format(mValues[setIndex])));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
