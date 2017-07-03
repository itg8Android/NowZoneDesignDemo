package itg8.com.nowzonedesigndemo.home;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.alarm.AlarmActivity;
import itg8.com.nowzonedesigndemo.audio.AudioActivity;
import itg8.com.nowzonedesigndemo.breath.BreathHistoryActivity;
import itg8.com.nowzonedesigndemo.common.BaseActivity;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.connection.BleService;
import itg8.com.nowzonedesigndemo.home.mvp.BreathPresenter;
import itg8.com.nowzonedesigndemo.home.mvp.BreathPresenterImp;
import itg8.com.nowzonedesigndemo.home.mvp.BreathView;
import itg8.com.nowzonedesigndemo.home.mvp.StateTimeModel;
import itg8.com.nowzonedesigndemo.profile.ProfileActivity;
import itg8.com.nowzonedesigndemo.sanning.ScanDeviceActivity;
import itg8.com.nowzonedesigndemo.setting.AlarmSettingActivity;
import itg8.com.nowzonedesigndemo.sleep.SleepActivity;
import itg8.com.nowzonedesigndemo.steps.StepsActivity;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.Rolling;
import itg8.com.nowzonedesigndemo.widget.wave.BreathwaveView;
import itg8.com.nowzonedesigndemo.widget.wave.WaveLoadingView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, BreathView, EasyPermissions.PermissionCallbacks, ComponentCallbacks2 {


    private static final int RC_STORAGE_PERM = 20;


    private static final String COLOR_NORMAL_M = "#24006bb7";
    private static final String COLOR_NORMAL_S = "#27BEFB";
    public static final String COLOR_CALM_M = "#240CB700";
    private static final String COLOR_CALM_S = "#FF35FB27";
    public static final String COLOR_STRESS_M = "#24B70F00";
    private static final String COLOR_STRESS_S = "#FFF92E27";
    public static final String COLOR_FOCUSED_M = "#240C00B7";
    private static final String COLOR_FOCUSED_S = "#FF4027FB";
    private static final int LAST_333 = 333;
    public static final double CONST_1 = -10.02d;
    public static final double CONST_2 = 10.02d;
    private static final double PI_MIN = -8.02d;
    private static final double PI_MAX = 8.02d;
    private static final double MIN_PRESSURE=800;
    private static final double MAX_PRESSURE=8100;
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_wave)
    FrameLayout rlWave;
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

    //    @BindView(R.id.txt_focus_time)
//    TextView txtFocusTime;

    //    @BindView(R.id.txt_stress_time)
//    TextView txtStressTime;

    @BindView(R.id.img_breath)
    ImageView imgBreath;
    @BindView(R.id.txt_breath)
    TextView txtBreath;
    @BindView(R.id.txt_breathCount)
    TextView txtBreathCount;
    @BindView(R.id.txt_AvgBreathValue)
    TextView txtAvgBreathValue;
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
//    @BindView(R.id.waveLoadingView)
//    WaveLoadingView waveLoadingView;
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
    @BindView(R.id.txt_focus_value)
    CustomFontTextView txtFocusValue;
    @BindView(R.id.txt_stress_value)
    TextView txtStressValue;
    @BindView(R.id.rl_main_top)
    RelativeLayout rlMainTop;
    @BindView(R.id.ll_breath_avg)
    LinearLayout llBreathAvg;


    private ActionBarDrawerToggle toggle;
    private double lastMax=10;
    private int count=1;
    private double lastMin=-10;
    private int lastCount=1;
    private long lastUpdate=0;
    private double smoothed=0;
    private static final double smoothing=50;
    Rolling rolling;
    private double dLast;
    private float a=0.2f;

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

        rolling=new Rolling(33);
        if(!getIntent().hasExtra(CommonMethod.FROMWEEk)) {
            startService(new Intent(this,BleService.class));

            checkDeviceConnection(rlWave);
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        presenter = new BreathPresenterImp(this);
        presenter.passContext(HomeActivity.this);
        presenter.onCreate();
        checkStoragePermission();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);


        toggle.syncState();
        drawer.addDrawerListener(toggle);
        rlSteps.setOnClickListener(this);
        llSleepMain.setOnClickListener(this);
        llBreathAvg.setOnClickListener(this);

        setType();
        setAnimator();

