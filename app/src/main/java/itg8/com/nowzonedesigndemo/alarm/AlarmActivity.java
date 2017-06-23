package itg8.com.nowzonedesigndemo.alarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.alarm.adapter.AlarmDaysAdapter;
import itg8.com.nowzonedesigndemo.alarm.model.AlarmDaysModel;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = AlarmActivity.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.frm_container_smart_alarm)
    ViewGroup frmContainerSmartAlarm;
    @BindView(R.id.frame_layout_meditation)
    ViewGroup frameLayoutMeditation;
    boolean visible;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.cardView)
//    CardView cardView;

    private TransitionManager mTransitionManager;
    private Scene sceneSmartAlarmExpand;
    private Scene sceneSmartAlarmCollapse;
    private RelativeLayout rlDays;
    private RelativeLayout rlAlarmSmart;
    private AlarmDaysModel model;
    private List<AlarmDaysModel> days;
    private AlarmDaysAdapter adapter;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        setSmartSenceAlarm();
        setMeditationSenceAlarm();

     //   View viewExpand = LayoutInflater.from(this).inflate(R.layout.layout_alarm_set, container, false);
        days = new ArrayList<>();
        AlarmDaysModel model;
        String[] dy = {"Mon", "Tue", "Wed", "Thr", "Fri", "Sat", "Sun"};
        for (int i = 0; i < dy.length; i++) {
            model = new AlarmDaysModel();
            model.setDays(dy[i]);
            model.setChecked(false);
            Log.d(TAG, "Check Days :" + dy[i]);
            days.add(model);
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frmContainerSmartAlarm.setOnClickListener(this);

    }

    private void setMeditationSenceAlarm() {
        ViewGroup containerMeditation = (ViewGroup) findViewById(R.id.frame_layout_meditation);






    }

    private void setSmartSenceAlarm() {
        ViewGroup container = (ViewGroup) findViewById(R.id.frm_container_smart_alarm);
        sceneSmartAlarmExpand = Scene.getSceneForLayout(container, R.layout.layout_alarm_set, this);
        sceneSmartAlarmCollapse = Scene.getSceneForLayout(container, R.layout.layout_smart_alarm, this);
        sceneSmartAlarmCollapse.enter();
    }


    private View createView(ViewGroup container, int resId) {
        return LayoutInflater.from(this).inflate(resId, container, false);

    }

    private void toggleSmartAlarm() {
        visible = !visible;

        if (visible) {
            TransitionManager.beginDelayedTransition(frmContainerSmartAlarm, TransitionInflater.from(AlarmActivity.this).
                    inflateTransition(R.transition.transition_manager));
            frmContainerSmartAlarm.removeAllViews();
            View expandView = createView(frmContainerSmartAlarm, R.layout.layout_alarm_set);

            ((CardView)expandView.findViewById(R.id.cardView)).setCardElevation(18f);

            TextView alarmTime = (TextView) expandView.findViewById(R.id.lbl_alarmTime);
            gridView = (GridView) expandView.findViewById(R.id.gridView);
            adapter = new AlarmDaysAdapter(this, days);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener((parent, view, position, id) -> {
                //  View singleView  = (View) adapter.getItem(position);

                if (days.get(position).isChecked()) {
                    setImageViewFadeOut(view, position);

                } else {
                    setImageViewFadeIn(view, position);

                }
            });
            alarmTime.setOnClickListener(v -> openDateTimeDialogue(v));
            frmContainerSmartAlarm.addView(expandView);

        } else {
            TransitionManager.beginDelayedTransition(frmContainerSmartAlarm, TransitionInflater.from(AlarmActivity.this).
                    inflateTransition(R.transition.transition_manager));
            frmContainerSmartAlarm.removeAllViews();
            View expandView = createView(frmContainerSmartAlarm, R.layout.layout_smart_alarm);
            CustomFontTextView alarmDay = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_days);
            CustomFontTextView alarmTime = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_time);
            String textview ="";
               String  time ="";

            for (int i = 0; i < days.size(); i++) {
                    if (days.get(i).isChecked()) {
                        textview += days.get(i).getDays() + " ";
                  time = days.get(i).getTime();

                    }
                }
            alarmDay.setText(textview);
            alarmTime.setText(time);

//            if(SharePrefrancClass.getInstance(this).getPref(CommonMethod.SAVEDAYS)!= null)
//                     {
//                    String oldDays = SharePrefrancClass.getInstance(this).getPref(CommonMethod.SAVEDAYS);
//                     alarmTime.setText(oldDays);
//
//                     }else
//                      {
//
//                     }
//            SharePrefrancClass.getInstance(this).savePref(CommonMethod.SAVEDAYS, textview);

                frmContainerSmartAlarm.addView(expandView);
        }
    }

    private void setImageViewFadeIn(View view, int position) {
        ImageView img = (ImageView) view.findViewById(R.id.img_dots);
        img.setVisibility(View.GONE);

        days.get(position).setChecked(true);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//                    fadeIn.setDuration(100);
        img.setAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void setImageViewFadeOut(View view, int position) {

        ImageView img = (ImageView) view.findViewById(R.id.img_dots);
        days.get(position).setChecked(false);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this

//                    fadeOut.setDuration(100);
        img.setAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void openDateTimeDialogue(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TextView txt = (TextView) v.findViewById(R.id.lbl_alarmTime);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> txt.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
        model.setTime( mHour+ ":"+mMinute);
        timePickerDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frm_container_smart_alarm:
                toggleSmartAlarm();
                break;
            case R.id.frame_layout_meditation:
                toggleMeditationAlarm();
                break;

        }

    }

    private void toggleMeditationAlarm() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
