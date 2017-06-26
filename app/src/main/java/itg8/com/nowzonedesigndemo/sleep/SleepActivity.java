package itg8.com.nowzonedesigndemo.sleep;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.sleep.adapter.ViewPagerSleepAdapter;

public class SleepActivity extends BaseSleepClass implements OnChartValueSelectedListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.txt_day)
    TextView txtDay;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.barchart)
    BarChart mChart;
    @BindView(R.id.txt_sleepTime)
    TextView txtSleepTime;
    @BindView(R.id.txt_sleepValue)
    TextView txtSleepValue;
    @BindView(R.id.txt_sleepEndValue)
    TextView txtSleepEndValue;
    @BindView(R.id.txt_sleepEndTime)
    TextView txtSleepEndTime;
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;

    private final String[] mLabels = {"Deep", "Awake", "Light"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if(getSupportActionBar()!=null) {
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        imgLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        setUpViewPager();

       // initBarChart();
         initPositiveNegativeChart();
    }

    private void initPositiveNegativeChart() {
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setExtraTopOffset(-30f);
        mChart.setExtraBottomOffset(10f);
        mChart.setExtraLeftOffset(70f);
        mChart.setExtraRightOffset(70f);
        mChart.setDrawValueAboveBar(true);
        mChart.getLegend().setEnabled(false);

        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
       // mChart.animateY(3000);


        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setDrawBorders(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(4);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(true);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        left.setTextColor(Color.WHITE);
        left.setTextSize(12f);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mLabels[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed

            public int getDecimalDigits() {
                return 0;
            }
        };


        left.setGranularity(1f);
        left.setValueFormatter(formatter);


        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.setFitBars(true);
        mChart.setHighlightPerTapEnabled(false);



        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();
        data.add(new Data(0f, -1538.1f, "11-1"));
        data.add(new Data(1f, 2338.5f, "1-3"));
        data.add(new Data(2f, -2438.1f, "3-5"));
        data.add(new Data(3f, 50f, "5-6"));
        data.add(new Data(4f, -2238.1f, "6-8"));

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return data.get(Math.min(Math.max((int) value, 0), data.size()-1)).xAxisValue;
            }
        });

        setData(data);
    }

    private void initBarChart() {
//        mChart.setOnChartValueSelectedListener(this);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);


        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.animateY(3000);


        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setDrawBorders(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.getLegend().setEnabled(false);


        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.TRANSPARENT);
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setDrawGridLines(false);
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setTextColor(Color.WHITE);


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);


        /**
         * check for round corner
         */

        onChartReadyToFillData();

    }

    private void setUpViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new ViewPagerSleepAdapter(this, fm));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChartReadyToFillData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

//        for (int i = 0; i < mSeekBarX.getProgress() + 1; i++) {
//            float mult = (mSeekBarY.getProgress() + 1);
//            float val1 = (float) (Math.random() * mult) + mult / 3;
//            float val2 = (float) (Math.random() * mult) + mult / 3;
//            float val3 = (float) (Math.random() * mult) + mult / 3;
//
//            yVals1.add(new BarEntry(
//                    i,
//                    new float[]{val1, val2, val3},
//                    getResources().getDrawable(R.drawable.star)));
//        }

        yVals1.add(new BarEntry(1,new float[]{3f}));
        yVals1.add(new BarEntry(2,new float[]{3f}));
        yVals1.add(new BarEntry(3,new float[]{3f}));
        yVals1.add(new BarEntry(4,new float[]{3f}));
        yVals1.add(new BarEntry(5,new float[]{3f}));
        yVals1.add(new BarEntry(6,new float[]{3f}));


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setValueTextSize(Color.WHITE);


           // set1.setStackLabels(new String[]{"Awake", "Light", "Deep"});
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(1f);

//            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private int[] getColors() {

        int stacksize = 6;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];
//            <color name="colorGreen"># 00CBAC</color>
//    <color name="colorBlue">#00B0EC</color>
//    <color name="colorOrange">#D4A280</color>

        colors[0] = Color.parseColor("#D4A280");
        colors[1] = Color.parseColor("#00B0EC");
        colors[2] = Color.parseColor("#00CBAC");
        colors[3] = Color.parseColor("#D4A280");
        colors[4] = Color.parseColor("#00B0EC");
        colors[5] = Color.parseColor("#00CBAC");

//        for (int i = 0; i < colors.length; i++) {
//            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
//
//        }
        return colors;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_left:
                break;

            case R.id.img_right:
                break;
        }
    }

    private class Data {

        public String xAxisValue;
        public float yValue;
        public float xValue;

        public Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();

        int blue = Color.parseColor("#00B0EC");
                //rgb(110, 190, 102);
        int blueLight =  Color.parseColor("#7f6ecded");
        int awakeColor =  Color.parseColor("#F39CDEF4");

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(blue);
            else
                colors.add(blueLight);

             if(d.yValue>=0 && d.yValue<=100)
                colors.add(awakeColor);
        }

        BarDataSet set;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColor(Color.TRANSPARENT);

            BarData data = new BarData(set);
            data.setValueTextSize(13f);
            //data.setValueFormatter(new ValueFormatter());
           // data.setBarWidth(0.8f);
            data.setBarWidth(1f);

            mChart.setData(data);
            mChart.invalidate();
        }
    }


}
