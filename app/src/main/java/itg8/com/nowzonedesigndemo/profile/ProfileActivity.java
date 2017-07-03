package itg8.com.nowzonedesigndemo.profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.ProfileModel;
import itg8.com.nowzonedesigndemo.utility.Helper;

public class ProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.image)
//    ImageView image;
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
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.input_weight)
    TextInputLayout inputWeight;
    @BindView(R.id.button)
    Button button;
    private ProfileModel model;
    private float heightInFeet;
    private float weightInKg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rbgMainWeight.setOnCheckedChangeListener(this);
        rbgMainHeight.setOnCheckedChangeListener(this);
        button.setOnClickListener(this);
        model=new ProfileModel();
       // checkAlreadyFilled();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rgb_height_cm:
                inputHeightCm.setVisibility(View.VISIBLE);
                inputHeight.setVisibility(View.GONE);
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
    public void onClick(View view) {
        if(validate()){
            model.setName(edtName.getText().toString());
            model.setHeight(calculateHeightInFeet());
            model.setWeight(calculateWeightInKg());
            ((AppApplication)getApplication()).setProfileModel(model);
            onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private float calculateWeightInKg() {
        if(rgbWeightPounds.isChecked())
            return (float) poundsToKilos(Double.parseDouble(edtWeight.getText().toString()));
        else
            return Float.parseFloat(edtWeight.getText().toString());
    }

    private static double poundsToKilos(double pounds) {
        return pounds * 0.454;
    }

    private float calculateHeightInFeet() {
        if(rgbHeightCm.isChecked())
            return Helper.centimeterToFeet(edtHeightCm.getText().toString());
        else
            return (float) (Float.parseFloat(edtHeight.getText().toString())+(0.1*Float.parseFloat(edtHeightCm.getText().toString())));
    }

    private boolean validate() {
        boolean valid=true;
        if(TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setError("Please provide name...");
            setFocus(edtName);
            valid = false;
        }
        if(TextUtils.isEmpty(edtHeight.getText().toString())) {
            edtHeight.setError("Please provide your height...");
            setFocus(edtHeight);
            valid = false;
        }
        if(TextUtils.isEmpty(edtWeight.getText().toString())) {
            edtWeight.setError("Please provide your weight...");
            setFocus(edtWeight);
            valid = false;
        }
        return valid;
    }

    private void setFocus(EditText edtName) {
        edtName.requestFocus();
    }
}
