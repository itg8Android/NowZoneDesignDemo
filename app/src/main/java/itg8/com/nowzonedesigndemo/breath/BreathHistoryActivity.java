package itg8.com.nowzonedesigndemo.breath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath.mvp.BreathHistoryMVP;
import itg8.com.nowzonedesigndemo.breath.mvp.BreathHistoryPresenterImp;
import itg8.com.nowzonedesigndemo.breath.timeline.InMemoryCursor;
import itg8.com.nowzonedesigndemo.breath.timeline.TimelineChartView;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;


public class BreathHistoryActivity extends AppCompatActivity implements BreathHistoryMVP.BreathHistoryView, View.OnClickListener {

    BreathHistoryMVP.BreathHistoryPresenter presenter;
    //
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerview;

    //
//    @BindView(R.id.frameLayout)
//    FrameLayout frameLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.viewPager)
//    ViewPager viewPager;
    @BindView(R.id.graph)
    TimelineChartView graph;
    @BindView(R.id.card_calm)
    CardView cardCalm;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rl_calm)
    RelativeLayout rlCalm;
    @BindView(R.id.rl_focus)
    RelativeLayout rlFocus;
    @BindView(R.id.rl_stress)
    RelativeLayout rlStress;
    @BindView(R.id.lbl_calm)
    CustomFontTextView lblCalm;
    @BindView(R.id.txt_calm_value)
    CustomFontTextView txtCalmValue;
    @BindView(R.id.lbl_calm_time)
    CustomFontTextView lblCalmTime;
    @BindView(R.id.lbl_focus)
    CustomFontTextView lblFocus;
    @BindView(R.id.txt_focus_value)
    CustomFontTextView txtFocusValue;
    @BindView(R.id.lbl_focus_time)
    CustomFontTextView lblFocusTime;
    @BindView(R.id.lbl_stress)
    CustomFontTextView lblStress;
    @BindView(R.id.txt_stress_value)
    CustomFontTextView txtStressValue;
    @BindView(R.id.lbl_stress_time)
    CustomFontTextView lblStressTime;


    private Calendar mStart;

    private final SimpleDateFormat DATETIME_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    // new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final NumberFormat NUMBER_FORMATTER = new DecimalFormat("#0.00");
    private final String[] COLUMN_NAMES = {"timestamp", "Calm", "Focus", "Stress"};
    private TextView[] mSeries;
    private View[] mSeriesColors;

    private final int[] MODES = {
            TimelineChartView.GRAPH_MODE_BARS,
            TimelineChartView.GRAPH_MODE_BARS_STACK,
            TimelineChartView.GRAPH_MODE_BARS_SIDE_BY_SIDE};
    private final String[] MODES_TEXT = {
            "GRAPH_MODE_BARS",
            "GRAPH_MODE_BARS_STACK",
            "GRAPH_MODE_BARS_SIDE_BY_SIDE"};


    private static final int LIVE_UPDATE_INTERVAL = 2;
    private Handler mHandler;
    private InMemoryCursor mCursor;

//    private final Runnable mLiveUpdateTask = new Runnable() {
//        @Override
//        public void run() {
//            mStart.setTimeInMillis(System.currentTimeMillis());
//            mStart.set(Calendar.DATE, 0);
//          //  mCursor.add(createItem(mStart.getTimeInMillis()));
//            mHandler.postDelayed(this, LIVE_UPDATE_INTERVAL * 1000);
//        }
//    };

    private List<TblState> listState = new ArrayList<>();
    private int[] mColor= new int[]{Color.parseColor("#81C784"), Color.parseColor("#64B5F6"), Color.parseColor("#E57373")};

    private Object[] createItem(long timeInMillis) {

        Object[] item = new Object[COLUMN_NAMES.length];
        item[0] = timeInMillis;
        for (int i = 1; i < COLUMN_NAMES.length; i++) {
            item[i] = random(9999);
        }
        return item;

    }
//    private Object[] createItem(Date timeInMillis) {
//
//            Object[] item = new Object[COLUMN_NAMES.length];
//            item[0] = timeInMillis;
//            for (int i = 1; i < COLUMN_NAMES.length; i++) {
//                item[i] = random(9999);
//            }
//            return item;
//
//    }

    private int random(int max) {
        return (int) (Math.random() * (max + 1));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_history);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        presenter = new BreathHistoryPresenterImp(this);
        presenter.initListOfState();
        init();

