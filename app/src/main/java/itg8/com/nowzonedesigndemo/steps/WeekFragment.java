package itg8.com.nowzonedesigndemo.steps;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.IHighlighter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.steps.widget.CustomMarkerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment  {
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
    LineChart chart;
    Unbinder unbinder;

    private final String[] mLabels = {"Mon", "Thu", "Wed", "Thus", "Fri", "Sat", "Sun"};
    private final float[] entryValues = {2000f, 1800f, 2300f, 7000f, 1200f, 1010f, 2305f};
 //   final String[] quarters = new String[] { "Mon", "Tue", "Wed", "Thus","Fri","Sat","Sun" };



    private final List<Entry> mValues =
            new ArrayList<>();

    @BindView(R.id.txt_cal)
    CustomFontTextView txtCal;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   // public Tooltip mTip;


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
        final View view = inflater.inflate(R.layout.fragment_week, container, false);
        unbinder = ButterKnife.bind(this, view);



        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        LimitLine ll1 = new LimitLine(6000f, " ");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setLineColor(getResources().getColor(R.color.color_green_dark));

        ll1.setTextSize(10f);
        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setXEntrySpace(5f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
        LegendEntry entry = new LegendEntry ("Threshold", Legend.LegendForm.LINE,10f,3f,new DashPathEffect(new float[]{10f, 5f},0f),getResources().getColor(R.color.color_green_dark));
        List<LegendEntry> entries = new ArrayList<>();
        entries.add(entry);
        l.setCustom(entries);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setAxisLineColor(Color.WHITE);



        YAxis yAxisL = chart.getAxisLeft();
        YAxis yAxisR = chart.getAxisRight();
        yAxisL.setAxisLineColor(Color.WHITE);
        yAxisL.setTextColor(Color.WHITE);
        yAxisR.setEnabled(false);
        yAxisL.setDrawAxisLine(true);
        yAxisL.setDrawGridLines(false);
        yAxisL.setXOffset(20f);
        yAxisL.addLimitLine(ll1);
        yAxisL.setEnabled(true);
        yAxisL.setAxisLineWidth(1f);
        yAxisL.setAxisLineColor(Color.WHITE);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mLabels[(int) value-1];
            }

            // we don't draw numbers, so no decimal digits needed

            public int getDecimalDigits() {  return 0; }
        };


        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        setData();

        return view;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<Entry>();
        int i = 1;
        for (Float val : entryValues) {
            values.add(new Entry(i,val));
                i++;
            }
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        }
            else
            {
                set1 = new LineDataSet(values, " ");
                set1.setLineWidth(3f);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);


                set1.setCircleRadius(5f);
                set1.setCircleHoleRadius(4f);
                set1.setCircleColorHole(Color.TRANSPARENT);
                set1.setValueTextColor(Color.TRANSPARENT);
                set1.setHighLightColor(getActivity().getResources().getColor(android.R.color.transparent));
                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets
                LineData data = new LineData(dataSets);
                CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.linechart_three_tooltip);
                mv.setChartView(chart);
                mv.setPadding(0,10,0,0);
                chart.setMarkerView(mv);
                chart.setData(data);

            }
    }


}