//        waveLoadingView.setWaveBgColor(Color.parseColor(COLOR_NORMAL_M));
//        waveLoadingView.setBorderColor(Color.parseColor(COLOR_NORMAL_S));

        initOtherView();
//        setFontOxygenRegular(FontType.ROBOTOlIGHT, txtBreathRate, txtStatus, txtMinute, txtStatusValue, breathValue);
//        setFontOpenSansSemiBold(FontType.ROBOTOlIGHT, txtCalm, txtCalmValue, txtStress, txtStressValue, txtFocus,  txtFocusValue);

    }

    @AfterPermissionGranted(RC_STORAGE_PERM)
    private void checkStoragePermission() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            onPermissionGrantedForStorage();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage), RC_STORAGE_PERM, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void onPermissionGrantedForStorage() {
        File extStorageDir = Environment.getExternalStorageDirectory();
        File newExternalStorageDir = new File(extStorageDir, getResources().getString(R.string.app_name));
        if (!newExternalStorageDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            newExternalStorageDir.mkdir();
        }

        SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.STORAGE_PATH, newExternalStorageDir.getAbsolutePath());

    }
    private void initOtherView() {
        int mAvgCount = SharePrefrancClass.getInstance(getApplicationContext()).getIPreference(CommonMethod.USER_CURRENT_AVG);
        if (mAvgCount > 0) {
            setAvgValue(mAvgCount);
        }
    }

    private void setAvgValue(int mAvgCount) {
        txtAvgBreathValue.setVisibility(View.VISIBLE);
        txtAvgBreathValue.setText(String.valueOf(mAvgCount));
    }


    @Override
    protected void onDestroy() {
//        waveLoadingView.cancelAnimation();
        presenter.onDetach();
        super.onDestroy();
    }

    private void setType() {
//        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.SQUARE);
//        waveLoadingView.setAmplitudeRatio(20);
//        waveLoadingView.setProgressValue(50);
    }

    /**
     * this is set by me
     */
    private void setAnimator() {

        //mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        // Sets the length of the animation, default is 1000.


//        waveLoadingView.setAnimDuration(3000);
//        waveLoadingView.startAnimation();


        //  waveLoadingView.cancelAnimation();
        // waveLoadingView.resumeAnimation();
        //                    waveLoadingView.pauseAnimation();


    }

    @Override
    protected void onResume() {
        if (breathview != null)
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

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), AlarmSettingActivity.class));
                break;
            case R.id.action_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.action_device:
                break;
            case R.id.action_alram:
                startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                break;
            case R.id.action_step_goal:
                break;
            case R.id.action_about:
                break;


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
                break;
            case R.id.ll_breath_avg:
                startActivity(new Intent(this, BreathHistoryActivity.class));
                break;
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
//            firstPreference(pressure);
        //Second Preference
        Observable.create(new ObservableOnSubscribe<Double>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Double> e) throws Exception {
//                firstPreference(pressure);
                e.onNext(calculateProportion(pressure));
            }
        }).observeOn(Schedulers.trampoline())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        if (count > 30) {
//            Log.d(TAG, "Presssure: "+pressure+" value after smoothing: " + smoothedValue(pressure) + " proportion:" + calculateProportion(smoothedValue(pressure)));
//            secondPref(pressure);
//            breathview.addSample(SystemClock.elapsedRealtime(),calculateProportion(smoothedValue(pressure)));
                            breathview.addSample(SystemClock.elapsedRealtime(),aDouble);
                        }else {
                            count++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
//        breathview.addSample(SystemClock.elapsedRealtime(), calculateProportion(pressure));

    double smoothedValue( double pressure ){
//        long now = Calendar.getInstance().getTimeInMillis();
//        long elapsedTime = now - lastUpdate;
        smoothed += 33 * (( pressure - smoothed ) / smoothing);
//        lastUpdate = now;
        return smoothed;
    }

    private void secondPref(double pressure){
        rolling.add(pressure);
        lastMax=((int)rolling.getaverage()+500)+1;
        lastMin=(int)rolling.getaverage()-500;
    }

    private void firstPreference(double pressure) {
//        if(lastMax<pressure || lastMax-2000>pressure) {
        if(lastMax<pressure) {
            lastMax = pressure;
        }else {
            lastMax=lastMax-1;
        }
        if(count>30) {
            if(lastMin == 0)
                lastMin=pressure;


//            if(lastMin-pressure>lastMax-1000)
//                lastMin=lastMax-1000;
//            if(lastMin)
//            if(count%LAST_333==0)
//                lastMin=lastMax-500;
//            else

//
//                if(lastMin>lastMax-1000)
//                    lastMax=lastMin+1000;

//            if(lastMax-lastMin>2000) {
//                lastMin = lastMax - 2000;
//            }
//            else
//                lastMax=lastMin+2000;
            if(lastMin>pressure)
                lastMin=pressure;
            else
                lastMin=lastMin+1;
//
////            if (lastMin > pressure || lastMin == 0){
////                lastMin = pressure;
////        }
        }else {
            count++;
        }


    }

    private double calculateProportion(double pressure) {
//        return (-0.02+(1.02*((pressure-(lastMax-500))/(lastMax-(lastMax-500)))));
//        double d=(double) Math.round((CONST_1+(CONST_2*((pressure-(lastMin))/(lastMax-lastMin)))) * 1000000000000000000d) / 1000000000000000000d;
//        s(i)=a*y(i)+(1-a)*s(i-1)
        double d=a*pressure+((1-a)*dLast);
//        double d=pressure;
        dLast=d;
       // Log.d(TAG,"ds:"+d);
//        return (PI_MIN + ((PI_MAX-PI_MIN) * ((d - (lastMin)) / (lastMax - lastMin))));
        return (PI_MIN + ((PI_MAX-PI_MIN) * ((d - (MIN_PRESSURE)) / (MAX_PRESSURE - MIN_PRESSURE))));
    }

    @Override
    public void onDeviceConnected() {
        Log.d(TAG, "Device connected");
    }

    @Override
    public void onDeviceDisconnected() {
        Log.d(TAG, "Device disconnected");
        Intent intent = new Intent("ACTION_NW_DEVICE_DISCONNECT");
        intent.putExtra(CommonMethod.ENABLE_TO_CONNECT,true);
        sendBroadcast(intent);
    }

    @Override
    public void onBreathCountAvailable(int intExtra) {
        Log.d(TAG, "Breath count: " + intExtra);
        new Handler().postDelayed(() -> breathValue.setText(String.valueOf(intExtra)),30);
    }

    @Override
    public void onStepCountReceived(int intExtra) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                txtStepCountValue.setText(String.valueOf(intExtra));

            }
        });
        Log.d(TAG, "Step count: " + intExtra);
    }

    @Override
    public void onStartDeviceScanActivity() {
//        Timber.i("Start device activity");
//        startActivity(new Intent(this, ScanDeviceActivity.class));
//        finish();
//        checkDeviceConnection(rlWave);
    }

    @Override
    public void onBreathingStateAvailable(BreathState state) {
        initOtherView();
        setStateRelatedDetails(state);
        presenter.onInitTimeHistory();
    }

    @Override
    public void onStateTimeHistoryReceived(StateTimeModel stateTimeModel) {
        if (stateTimeModel != null) {
            txtCalmValue.setText(stateTimeModel.getCalm());
            txtFocusValue.setText(stateTimeModel.getFocus());
            txtStressValue.setText(stateTimeModel.getStress());
        }
    }

    private void setStateRelatedDetails(BreathState state) {
        switch (state) {
            case CALM:
                        reactCalmState();
                break;
            case FOCUSED:
                        reactFocusedState();
                break;
            case STRESS:
                        reactStressState();
                break;
        }
    }

    private void reactStressState() {
      setState(BreathState.STRESS.name(),Color.parseColor(COLOR_STRESS_M),Color.parseColor(COLOR_STRESS_S));
    }

    private void setState(String name, int m, int s) {
        txtStatusValue.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                txtStatusValue.setText(name);
            }
        },60);
//        waveLoadingView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                waveLoadingView.setWaveColor(m);
//            }
//        },30);
//        waveLoadingView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                waveLoadingView.setWaveBgColor(s);
//            }
//        },90);
    }

    private void reactFocusedState() {
               setState(BreathState.FOCUSED.name(),Color.parseColor(COLOR_FOCUSED_M),Color.parseColor(COLOR_FOCUSED_S));
    }

    private void reactCalmState() {
        setState(BreathState.CALM.name(),Color.parseColor(COLOR_CALM_M),Color.parseColor(COLOR_CALM_S));
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onPermissionGrantedForStorage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