//        FragmentManager fm = getSupportFragmentManager();
//        viewPager.setAdapter(new BreathPagerAdapter(getApplicationContext(),fm));
//fm.beginTransaction().replace(R.id.frameLayout, new BreathHistoryFragment(), getClass().getSimpleName()).commit();


    }

    private void checkScreenDensity(Context context, TextView... textView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                for (TextView text : textView
                        ) {
                    text.setTextSize(15);

                }
                //textView.setTextSize(R.dimen.lbl_breath_value);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                //textView.setTextSize(R.dimen.lbl_breath_value);
                for (TextView text : textView
                        ) {
                    text.setTextSize(18);
                }


                break;
            case DisplayMetrics.DENSITY_HIGH:
                //  textView.setTextSize(R.dimen.lbl_breath_value);
                for (TextView text : textView
                        ) {

                    text.setTextSize(20);
                }
                break;
        }
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkScreenDensity(getApplicationContext(),lblCalm, lblCalmTime, lblFocus, lblFocusTime, lblStress, lblStressTime, txtCalmValue, txtFocusValue, txtStressValue );

        rlCalm.setOnClickListener(this);
        rlFocus.setOnClickListener(this);
        rlStress.setOnClickListener(this);
        initGraphWithTimeline();

    }


    private void initGraphWithTimeline() {
        graph.setBarItemSpace(8);
        graph.setBarItemWidth(133);
        graph.setFollowCursorPosition(false);
        graph.animate();
        graph.setGraphMode(1);
        graph.setGraphAreaBackground(Color.TRANSPARENT);

        // graph.setUserPalette(new int[]{Color.parseColor("#388E3C"),Color.parseColor("#1976D2"),Color.parseColor("#EF5350")});
        //graph.setUserPalette(new int[]{Color.parseColor("#81C784"), Color.parseColor("#64B5F6"), Color.parseColor("#F8BBD0")});
        graph.setUserPalette(new int[]{Color.parseColor("#81C784"), Color.parseColor("#64B5F6"), Color.parseColor("#E57373")});
        //E53935
        mCursor = createInMemoryCursor();
       // createRandomData(mCursor);
        graph.observeData(mCursor);

        graph.setOnClickItemListener(new TimelineChartView.OnClickItemListener() {
            @Override
            public void onClickItem(TimelineChartView.Item item, int serie) {
                final String timestamp = DATETIME_FORMATTER.format(item.mTimestamp);
                System.out.println(timestamp);
                Log.d(getClass().getSimpleName(), "TimesStamped:" + timestamp);
                graph.smoothScrollTo(item.mTimestamp);
            }
        });

        setUpCalender();


    }

    private InMemoryCursor createInMemoryCursor() {
        InMemoryCursor cursor = new InMemoryCursor(COLUMN_NAMES);
        createRandomData(cursor);
        return cursor;

    }

    private void setUpCalender() {
        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup series = (ViewGroup) findViewById(R.id.item_series);

        mSeries = new TextView[COLUMN_NAMES.length - 1];
        mSeriesColors = new View[COLUMN_NAMES.length - 1];
        for (int i = 1; i < COLUMN_NAMES.length; i++) {
            View v = inflater.inflate(R.layout.serie_item_layout, series, false);
            TextView title = (TextView) v.findViewById(R.id.title);
            //title.setText(getString(R.string.item_name, COLUMN_NAMES[i]));
            title.setText(COLUMN_NAMES[i]);
            title.setTextColor(Color.WHITE);
            mSeries[i - 1] = (TextView) v.findViewById(R.id.value);
            mSeriesColors[i - 1] = v.findViewById(R.id.color);
            mSeriesColors[i - 1].setBackgroundColor(mColor[i-1]);
            series.addView(v);
        }

    }

    private void createRandomData(InMemoryCursor cursor) {
        List<Object[]> data = new ArrayList<>();
        Calendar today = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        //Calendar today = Calendar.getInstance( Locale.getDefault());
//        today.set(Calendar.HOUR_OF_DAY, 0);
//        today.set(Calendar.MINUTE, 0);
//        today.set(Calendar.SECOND, 0);
//        today.set(Calendar.MILLISECOND, 0);
        //today.set(Calendar.HOUR_OF_DAY, 24);
//        today.set(Calendar.HOUR, 0);
//        today.set(Calendar.SECOND, 0);
        today.set(Calendar.DAY_OF_MONTH, 0);
      //  mStart = (Calendar) today.clone();
        mStart = Calendar.getInstance();
        mStart.add(Calendar.DAY_OF_MONTH, -30);
        while (mStart.compareTo(today) <= 0) {
            data.add(createItem(mStart.getTimeInMillis()));
            mStart.add(Calendar.DAY_OF_MONTH, 1);
        }
        mStart.add(Calendar.DAY_OF_MONTH, -1);
        cursor.addAll(data);


    }


    @Override
    public void onListAvailable(List<TblState> list) {
        this.listState = list;


    }

    private void setRecyclerView(List<TblState> list) {
        BreathHistoryAdapter adapter = new BreathHistoryAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerview.setLayoutManager(layoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(),
//                layoutManager.getOrientation());
//        recyclerview.addItemDecoration(dividerItemDecoration);
//        recyclerview.setAdapter(adapter);

    }

    @Override
    public void onErrorLoading(String error) {

    }

    @Override
    public void onConnectionFailed(String issue) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_calm:
                callHistoryActivity();
                break;
            case R.id.rl_focus:
                callHistoryActivity();
                break;
            case R.id.rl_stress:
                callHistoryActivity();
                break;
        }
    }

    private void callHistoryActivity() {
        Intent intent = new Intent(getApplicationContext(), BreathHistoryListActivity.class);
        intent.putParcelableArrayListExtra(CommonMethod.BREATH, (ArrayList<? extends Parcelable>) listState);
        startActivity(intent);
    }
}
