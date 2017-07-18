package itg8.com.nowzonedesigndemo.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, CommonMethod.alarmListener {


    private static final String TAG = AlarmActivity.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //    @BindView(R.id.frm_container_smart_alarm)
//    ViewGroup frmContainerSmartAlarm;
//    @BindView(R.id.frame_layout_meditation)
//    ViewGroup frameLayoutMeditation;
//    boolean visible;
    @BindView(R.id.rl_alarm_meditation_main)
    RelativeLayout rlAlarmMeditationMain;
    @BindView(R.id.rl_alarm_smart_main)
    RelativeLayout rlAlarmSmartMain;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ImageView imgAlarm;
    @BindView(R.id.txt_alarm_smart)
    CustomFontTextView txtAlarmSmart;
    @BindView(R.id.txt_alarm_time)
    CustomFontTextView txtAlarmTime;
    @BindView(R.id.txt_alarm_days)
    CustomFontTextView txtAlarmDays;
    @BindView(R.id.switch_time)
    SwitchCompat switchTime;

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
    private String bc="";
    private String share="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        init();

//        setSmartSenceAlarm();
//        setMeditationSenceAlarm();

        //   View viewExpand = LayoutInflater.from(this).inflate(R.layout.layout_alarm_set, container, false);


    }

    private void init() {
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

        String abc ="";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlAlarmMeditationMain.setOnClickListener(this);
        rlAlarmSmartMain.setOnClickListener(this);
       adapter= new AlarmDaysAdapter(getApplicationContext(), days, this);
        if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS) != null) {
           String bc = SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS);
            Type type = new TypeToken<List<AlarmDaysModel>>(){}.getType();
           List<AlarmDaysModel> share = new Gson().fromJson(bc, type);
            Log.d(getClass().getSimpleName(),"share"+new Gson().toJson(share));
            for (AlarmDaysModel alarmModel:share) {

                if(alarmModel.isChecked()) {
                   abc += alarmModel.getDays()+" ";
                    txtAlarmDays.setText(abc);
                }
            }

        }
        adapter.notifyDataSetChanged();


    }


    private void setMeditationSenceAlarm() {
//        ViewGroup containerMeditation = (ViewGroup) findViewById(R.id.frame_layout_meditation);
//        sceneSmartAlarmExpand = Scene.getSceneForLayout(containerMeditation, R.layout.layout_alarm_set, this);
//        sceneSmartAlarmCollapse = Scene.getSceneForLayout(containerMeditation, R.layout.layout_smart_alarm, this);
//        sceneSmartAlarmCollapse.enter();
//
//        View expandView = createView(containerMeditation, R.layout.layout_smart_alarm);
//        CustomFontTextView alarmDay = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_days);
//        CustomFontTextView alarmTime = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_time);
//        if (SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
//            alarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
//        } else {
//            alarmTime.setText("Set alarm time..!!!");
//        }
//
//        if (SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS) != null) {
//            alarmDay.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS));
//        } else {
//            alarmDay.setText("Set alarm Day...!!!");
//        }

    }

    private void setSmartSenceAlarm() {
//        ViewGroup container = (ViewGroup) findViewById(R.id.frm_container_smart_alarm);
//        sceneSmartAlarmExpand = Scene.getSceneForLayout(container, R.layout.layout_alarm_set, this);
//        sceneSmartAlarmCollapse = Scene.getSceneForLayout(container, R.layout.layout_smart_alarm, this);
//
//        View expandView = createView(frmContainerSmartAlarm, R.layout.layout_smart_alarm);
//
//        CustomFontTextView alarmDay = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_days);
//        CustomFontTextView alarmTime = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_time);
//        sceneSmartAlarmCollapse.enter();
//
//
//        if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
//            alarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
//        } else {
//            alarmTime.setText("Set alarm time..!!!");
//        }
//
//        if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS) != null) {
//            alarmDay.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS));
//        } else {
//            alarmDay.setText("Set alarm Day...!!!");
//        }

    }


    private View createView(ViewGroup container, int resId) {
        return LayoutInflater.from(this).inflate(resId, container, false);
    }

    private void toggleSmartAlarm() {
//        visible = !visible;
//
//        if (visible) {
////            TransitionManager.beginDelayedTransition(frmContainerSmartAlarm, TransitionInflater.from(AlarmActivity.this).
////                    inflateTransition(R.transition.transition_manager));
//            TransitionManager.beginDelayedTransition(frmContainerSmartAlarm);
//            frmContainerSmartAlarm.removeAllViews();
//            View expandView = createView(frmContainerSmartAlarm, R.layout.layout_alarm_set);
//
//
//            TextView alarmTime = (TextView) expandView.findViewById(R.id.lbl_alarmTime);
//            gridView = (GridView) expandView.findViewById(R.id.gridView);
//            adapter = new AlarmDaysAdapter(this, days);
//            gridView.setAdapter(adapter);
//
//            gridView.setOnItemClickListener((parent, view, position, id) -> {
//                //  View singleView  = (View) adapter.getItem(position);
//
//                if (days.get(position).isChecked()) {
//                    setImageViewFadeOut(view, position);
//
//                } else {
//                    setImageViewFadeIn(view, position);
//
//                }
//            });
//            alarmTime.setOnClickListener(v -> openDateTimeDialogue(v));
//
//            if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
//                alarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
//            }
//            frmContainerSmartAlarm.addView(expandView);
//
//        } else {
//            TransitionManager.beginDelayedTransition(frmContainerSmartAlarm, TransitionInflater.from(AlarmActivity.this).
//                    inflateTransition(R.transition.transition_manager));
//            frmContainerSmartAlarm.removeAllViews();
//            View expandView = createView(frmContainerSmartAlarm, R.layout.layout_smart_alarm);
//            CustomFontTextView alarmDay = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_days);
//            CustomFontTextView alarmTime = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_time);
//            String textview = "";
//
//            for (int i = 0; i < days.size(); i++) {
//                if (days.get(i).isChecked()) {
//                    textview += days.get(i).getDays() + " ";
//                }
//            }
//            alarmDay.setText(textview);
//            if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
//                alarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
//
//            } else {
//                alarmTime.setText("Set alarm time..!!!");
//            }
//            if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS) != null) {
//                String oldDays = SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS);
//                alarmDay.setText(oldDays);
//            } else {
//                alarmDay.setText(textview);
//            }
//            SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.SAVEDAYS, textview);
//            frmContainerSmartAlarm.addView(expandView);
//        }
    }

    private void setImageViewFadeIn(View view, int position) {
        final ImageView img = (ImageView) view.findViewById(R.id.img_dots);
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
        fadeIn.start();
    }

    private void setImageViewFadeOut(View view, int position) {

        final ImageView img = (ImageView) view.findViewById(R.id.img_dots);
        days.get(position).setChecked(false);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this

        fadeOut.setDuration(100);
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
        fadeOut.start();
    }

    private void openDateTimeDialogue(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TextView txt = (TextView) v.findViewById(R.id.lbl_alarmTime);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        txt.setText(hourOfDay + ":" + minute);
                        String time = (hourOfDay + ":" + minute);
                        Log.d(TAG, "Time:" + time);
                        SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.SAVEALARMTIME, time);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }
    /// (view, hourOfDay, minute) ->
    // {txt.setText(hourOfDay + ":" + minute), mHour, mMinute, false);


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_alarm_smart_main:
                openBottomSheetForSmartAlarm();
                // toggleSmartAlarm();
                break;
            case R.id.rl_alarm_meditation_main:
                // toggleMeditationAlarm();
                openBottomSheetForSmartAlarm();
                break;
        }

    }


    private void openBottomSheetForSmartAlarm() {
        View view = getLayoutInflater().inflate(R.layout.layout_alarm_set, null);

        final Dialog mBottomSheetDialog = new Dialog(AlarmActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        GridView gridView = (GridView) mBottomSheetDialog.findViewById(R.id.gridView);
        CustomFontTextView txtAlarmTime = (CustomFontTextView) mBottomSheetDialog.findViewById(R.id.lbl_alarmTime);
        CustomFontTextView txtAlarmtype = (CustomFontTextView) mBottomSheetDialog.findViewById(R.id.lbl_alarmType);
        SeekBar seekBar = (SeekBar) mBottomSheetDialog.findViewById(R.id.seekbar);
        ImageView imgSpeaker = (ImageView) mBottomSheetDialog.findViewById(R.id.img_speaker);
        gridView.setAdapter(adapter);
        mBottomSheetDialog.show();
        gridViewClickListner(gridView, txtAlarmTime);

    }

    private void gridViewClickListner(GridView gridView, CustomFontTextView txtAlarmTime) {
//        gridView.setOnItemClickListener((parent, view, position, id) -> {
//            //  View singleView  = (View) adapter.getItem(position);
//
//            if (days.get(position).isChecked()) {
//                setImageViewFadeOut(view, position);
//
//            } else {
//                setImageViewFadeIn(view, position);
//            }
//        });
        txtAlarmTime.setOnClickListener(v -> openDateTimeDialogue(v));

        if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
            txtAlarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
        }


    }

    private void toggleMeditationAlarm() {
//        visible = !visible;
//        if (visible) {
//            TransitionManager.beginDelayedTransition(frameLayoutMeditation, TransitionInflater.from(AlarmActivity.this).
//                    inflateTransition(R.transition.transition_manager));
//            frameLayoutMeditation.removeAllViews();
//            View expandView = createView(frameLayoutMeditation, R.layout.layout_alarm_set);
//
//            ((CardView) expandView.findViewById(R.id.cardView)).setCardElevation(18f);
//
//
//            TextView alarmTime = (TextView) expandView.findViewById(R.id.lbl_alarmTime);
//            gridView = (GridView) expandView.findViewById(R.id.gridView);
//            adapter = new AlarmDaysAdapter(this, days);
//            gridView.setAdapter(adapter);
//
//            gridView.setOnItemClickListener((parent, view, position, id) -> {
//                //  View singleView  = (View) adapter.getItem(position);
//
//                if (days.get(position).isChecked()) {
//                    setImageViewFadeOut(view, position);
//
//                } else {
//                    setImageViewFadeIn(view, position);
//
//                }
//            });
//            alarmTime.setOnClickListener(v -> openDateTimeDialogue(v));
//
//            if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
//                alarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
//            }
//            frameLayoutMeditation.addView(expandView);
//
//        } else {
//            TransitionManager.beginDelayedTransition(frameLayoutMeditation, TransitionInflater.from(AlarmActivity.this).
//                    inflateTransition(R.transition.transition_manager));
//            frameLayoutMeditation.removeAllViews();
//            View expandView = createView(frameLayoutMeditation, R.layout.layout_smart_alarm);
//            CustomFontTextView alarmDay = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_days);
//            CustomFontTextView alarmTime = (CustomFontTextView) expandView.findViewById(R.id.txt_alarm_time);
//            String textview = "";
//
//            for (int i = 0; i < days.size(); i++) {
//                if (days.get(i).isChecked()) {
//                    textview += days.get(i).getDays() + " ";
//                }
//            }
//            alarmDay.setText(textview);
//            if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME) != null) {
//                alarmTime.setText(SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEALARMTIME));
//
//            } else {
//                alarmTime.setText("Set alarm time..!!!");
//            }
//            if (getApplicationContext() != null && SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS) != null) {
//                String oldDays = SharePrefrancClass.getInstance(getApplicationContext()).getPref(CommonMethod.SAVEDAYS);
//                alarmDay.setText(oldDays);
//            } else {
//                alarmDay.setText(textview);
//            }
//            SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.SAVEDAYS, textview);
//            frameLayoutMeditation.addView(expandView);
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAlarmListener(List<AlarmDaysModel> abc) {
        Log.d(getClass().getSimpleName(),"Days:"+bc);
        bc="";
         for (int i=0; i<abc.size(); i++)
         {
             if(abc.get(i).isChecked())
             {
                 bc +=abc.get(i).getDays();

             }

         }
        Type type = new TypeToken<List<AlarmDaysModel>>(){}.getType();
        share = new Gson().toJson(abc, type);
        SharePrefrancClass.getInstance(getApplicationContext()).savePref(CommonMethod.SAVEDAYS,share);
        txtAlarmDays.setText(bc);
    }



}
