package itg8.com.nowzonedesigndemo.breath;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import itg8.com.nowzonedesigndemo.db.tbl.TblState;


public class BreathHistoryActivity extends AppCompatActivity implements BreathHistoryMVP.BreathHistoryView, SeekBar.OnSeekBarChangeListener {

    BreathHistoryMVP.BreathHistoryPresenter presenter;
    //
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerview;

    //
//    @BindView(R.id.frameLayout)
//    FrameLayout frameLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.graph)
    TimelineChartView graph;
    @BindView(R.id.seekbar_r)
    SeekBar seekbarR;
    @BindView(R.id.seekbar_g)
    SeekBar seekbarG;
    @BindView(R.id.seekbar_b)
    SeekBar seekbarB;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    private Calendar mStart;

    private final SimpleDateFormat DATETIME_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    // new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final NumberFormat NUMBER_FORMATTER = new DecimalFormat("#0.00");
    private final String[] COLUMN_NAMES = {"timestamp", "Serie 1", "Serie 2", "Serie 3"};
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
    private int mMode;
    private int mSound;
    private boolean mInLiveUpdate;

    private static final int LIVE_UPDATE_INTERVAL = 2;
    private Handler mHandler;
    private InMemoryCursor mCursor;

    private final Runnable mLiveUpdateTask = new Runnable() {
        @Override
        public void run() {
            mStart.setTimeInMillis(System.currentTimeMillis());
            mStart.set(Calendar.MILLISECOND, 0);
//            mCursor.add(createItem(mStart.getTimeInMillis()));
            mHandler.postDelayed(this, LIVE_UPDATE_INTERVAL * 1000);
        }
    };
    private int seekB;
    private int seekR;
    private int seekG;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initGraphWithTimeline();
//        FragmentManager fm = getSupportFragmentManager();
//        viewPager.setAdapter(new BreathPagerAdapter(getApplicationContext(),fm));
//// fm.beginTransaction().replace(R.id.frameLayout, new BreathHistoryFragment(), getClass().getSimpleName()).commit();

        presenter = new BreathHistoryPresenterImp(this);
        presenter.initListOfState();

    }

    private void initGraphWithTimeline() {
        graph.setBarItemSpace(8);
        graph.setBarItemWidth(133);
        graph.setFollowCursorPosition(false);
        graph.animate();
        graph.setGraphMode(1);
        graph.setGraphAreaBackground(Color.TRANSPARENT);
        seekbarB.setOnSeekBarChangeListener(this);
        seekbarR.setOnSeekBarChangeListener(this);
        seekbarG.setOnSeekBarChangeListener(this);
        seekbarB.setMax(255);
        seekbarG.setMax(255);
        seekbarR.setMax(255);




        graph.setUserPalette(new int[]{Color.parseColor("#bc2196f3"), Color.parseColor("#FF7DF282"), Color.parseColor("#65f57e58")});

        mSound = graph.isPlaySelectionSoundEffect() ? 1 : 0;
        mSound += graph.getSelectionSoundEffectSource() != 0 ? 1 : 0;
        mCursor = createInMemoryCursor();
        createRandomData(mCursor);
        graph.observeData(mCursor);
        // Create random data
//        graph.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(getClass().getSimpleName(),"TimesStamped:");
//            }
//        });
//


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
            title.setText(getString(R.string.item_name, COLUMN_NAMES[i]));
            title.setTextColor(Color.WHITE);
            mSeries[i - 1] = (TextView) v.findViewById(R.id.value);
            mSeries[i - 1].setText("-");
            mSeriesColors[i - 1] = v.findViewById(R.id.color);
            mSeriesColors[i - 1].setBackgroundColor(Color.WHITE);
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
        mStart = (Calendar) today.clone();
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
         switch (seekBar.getId())
         {
             case R.id.seekbar_b:
                 seekB= progress;
                 break;
             case R.id.seekbar_r:
                 seekR= progress;
                 break;
             case R.id.seekbar_g:
                 seekG = progress;
                 break;
         }
        doSomethingWithColor();
    }
    private void doSomethingWithColor() {
        int color = Color.rgb(seekR, seekG, seekB);
        graph.setUserPalette(new int[]{color,Color.parseColor("#AF729F"), Color.parseColor("#65f57e58")});
        Log.d(getClass().getSimpleName(),"Colors:"+color);

        //11498143 for calm
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
