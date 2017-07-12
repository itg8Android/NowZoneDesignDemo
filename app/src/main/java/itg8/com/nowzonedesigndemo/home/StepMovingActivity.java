package itg8.com.nowzonedesigndemo.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.widget.steps.CustomStepImage;

public class StepMovingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    CustomStepImage image;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.btn_add)
    Button btnAdd;
    List<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_moving);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       images = new ArrayList<>();
        Log.v(getClass().getSimpleName(),""+SharePrefrancClass.getInstance(getApplicationContext()).getPref("TEMP"));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              image.addStep();
                //addValue();

            }
        });



    }

    private void addValue() {
        for(int i=0 ; i<50;i++)
            SharePrefrancClass.getInstance(getApplicationContext()).savePref("TEMP","VAL");

    }

    private void addImage() {

        images.add(R.drawable.ic_steps);
        setAnimation(images);
        if (images.size() > 3) {
            images.clear();
        }

        Log.d(getClass().getSimpleName(),"images:"+images.size());


    }

    private void setAnimation(List<Integer> images) {
        TranslateAnimation animation;
        float fromXDelta = 0.0f;
        float toXDelta = 60.0f;
        float increaseXDelta =+toXDelta;

       animation = new TranslateAnimation(fromXDelta, toXDelta,
                0.0f, 0.0f);
        if(images.size()>1)
        {
           animation = new TranslateAnimation(toXDelta, increaseXDelta,
                    0.0f, 0.0f);
        }
        image.startAnimation(animation);
        animation.setDuration(5000);
//        animation.setRepeatCount(1);
//        animation.setRepeatMode(1);
        animation.setFillAfter(true);

    }


}
