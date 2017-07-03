package itg8.com.nowzonedesigndemo.setting;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_awakeAlarm)
    CustomFontTextView lblAwakeAlarm;
    @BindView(R.id.txt_alarm_time)
    CustomFontTextView txtAlarmTime;
    @BindView(R.id.btn_alarmSetting)
    Button btnAlarmSetting;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private Animation zoomIn, zoomOut;
      boolean isFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.zoom_in_text);
        zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.zoomout_text);
        Log.d(getClass().getSimpleName(),"Calender:"+Calendar.getInstance().getTimeInMillis());
        Log.d(getClass().getSimpleName(),"Calender:"+System.currentTimeMillis());
        btnAlarmSetting.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_alarmSetting) {

                startAnimation();

        }

    }

    private void startAnimation() {
        if (btnAlarmSetting.getText().equals("Calibarating"))
        {
            btnAlarmSetting.startAnimation(zoomOut);
            btnAlarmSetting.startAnimation(zoomIn);
            btnAlarmSetting.setText("Finished");

        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            btnAlarmSetting.startAnimation(zoomOut);
                            btnAlarmSetting.startAnimation(zoomIn);
                            btnAlarmSetting.setPadding(8, 0, 8, 0);
                            btnAlarmSetting.setText("Calibarating");

                            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100);
                            objectAnimator.start();

                        }
                    });
                    Calendar calendar =   Calendar.getInstance();
                    calendar.add(Calendar.MINUTE,1);
                    long timerEnd =calendar.getTimeInMillis();
                    while (timerEnd > System.currentTimeMillis()) {
                        int progress = (int) (timerEnd - System.currentTimeMillis()) / 600;
                        progress = 100 - progress;
                        progressBar.setProgress(progress);
                    }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnAlarmSetting.startAnimation(zoomOut);
                                btnAlarmSetting.startAnimation(zoomIn);
                                btnAlarmSetting.setPadding(8, 0, 8, 0);
                                btnAlarmSetting.setText("Alarm Started");
                                btnAlarmSetting.setTextColor(Color.GREEN);
                                GradientDrawable drawable = (GradientDrawable)btnAlarmSetting.getBackground();
                                drawable.setStroke(1, Color.GREEN);
                                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100);
                                objectAnimator.start();
                            }
                        });
                }
            }).start();




                     }
                 }



        }





