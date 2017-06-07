package itg8.com.nowzonedesigndemo.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.audio.AudioActivity;
import itg8.com.nowzonedesigndemo.common.BaseActivity;
import itg8.com.nowzonedesigndemo.home.mvp.BreathPresenter;
import itg8.com.nowzonedesigndemo.home.mvp.BreathPresenterImp;
import itg8.com.nowzonedesigndemo.home.mvp.BreathView;
import itg8.com.nowzonedesigndemo.sanning.ScanDeviceActivity;
import itg8.com.nowzonedesigndemo.sleep.SleepActivity;
import itg8.com.nowzonedesigndemo.steps.StepsActivity;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.widget.wave.BreathwaveView;
import itg8.com.nowzonedesigndemo.widget.wave.WaveLoadingView;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,BreathView {


    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_wave)
    FrameLayout rlWave;
    @BindView(R.id.img_breatch)
    ImageView imgBreatch;
    @BindView(R.id.txt_breathRate)
    TextView txtBreathRate;
    @BindView(R.id.txt_statusValue)
    TextView txtStatusValue;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.breathValue)
    TextView breathValue;
    @BindView(R.id.txt_minute)
    TextView txtMinute;
    @BindView(R.id.rl_breath)
    RelativeLayout rlBreath;
    @BindView(R.id.txt_calm)
    TextView txtCalm;
    @BindView(R.id.txt_calm_value)
    TextView txtCalmValue;
    //    @BindView(R.id.txt_calm_time)
//    TextView txtCalmTime;
    @BindView(R.id.txt_focus_value)
    TextView txtFocusValue;
    //    @BindView(R.id.txt_focus_time)
//    TextView txtFocusTime;
    @BindView(R.id.txt_stress_value)
    TextView txtStressValue;
    //    @BindView(R.id.txt_stress_time)
//    TextView txtStressTime;
    @BindView(R.id.rl_main_top)
    RelativeLayout rlMainTop;
    @BindView(R.id.img_breath)
    ImageView imgBreath;
    @BindView(R.id.txt_breath)
    TextView txtBreath;
    @BindView(R.id.txt_breathCount)
    TextView txtBreathCount;
    @BindView(R.id.txt_breathValue)
    TextView txtBreathValue;
    @BindView(R.id.img_sleep)
    ImageView imgSleep;
    @BindView(R.id.txt_forth)
    TextView txtForth;
    @BindView(R.id.txt_hour)
    TextView txtHour;
    @BindView(R.id.txt_hourValue)
    TextView txtHourValue;
    @BindView(R.id.ll_sleep_main)
    LinearLayout llSleepMain;
    @BindView(R.id.img_step)
    ImageView imgStep;
    @BindView(R.id.txt_step)
    TextView txtStep;
    @BindView(R.id.txt_stepCount)
    TextView txtStepCount;
    @BindView(R.id.txt_stepCountValue)
    TextView txtStepCountValue;
    @BindView(R.id.rlSteps)
    LinearLayout rlSteps;
    @BindView(R.id.rl_main_bottom)
    RelativeLayout rlMainBottom;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.waveLoadingView)
    WaveLoadingView waveLoadingView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.breathview)
    BreathwaveView breathview;
    @BindView(R.id.txt_focus)
    CustomFontTextView txtFocus;
    @BindView(R.id.txt_stress)
    TextView txtStress;
    @BindView(R.id.main_FrameLayout)
    FrameLayout mainFrameLayout;

    BreathPresenter presenter;


    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        Timber.tag(TAG);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        presenter=new BreathPresenterImp(this);
        presenter.passContext(this);
        presenter.onCreate();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);


        toggle.syncState();
        drawer.addDrawerListener(toggle);
        rlSteps.setOnClickListener(this);
        llSleepMain.setOnClickListener(this);

        setType();
        setAnimator();

        waveLoadingView.setWaveBgColor(Color.parseColor("#24006bb7"));
        waveLoadingView.setBorderColor(Color.parseColor("#27BEFB"));


//        setFontOxygenRegular(FontType.ROBOTOlIGHT, txtBreathRate, txtStatus, txtMinute, txtStatusValue, breathValue);
//        setFontOpenSansSemiBold(FontType.ROBOTOlIGHT, txtCalm, txtCalmValue, txtStress, txtStressValue, txtFocus,  txtFocusValue);

    }


    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void setType() {
        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.SQUARE);
        waveLoadingView.setAmplitudeRatio(50);
        waveLoadingView.setProgressValue(50);
    }

    /**
     * this is set by me
     */
    private void setAnimator() {

        //mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        // Sets the length of the animation, default is 1000.
        waveLoadingView.setAnimDuration(1000);
        waveLoadingView.startAnimation();
        //  waveLoadingView.cancelAnimation();
        // waveLoadingView.resumeAnimation();
        //                    waveLoadingView.pauseAnimation();


    }

    @Override
    protected void onResume() {
        if(breathview!=null)
            breathview.reset();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AudioActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlSteps:
                Intent intent = new Intent(this, StepsActivity.class);
                overridePendingTransition(R.animator.slid_up, R.animator.slid_down);
                startActivity(intent);
                break;
            case R.id.ll_sleep_main:
                startActivity(new Intent(this, SleepActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_breath:
                break;
            case R.id.nav_sleep:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onPressureDataAvail(double pressure) {
        breathview.addSample(SystemClock.elapsedRealtime(), pressure);
    }

    @Override
    public void onDeviceConnected() {
        Log.d(TAG,"Device connected");
    }

    @Override
    public void onDeviceDisconnected() {
        Log.d(TAG,"Device disconnected");
    }

    @Override
    public void onBreathCountAvailable(int intExtra) {
        Log.d(TAG,"Breath count: "+intExtra);
        breathValue.setText(String.valueOf(intExtra));
    }

    @Override
    public void onStepCountReceived(int intExtra) {
        Log.d(TAG,"Step count: "+intExtra);
    }

    @Override
    public void onStartDeviceScanActivity() {
        Timber.i("Start device activity");
        startActivity(new Intent(this, ScanDeviceActivity.class));
    }
}
