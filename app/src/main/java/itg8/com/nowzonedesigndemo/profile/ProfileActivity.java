package itg8.com.nowzonedesigndemo.profile;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.ProfileModel;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.Helper;

public class ProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, View.OnFocusChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.image)
//    ImageView image;

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
    @BindView(R.id.basic_detail)
    CustomFontTextView basicDetail;

    @BindView(R.id.input_name)
    TextInputLayout inputName;
    @BindView(R.id.ll_height)
    LinearLayout llHeight;
    @BindView(R.id.layout_weight)
    LinearLayout layoutWeight;
    @BindView(R.id.edt_day)
    EditText edtDay;
    @BindView(R.id.input_day)
    TextInputLayout inputDay;
    @BindView(R.id.edt_month)
    EditText edtMonth;
    @BindView(R.id.input_month)
    TextInputLayout inputMonth;
    @BindView(R.id.edt_year)
    EditText edtYear;
    @BindView(R.id.input_year)
    TextInputLayout inputYear;
    @BindView(R.id.ll_age)
    LinearLayout llAge;
    private ProfileModel model;
    private float heightInFeet;
    private float weightInKg;

    private static double poundsToKilos(double pounds) {
        return pounds * 0.454;
    }

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
        edtDay.setOnFocusChangeListener (this);
        edtMonth.setOnFocusChangeListener (this);
        edtYear.setOnFocusChangeListener (this);
        model = new ProfileModel();

        // checkAlreadyFilled();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rgb_height_cm:
                inputHeightCm.setVisibility(View.VISIBLE);
                inputHeight.setVisibility(View.GONE);
                edtHeightCm.setHintTextColor(Color.WHITE);
                edtHeightCm.setHint("Height[cm]");
                break;
            case R.id.rgb_height_feet:
                inputHeightCm.setVisibility(View.VISIBLE);
                edtHeightCm.setHintTextColor(Color.WHITE);
                edtHeight.setHintTextColor(Color.WHITE);
                edtHeightCm.setHint("Height[feet]");
                edtHeight.setHint("Height[inch]");
                break;
            case R.id.fab:

                break;
            case R.id.rgb_weight_kg:
                edtWeight.setHintTextColor(Color.WHITE);
                edtWeight.setHint("Weight[kg]");

                break;
            case R.id.rgb_weight_pounds:
                edtWeight.setHintTextColor(Color.WHITE);
                edtWeight.setHint("Weight[pounds]");
                break;


        }

    }

    private void openDateTimeDialogue(EditText inputDay) {
        // Get Current Time
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int  mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub

                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MMM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                String[] entireDate = sdf.format(mcurrentDate.getTime()).split("/");
                String day= entireDate[0];
                String month= entireDate[1];
                String year= entireDate[2];
                edtDay.setText(day);
                edtMonth.setText(month);
                edtYear.setText(year);
                Log.d(getClass().getSimpleName(),"DatePicker:"+mcurrentDate.getTime());


            }
        },mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }






    @Override
    public void onClick(View view) {
        if (validate()) {
            model.setName(edtName.getText().toString());
            model.setHeight(calculateHeightInFeet());
            model.setWeight(calculateWeightInKg());
            ((AppApplication) getApplication()).setProfileModel(model);
            onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private float calculateWeightInKg() {
        if (rgbWeightPounds.isChecked())
            return (float) poundsToKilos(Double.parseDouble(edtWeight.getText().toString()));
        else
            return Float.parseFloat(edtWeight.getText().toString());
    }

    private float calculateHeightInFeet() {
        if (rgbHeightCm.isChecked())
            return Helper.centimeterToFeet(edtHeightCm.getText().toString());
        else
            return (float) (Float.parseFloat(edtHeight.getText().toString()) + (0.1 * Float.parseFloat(edtHeightCm.getText().toString())));
    }

    private boolean validate() {
        boolean valid = true;
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setError("Please provide name...");
            setFocus(edtName);
            valid = false;
        }
        if (TextUtils.isEmpty(edtHeight.getText().toString())) {
            edtHeight.setError("Please provide your height...");
            setFocus(edtHeight);
            valid = false;
        }
        if (TextUtils.isEmpty(edtWeight.getText().toString())) {
            edtWeight.setError("Please provide your weight...");
            setFocus(edtWeight);
            valid = false;
        }

        return valid;
    }

    private void setFocus(EditText edtName) {
        edtName.requestFocus();

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

             if(hasFocus) {
                 if (v.getId() == R.id.edt_day) {
                     openDateTimeDialogue(edtDay);

                 }
                 else if(v.getId()== R.id.edt_month)
                 {
                     openDateTimeDialogue(edtDay);

                 }else if(v.getId()== R.id.edt_year)
                 {
                     openDateTimeDialogue(edtDay);

                 }


             }


    }
}
