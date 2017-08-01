package itg8.com.nowzonedesigndemo.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.audio.fragment.MeditationAllFragment;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.setting.fragment.AlarmFragment;
import itg8.com.nowzonedesigndemo.setting.fragment.AlarmSettingFragment;
import itg8.com.nowzonedesigndemo.setting.fragment.DeviceHistoryFragment;
import itg8.com.nowzonedesigndemo.setting.fragment.FragmentDevice;
import itg8.com.nowzonedesigndemo.setting.fragment.ProfileFragment;
import itg8.com.nowzonedesigndemo.setting.fragment.StepGoalFragment;

public class SettingMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        init();


    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         checkIntent();


    }

    private void checkIntent() {
        Fragment fragment = null;

        if(getIntent().hasExtra(CommonMethod.BREATH))
        {
            switch (getIntent().getStringExtra(CommonMethod.BREATH))
            {
                case CommonMethod.FROM_DEVICE:
                    fragment= new FragmentDevice();
                    callFragmentInstaance(fragment);
                    break;
                case CommonMethod.FROM_STEP_GOAL:
                    fragment= new StepGoalFragment();
                    callFragmentInstaance(fragment);
                    break;
                case CommonMethod.FROM_PROFILE:
                    fragment= new ProfileFragment();
                    callFragmentInstaance(fragment);
                    break;
                case CommonMethod.FROM_ALARM_SETTING:
                    fragment= new AlarmFragment();
                    callFragmentInstaance(fragment);
                    break;
                case CommonMethod.FROM_MEDITATION:
                    fragment = new MeditationAllFragment();
                    callFragmentInstaance(fragment);
                    break;
            case CommonMethod.FROM_DEVICE_HISTORY:
                    //fragment = new DeviceHistoryFragment();
                  //  callFragmentInstaance(fragment);
                    break;
                case CommonMethod.FROM_ALARM_HOME:
                    fragment = new AlarmSettingFragment();
                    callFragmentInstaance(fragment);
                    break;

            }
        }
    }



    private void callFragmentInstaance(Fragment fragmentDevice) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout_setting,fragmentDevice).commit();
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
