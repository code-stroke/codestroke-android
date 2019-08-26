package com.simi.codestrokealert.activity.paramedics;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.model.CaseAssessments;

import androidx.appcompat.app.AppCompatActivity;

public class MassActivity  extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back_btn;
    private Button next_btn, unknown_facial_droop
            ,unknown_arm_drift, unknown_weak_grip
            ,unknown_speech_difficulty;
    private RadioButton facial_droop_yes, facial_droop_no
            ,arm_drift_yes, arm_drift_no
            ,weak_grip_yes, weak_grip_no
            ,speech_difficulty_yes, speech_difficulty_no;
    private RadioGroup radio_grp_facial_drop, radio_grp_arm_drift,
            radio_grp_weak_grip,radio_grp_speech_difficulty;
    private String strFacialDroop, strArmDrift, strWeakGrip,
            strSpeechDifficulty, unknown = "unknown";
    private CaseAssessments caseAssessments;
    private enum facial_droop{
        no, yes, unknown
    }
    private enum arm_drift{
        no, yes, unknown
    }
    private enum weak_grip{
        no, yes, unknown
    }
    private enum speech_difficulty{
        no, yes, unknown
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.check_back_full){
            finish();
        }
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
        setContentView(R.layout.activity_mass);
        initViews();
        fillData();

        //Go to previous activity
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    protected void initViews(){
        caseAssessments = new CaseAssessments();
        back_btn = (ImageButton)findViewById(R.id.arrow_back);
        next_btn = (Button)findViewById(R.id.btn_next);
        unknown_facial_droop = (Button)findViewById(R.id.unknown_facial_droop);
        unknown_arm_drift = (Button)findViewById(R.id.unknown_arm_drift);
        unknown_weak_grip = (Button)findViewById(R.id.unknown_weak_grip);
        unknown_speech_difficulty = (Button)findViewById(R.id.unknown_speech_difficulty);
        facial_droop_yes = (RadioButton)findViewById(R.id.facial_droop_yes);
        facial_droop_no = (RadioButton)findViewById(R.id.facial_droop_no);
        arm_drift_yes = (RadioButton)findViewById(R.id.arm_drift_yes);
        arm_drift_no = (RadioButton)findViewById(R.id.arm_drift_no);
        weak_grip_yes = (RadioButton)findViewById(R.id.weak_grip_yes);
        weak_grip_no = (RadioButton)findViewById(R.id.weak_grip_no);
        speech_difficulty_yes = (RadioButton)findViewById(R.id.speech_difficulty_yes);
        speech_difficulty_no = (RadioButton)findViewById(R.id.speech_difficulty_no);
        radio_grp_facial_drop = (RadioGroup) findViewById(R.id.radio_grp_facial_drop);
        radio_grp_arm_drift = (RadioGroup) findViewById(R.id.radio_grp_arm_drift);
        radio_grp_weak_grip = (RadioGroup) findViewById(R.id.radio_grp_weak_grip);
        radio_grp_speech_difficulty = (RadioGroup) findViewById(R.id.radio_grp_speech_difficulty);
        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        unknown_facial_droop.setOnClickListener(this);
        unknown_arm_drift.setOnClickListener(this);
        unknown_weak_grip.setOnClickListener(this);
        unknown_speech_difficulty.setOnClickListener(this);
        facial_droop_yes.setOnClickListener(this);
        facial_droop_no.setOnClickListener(this);
        arm_drift_yes.setOnClickListener(this);
        arm_drift_no.setOnClickListener(this);
        weak_grip_yes.setOnClickListener(this);
        weak_grip_no.setOnClickListener(this);
        speech_difficulty_yes.setOnClickListener(this);
        speech_difficulty_no.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.arrow_back:
                //Go to previous activity
                onBackPressed();
                break;

            case R.id.unknown_facial_droop:
                unknown_facial_droop.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                strFacialDroop = facial_droop.unknown.name();
                SharedPref.write(SharedPref.FACIAL_DROOP, strFacialDroop);
                radio_grp_facial_drop.clearCheck();
                break;

            case R.id.facial_droop_yes:
                unknown_facial_droop.setBackgroundResource(R.drawable.rounded_btn);
                strFacialDroop = facial_droop.yes.name();
                SharedPref.write(SharedPref.FACIAL_DROOP, strFacialDroop);
                facial_droop_yes.setChecked(true);
                break;

            case R.id.facial_droop_no:
                unknown_facial_droop.setBackgroundResource(R.drawable.rounded_btn);
                strFacialDroop = facial_droop.no.name();
                SharedPref.write(SharedPref.FACIAL_DROOP, strFacialDroop);
                facial_droop_no.setChecked(true);
                break;

            case R.id.unknown_arm_drift:
                unknown_arm_drift.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                strArmDrift = arm_drift.unknown.name();
                SharedPref.write(SharedPref.ARM_DRIFT, strArmDrift);
                radio_grp_arm_drift.clearCheck();
                break;

            case R.id.arm_drift_yes:
                unknown_arm_drift.setBackgroundResource(R.drawable.rounded_btn);
                strArmDrift = arm_drift.yes.name();
                SharedPref.write(SharedPref.ARM_DRIFT, strArmDrift);
                arm_drift_yes.setChecked(true);
                break;

            case R.id.arm_drift_no:
                unknown_arm_drift.setBackgroundResource(R.drawable.rounded_btn);
                strArmDrift = arm_drift.no.name();
                SharedPref.write(SharedPref.ARM_DRIFT, strArmDrift);
                arm_drift_no.setChecked(true);
                break;

            case R.id.unknown_weak_grip:
                unknown_weak_grip.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                strWeakGrip = weak_grip.unknown.name();
                SharedPref.write(SharedPref.WEAK_GRIP, strWeakGrip);
                radio_grp_weak_grip.clearCheck();
                break;

            case R.id.weak_grip_yes:
                unknown_weak_grip.setBackgroundResource(R.drawable.rounded_btn);
                strWeakGrip = weak_grip.yes.name();
                SharedPref.write(SharedPref.WEAK_GRIP, strWeakGrip);
                weak_grip_yes.setChecked(true);
                break;

            case R.id.weak_grip_no:
                unknown_weak_grip.setBackgroundResource(R.drawable.rounded_btn);
                strWeakGrip = weak_grip.no.name();
                SharedPref.write(SharedPref.WEAK_GRIP, strWeakGrip);
                weak_grip_no.setChecked(true);
                break;

            case R.id.unknown_speech_difficulty:
                unknown_speech_difficulty.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                strSpeechDifficulty = speech_difficulty.unknown.name();
                SharedPref.write(SharedPref.SPEECH_DIFFICULTY, strSpeechDifficulty);
                radio_grp_speech_difficulty.clearCheck();
                break;

            case R.id.speech_difficulty_yes:
                unknown_speech_difficulty.setBackgroundResource(R.drawable.rounded_btn);
                strSpeechDifficulty = speech_difficulty.yes.name();
                SharedPref.write(SharedPref.SPEECH_DIFFICULTY, strSpeechDifficulty);
                speech_difficulty_yes.setChecked(true);
                break;

            case R.id.speech_difficulty_no:
                unknown_speech_difficulty.setBackgroundResource(R.drawable.rounded_btn);
                strSpeechDifficulty = speech_difficulty.no.name();
                SharedPref.write(SharedPref.SPEECH_DIFFICULTY, strSpeechDifficulty);
                speech_difficulty_no.setChecked(true);
                break;

            case R.id.btn_next:
                Intent intent = new Intent(getBaseContext(), VitalSingsActivity.class);
                startActivity(intent);

        }
    }


  protected void fillData(){
      strArmDrift = SharedPref.read(SharedPref.ARM_DRIFT, unknown);
      strFacialDroop = SharedPref.read(SharedPref.FACIAL_DROOP, unknown);
      strWeakGrip = SharedPref.read(SharedPref.WEAK_GRIP, unknown);
      strSpeechDifficulty = SharedPref.read(SharedPref.SPEECH_DIFFICULTY, unknown);
      if(strArmDrift != null){
          if(strArmDrift.equals(arm_drift.yes.name())){
              arm_drift_yes.setChecked(true);
          }
          if(strArmDrift.equals(arm_drift.no.name())){
              arm_drift_no.setChecked(true);
          }
          if(strArmDrift.equals(arm_drift.unknown.name())){
              unknown_arm_drift.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          }
      }
      if(strFacialDroop != null){
          if(strFacialDroop.equals(facial_droop.yes.name())){
              facial_droop_yes.setChecked(true);
          }
          if(strFacialDroop.equals(facial_droop.no.name())){
              facial_droop_no.setChecked(true);
          }
          if(strFacialDroop.equals(facial_droop.unknown.name())){
              unknown_facial_droop.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          }
      }
      if(strWeakGrip != null){
          if(strWeakGrip.equals(weak_grip.yes.name())){
              weak_grip_yes.setChecked(true);
          }
          if(strWeakGrip.equals(weak_grip.no.name())){
              weak_grip_no.setChecked(true);
          }
          if(strWeakGrip.equals(weak_grip.unknown.name())){
              unknown_weak_grip.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          }
      }
      if(strSpeechDifficulty != null){
          if(strSpeechDifficulty.equals(speech_difficulty.yes.name())){
              speech_difficulty_yes.setChecked(true);
          }
          if(strSpeechDifficulty.equals(speech_difficulty.no.name())){
              speech_difficulty_no.setChecked(true);
          }
          if(strSpeechDifficulty.equals(speech_difficulty.unknown.name())){
              unknown_speech_difficulty.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          }
      }
  }

}
