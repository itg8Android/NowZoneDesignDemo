package itg8.com.nowzonedesigndemo.breath_history;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.adapter.BreathsHistoryAdapter;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.utility.BreathState;

public class SleepDetailActivity extends AppCompatActivity implements BreathsHistoryAdapter.OnItemHistoryClickedListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.lbl_state)
    TextView lblState;
    @BindView(R.id.lbl_time)
    TextView lblTime;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.rl_appbar)
    RelativeLayout rlAppbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.nestedScrolling)
    NestedScrollView nestedScrolling;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private List<TblState> listState= new ArrayList<>();

    // Google Map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        prepareData();
        init();


    }

    private void init() {
        // cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_sleep));
        rlAppbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_sleep_half));
        appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_sleep_half));
        ImageView img = (ImageView) findViewById(R.id.img_icon);
        img.setTransitionName(getString(R.string.activity_image_trans));
        img.setBackground(ContextCompat.getDrawable(this, R.drawable.sleep));
        lblState.setText(getString(R.string.sleep));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));


    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new BreathsHistoryAdapter(getApplicationContext(), listState, this));
    }
    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        TblState tblState = null;
        for (int i = 0; i < 20; i++) {
            tblState = new TblState();
            tblState.setState(randomEnum(BreathState.class).name());
            tblState.setTimestampStart(calendar.getTimeInMillis());
            calendar.add(Calendar.MINUTE, 10);
            tblState.setTimestampEnd(calendar.getTimeInMillis());
            listState.add(tblState);
        }

        setRecyclerView();

    }
    private static <T extends Enum<?>> T randomEnum(Class<T> tClass) {
        Random random = new Random();
        int x = random.nextInt(tClass.getEnumConstants().length);
        return tClass.getEnumConstants()[x];
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int position, TblState listState) {

    }
}
