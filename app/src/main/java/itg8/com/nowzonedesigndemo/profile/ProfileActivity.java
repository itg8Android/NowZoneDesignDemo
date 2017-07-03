package itg8.com.nowzonedesigndemo.profile;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;

public class ProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.image)
//    ImageView image;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.input_name)
    TextInputLayout inputName;
    @BindView(R.id.edt_height)
    EditText edtHeight;
    @BindView(R.id.input_height)
    TextInputLayout inputHeight;
    @BindView(R.id.edt_height_cm)
    EditText edtHeightCm;
    @BindView(R.id.rgb_height_cm)
    RadioButton rgbHeightCm;
    @BindView(R.id.rgb_height_feet)
    RadioButton rgbHeightFeet;
    @BindView(R.id.rbg_main_height)
    RadioGroup rbgMainHeight;
    //    @BindView(R.id.ll_height)
//    LinearLayout llHeight;
//    @BindView(R.id.edt_weight)
//    EditText edtWeight;
    @BindView(R.id.input_weight)
    TextInputLayout inputWeight;

    @BindView(R.id.rbg_main_weight)
    RadioGroup rbgMainWeight;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rl_profileImg)
    RelativeLayout rlProfileImg;
    @BindView(R.id.edt_weight)
    EditText edtWeight;
    @BindView(R.id.input_height_cm)
    TextInputLayout inputHeightCm;
    @BindView(R.id.rgb_weight_kg)
    RadioButton rgbWeightKg;
    @BindView(R.id.rgb_weight_pounds)
    RadioButton rgbWeightPounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rbgMainWeight.setOnCheckedChangeListener(this);
        rbgMainHeight.setOnCheckedChangeListener(this);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rgb_height_cm:
                inputHeightCm.setVisibility(View.VISIBLE);
                edtHeightCm.setHint("Height[cm]");
                break;
            case R.id.rgb_height_feet:
                inputHeightCm.setVisibility(View.VISIBLE);
                edtHeightCm.setHint("Height[inch]");
                edtHeight.setHint("Height[feet]");
                break;
            case R.id.fab:

                break;
            case R.id.rgb_weight_kg:
                edtWeight.setHint("Weight[kg]");
                break;
        case R.id.rgb_weight_pounds:
            edtWeight.setHint("Weight[pounds]");
                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
