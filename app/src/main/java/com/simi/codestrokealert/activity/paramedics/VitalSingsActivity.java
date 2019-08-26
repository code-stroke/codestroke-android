package com.simi.codestrokealert.activity.paramedics;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.google.android.material.tabs.TabLayout;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.fragment.EyeFragment;
import com.simi.codestrokealert.fragment.MotorFragment;
import com.simi.codestrokealert.fragment.VerbalFragment;
import com.simi.codestrokealert.model.CaseAssessments;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class VitalSingsActivity  extends AppCompatActivity implements View.OnClickListener,
        EyeFragment.SendData, MotorFragment.SendIndexMotor, VerbalFragment.SendIndexVerbal {

    private ImageButton back_btn;
    private Button next_btn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle bundle;
    private EditText txtBloodPressureSystolic , txtBloodPressureDiastolic , txtHeartRate, txtRespiratoryRate,
            txtOxygenSaturation, txtTemperature, txtBloodGlucose;
    private RadioButton btnRegular, btnIrregular;
    private CaseAssessments caseAssessments;
    private String strBloodPressureDyastolic, strBloodPressureSystolic, strHeartRate, strRespiratoryRate, stroxygenSaturation,
            strTemperature, strBloodGlucose, strGCS = " ", strHeartRythm, strVerbal = "", strEye = "", strMotor = "";

    private int bloodPressureDiastolic = -1, bloodPressureSystolic = -1, heartRate = -1,
            respiratoryRate = -1, oxygenSaturation = -1, temperature = -1,
            bloodGlucose = -1, gcsItemSelected = 0, eyeIndexSelected = 0, motorIndexSelected = 0, verbalIndexSelected = 0 ;

    @Override
    public void sendIndex(int index) {
         switch (index){
             case 1:
                 strEye = getString(R.string.no_eye_opening_1);
                 SharedPref.write(SharedPref.EYE, strEye);
                 eyeIndexSelected = 1;
                 SharedPref.write(SharedPref.EYE_INDEX, eyeIndexSelected);
                 break;
             case 2:
                 strEye = getString(R.string.eye_open_to_pain_2);
                 SharedPref.write(SharedPref.EYE, strEye);
                 eyeIndexSelected = 2;
                 SharedPref.write(SharedPref.EYE_INDEX, eyeIndexSelected);
                 break;
             case 3:
                 strEye = getString(R.string.eye_open_to_speech_3);
                 SharedPref.write(SharedPref.EYE, strEye);
                 eyeIndexSelected = 3;
                 SharedPref.write(SharedPref.EYE_INDEX, eyeIndexSelected);
                 break;
             case 4:
                 strEye = getString(R.string.eye_open_spontaneously_4);
                 SharedPref.write(SharedPref.EYE, strEye);
                 eyeIndexSelected = 4;
                 SharedPref.write(SharedPref.EYE_INDEX, eyeIndexSelected);
                 break;
         }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.check_back_full){
            finish();
        }
    }

    @Override
    public void sendIdxMotor(int index) {
        switch (index){
            case 1:
                strMotor = getString(R.string.no_motor_response);
                SharedPref.write(SharedPref.MOTOR, strMotor);
                motorIndexSelected = 1;
                SharedPref.write(SharedPref.MOTOR_INDEX, motorIndexSelected);
                break;
            case 2:
                strMotor = getString(R.string.extension_to_pain);
                SharedPref.write(SharedPref.MOTOR, strMotor);
                motorIndexSelected = 2;
                SharedPref.write(SharedPref.MOTOR_INDEX, motorIndexSelected);
                break;
            case 3:
                strMotor = getString(R.string.abnormal_ﬂexion_to_pain);
                SharedPref.write(SharedPref.MOTOR, strMotor);
                motorIndexSelected = 3;
                SharedPref.write(SharedPref.MOTOR_INDEX, motorIndexSelected);
                break;
            case 4:
                strMotor = getString(R.string.flexion_to_pain);
                SharedPref.write(SharedPref.MOTOR, strMotor);
                motorIndexSelected = 4;
                SharedPref.write(SharedPref.MOTOR_INDEX, motorIndexSelected);
                break;
            case 5:
                strMotor = getString(R.string.localises_to_pain);
                SharedPref.write(SharedPref.MOTOR, strMotor);
                motorIndexSelected = 5;
                SharedPref.write(SharedPref.MOTOR_INDEX, motorIndexSelected);
                break;
            case 6:
                strMotor = getString(R.string.obeys_commands);
                SharedPref.write(SharedPref.MOTOR, strMotor);
                motorIndexSelected = 6;
                SharedPref.write(SharedPref.MOTOR_INDEX, motorIndexSelected);
                break;
        }
    }

    @Override
    public void sendIdxVerbal(int index) {
        switch (index){
            case 1:
                strVerbal = getString(R.string.no_verbal_response);
                SharedPref.write(SharedPref.VERBAL, strVerbal);
                verbalIndexSelected = 1;
                SharedPref.write(SharedPref.VERBAL_INDEX, verbalIndexSelected);
                break;
            case 2:
                strVerbal = getString(R.string.incomprehensible_sounds);
                SharedPref.write(SharedPref.VERBAL, strVerbal);
                verbalIndexSelected = 2;
                SharedPref.write(SharedPref.VERBAL_INDEX, verbalIndexSelected);
                break;
            case 3:
                strVerbal = getString(R.string.inappropriate_words);
                SharedPref.write(SharedPref.VERBAL, strVerbal);
                verbalIndexSelected = 3;
                SharedPref.write(SharedPref.VERBAL_INDEX, verbalIndexSelected);
                break;
            case 4:
                strVerbal = getString(R.string.confused);
                verbalIndexSelected = 4;
                SharedPref.write(SharedPref.VERBAL, strVerbal);
                SharedPref.write(SharedPref.VERBAL_INDEX, verbalIndexSelected);
                break;
            case 5:
                strVerbal = getString(R.string.oriented);
                verbalIndexSelected = 5;
                SharedPref.write(SharedPref.VERBAL, strVerbal);
                SharedPref.write(SharedPref.VERBAL_INDEX, verbalIndexSelected);
                break;
        }
    }

    private enum heartRhythm{
       regular, irregular
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get translucent status bar and push activity layout to status bar for the gradient to work for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_vital_sings);
        initViews();
        setupViewPager(viewPager);

        //Assigns the ViewPager to TabLayout
        tabLayout.setupWithViewPager(viewPager);
        fillData();


    }

    protected void initViews(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        bundle = new Bundle();
        back_btn = (ImageButton)findViewById(R.id.arrow_back);
        next_btn = (Button)findViewById(R.id.btn_next);
        txtBloodPressureSystolic = (EditText)findViewById(R.id.blood_pressure_systolic);
        txtBloodPressureDiastolic = (EditText)findViewById(R.id.blood_pressure_diastolic);
        txtHeartRate = (EditText)findViewById(R.id.heart_rate);
        txtRespiratoryRate = (EditText)findViewById(R.id.respiratory_rate);
        txtOxygenSaturation = (EditText) findViewById(R.id.oxygen_saturation);
        txtTemperature = (EditText)findViewById(R.id.temperature);
        txtTemperature.setInputType(InputType.TYPE_CLASS_NUMBER);
        txtBloodGlucose = (EditText)findViewById(R.id.blood_glucose);
        btnIrregular = (RadioButton)findViewById(R.id.btn_irregular);
        btnRegular = (RadioButton)findViewById(R.id.btn_regular);
        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        btnIrregular.setOnClickListener(this);
        btnRegular.setOnClickListener(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EyeFragment(), "Eye");
        adapter.addFragment(new VerbalFragment(), "Verbal");
        adapter.addFragment(new MotorFragment(), "Motor");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        savedVitalSingsData( );
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.arrow_back:
               onBackPressed();
               break;

           case R.id.btn_irregular:
               strHeartRythm = heartRhythm.irregular.name();
               btnIrregular.setChecked(true);
               SharedPref.write(SharedPref.HEART_RHYTHM, strHeartRythm);
               break;

           case R.id.btn_regular:
               strHeartRythm = heartRhythm.regular.name();
               SharedPref.write(SharedPref.HEART_RHYTHM, strHeartRythm);
               btnRegular.setChecked(true);
               break;

           case R.id.btn_next:
               savedVitalSingsData( );
               Intent intent = new Intent(getBaseContext(), RaceScoreActivity.class );
               startActivity(intent);
               break;
       }
    }

    protected void savedVitalSingsData(){
        strBloodPressureDyastolic = txtBloodPressureDiastolic.getText().toString();
        strBloodPressureSystolic = txtBloodPressureSystolic.getText().toString();
        strTemperature = txtTemperature.getText().toString();
        stroxygenSaturation = txtOxygenSaturation.getText().toString();
        strRespiratoryRate = txtRespiratoryRate.getText().toString();
        strHeartRate = txtHeartRate.getText().toString();
        strBloodGlucose  = txtBloodGlucose.getText().toString();
        bloodPressureDiastolic = (strBloodPressureDyastolic.isEmpty() ? -1 : Integer.parseInt(txtBloodPressureDiastolic.getText().toString()));
        bloodPressureSystolic = (strBloodPressureSystolic.isEmpty() ? -1 : Integer.parseInt(txtBloodPressureSystolic.getText().toString()));
        heartRate = (strHeartRate.isEmpty() ? -1 : Integer.parseInt(txtHeartRate.getText().toString()));
        respiratoryRate = (strRespiratoryRate.isEmpty() ? -1 : Integer.parseInt(txtRespiratoryRate.getText().toString()));
        oxygenSaturation = (stroxygenSaturation.isEmpty() ? -1 : Integer.parseInt(txtOxygenSaturation.getText().toString()));
        temperature = (strTemperature.isEmpty() ? -1 : Integer.parseInt(txtTemperature.getText().toString()));
        bloodGlucose = (strBloodGlucose.isEmpty() ? -1 : Integer.parseInt(txtBloodGlucose.getText().toString()));
        SharedPref.write(SharedPref.BP_SYSTOLIC, bloodPressureSystolic);
        SharedPref.write(SharedPref.BP_DIASTOLIC, bloodPressureDiastolic);
        SharedPref.write(SharedPref.HEART_RATE, heartRate);
        SharedPref.write(SharedPref.RR, respiratoryRate);
        SharedPref.write(SharedPref.O2STAS, oxygenSaturation);
        SharedPref.write(SharedPref.TEMP, temperature);
        SharedPref.write(SharedPref.BLOOD_GLOUCE, bloodGlucose);
        strEye = SharedPref.read(SharedPref.EYE, "");
        strMotor = SharedPref.read(SharedPref.MOTOR, "");
        strVerbal = SharedPref.read(SharedPref.VERBAL, "");
        eyeIndexSelected = SharedPref.read(SharedPref.EYE_INDEX, 0);
        motorIndexSelected = SharedPref.read(SharedPref.MOTOR_INDEX, 0);
        verbalIndexSelected = SharedPref.read(SharedPref.VERBAL_INDEX, 0);
        strGCS = strEye + "\n" + strVerbal + "\n" + strMotor;
        gcsItemSelected = eyeIndexSelected + verbalIndexSelected + motorIndexSelected;
        SharedPref.write(SharedPref.GCS_TEXT, strGCS);
        SharedPref.write(SharedPref.GCS, gcsItemSelected);

    }

    protected void fillData(){
        bloodPressureSystolic = SharedPref.read(SharedPref.BP_SYSTOLIC, bloodPressureSystolic);
        bloodPressureDiastolic = SharedPref.read(SharedPref.BP_DIASTOLIC, bloodPressureDiastolic);
        heartRate = SharedPref.read(SharedPref.HEART_RATE, heartRate);
        strHeartRythm = SharedPref.read(SharedPref.HEART_RHYTHM, "unknown");
        respiratoryRate = SharedPref.read(SharedPref.RR, respiratoryRate);
        oxygenSaturation = SharedPref.read(SharedPref.O2STAS, oxygenSaturation);
        temperature = SharedPref.read(SharedPref.TEMP, temperature);
        bloodGlucose = SharedPref.read(SharedPref.BLOOD_GLOUCE, bloodGlucose);
        strEye = SharedPref.read(SharedPref.EYE, "");
        strMotor = SharedPref.read(SharedPref.MOTOR, "");
        strVerbal = SharedPref.read(SharedPref.VERBAL, "");

        if(strHeartRythm != null){
            if(strHeartRythm.equals(heartRhythm.regular.name())){
                btnRegular.setChecked(true);
            }
            if(strHeartRythm.equals(heartRhythm.irregular.name())){
                btnIrregular.setChecked(true);
            }
        }

        if(bloodPressureSystolic != -1 ){
            strBloodPressureSystolic = String.valueOf(bloodPressureSystolic);
            txtBloodPressureSystolic.setText(strBloodPressureSystolic);
        }
        if(bloodPressureDiastolic != -1 ){
            strBloodPressureDyastolic = String.valueOf(bloodPressureDiastolic);
            txtBloodPressureDiastolic.setText(strBloodPressureDyastolic);
        }
        if(heartRate != -1 ){
            strHeartRate = String.valueOf(heartRate);
            txtHeartRate.setText(strHeartRate);
        }
        if(respiratoryRate != -1 ){
            strRespiratoryRate = String.valueOf(respiratoryRate);
            txtRespiratoryRate.setText(strRespiratoryRate);
        }
        if(oxygenSaturation != -1 ){
            stroxygenSaturation = String.valueOf(oxygenSaturation);
            txtOxygenSaturation.setText(stroxygenSaturation);
        }
        if(temperature != -1 ){
            strTemperature = String.valueOf(temperature);
            txtTemperature.setText(strTemperature);
        }
        if(bloodGlucose != -1 ){
            strBloodGlucose = String.valueOf(bloodGlucose);
            txtBloodGlucose.setText(strBloodGlucose);
        }

    }

    //Custom adapter class provides fragments required for the view pager
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}