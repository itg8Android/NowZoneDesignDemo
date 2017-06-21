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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.sleep.adapter.ViewPagerSleepAdapter;

public class SleepActivity extends BaseSleepClass implements OnChartValueSelectedListener {

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViewPager();

        initBarChart();
    }

    private void initBarChart() {
        mChart.setOnChartValueSelectedListener(this);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setDrawBorders(false);
        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.WHITE);
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

        yVals1.add(new BarEntry(1,new float[]{1f,2f,3f}));
        yVals1.add(new BarEntry(2,new float[]{1f,3f,2f}));
        yVals1.add(new BarEntry(3,new float[]{2f,1f,3f}));
        yVals1.add(new BarEntry(4,new float[]{2f,3f,1f}));
        yVals1.add(new BarEntry(5,new float[]{3f,1f,2f}));
        yVals1.add(new BarEntry(6,new float[]{3f,2f,1f}));


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Statistics Vienna 2014");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Births", "Divorces", "Marriages"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
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

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }
}
