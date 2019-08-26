package com.simi.codestrokealert.activity.paramedics;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.adapter.CustomAdapter;


import androidx.appcompat.app.AppCompatActivity;


public class RaceScoreActivity extends AppCompatActivity {

    private ImageButton back_btn;
    private Button next_btn;
    private String[] facial_palsy = {"Absent", "Mild", "Mod-Severe"};
    private String[] arm_motor_impairment = {"Normal-Mild", "Mod", "Severe"};
    private String[] leg_motor_impairment = {"Normal-Mild", "Mod", "Severe"};
    private String[] head_and_gaze_deviation = {"Absent", "Present"};
    private ListView facial_palsy_list, arm_motor_impairment_list, leg_motor_impairment_list, deviation_list;
    private CustomAdapter facialPalsyAdapter, armMotorImpairmentAdapter, legMotorImpairmentAdapter, deviationAdapter;
    private String strFacialPalsy, strArmMotorImp, strLegMotorImp, strDeviation;

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
        setContentView(R.layout.activity_race_score);
        initViews();


        //Go to previous activity
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ClinicalAssessmentActivity.class );
                startActivity(intent);
            }
        });

        // set the adapter to fill the data in ListView
        facialPalsyAdapter = new CustomAdapter(getApplicationContext(), facial_palsy);
        facial_palsy_list.setAdapter(facialPalsyAdapter);
        facial_palsy_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        facial_palsy_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                facialPalsyAdapter.setSelectedIndex(position);
                facialPalsyAdapter.notifyDataSetChanged();
                strFacialPalsy = facial_palsy[position];
                SharedPref.write(SharedPref.FACIAL_PALSY_RACE_TEXT, strFacialPalsy);
                SharedPref.write(SharedPref.FACIAL_PALSY_RACE, position);

            }
        });

       armMotorImpairmentAdapter = new CustomAdapter(getApplicationContext(), arm_motor_impairment);
        arm_motor_impairment_list.setAdapter(armMotorImpairmentAdapter);

        arm_motor_impairment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                armMotorImpairmentAdapter.setSelectedIndex(position);
                armMotorImpairmentAdapter.notifyDataSetChanged();
                strArmMotorImp = arm_motor_impairment[position];
                SharedPref.write(SharedPref.ARM_MOTOR_IMPAIR_TEXT, strArmMotorImp);
                SharedPref.write(SharedPref.ARM_MOTOR_IMPAIR, position);


            }
        });

         legMotorImpairmentAdapter = new CustomAdapter(getApplicationContext(), leg_motor_impairment);
        leg_motor_impairment_list.setAdapter(legMotorImpairmentAdapter);

        leg_motor_impairment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                legMotorImpairmentAdapter.setSelectedIndex(position);
                legMotorImpairmentAdapter.notifyDataSetChanged();
                strLegMotorImp = leg_motor_impairment[position];
                SharedPref.write(SharedPref.LEG_MOTOR_IMPAIR_TEXT, strLegMotorImp);
                SharedPref.write(SharedPref.LEG_MOTOR_IMPAIR, position);


            }
        });

        deviationAdapter = new CustomAdapter(getApplicationContext(), head_and_gaze_deviation);
        deviation_list.setAdapter(deviationAdapter);

        deviation_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                deviationAdapter.setSelectedIndex(position);
                deviationAdapter.notifyDataSetChanged();
                strDeviation = head_and_gaze_deviation[position];
                SharedPref.write(SharedPref.HEAD_GAZE_DEVIATE_TEXT, strDeviation);
                SharedPref.write(SharedPref.HEAD_GAZE_DEVIATE, position);
            }
        });
        fillData();

    }

    protected void initViews(){
        facial_palsy_list = (ListView) findViewById(R.id.facial_palsy_list);
        arm_motor_impairment_list = (ListView)findViewById(R.id.arm_motor_impairment_list);
        leg_motor_impairment_list = (ListView)findViewById(R.id.leg_motor_impairment_list);
        deviation_list = (ListView)findViewById(R.id.deviation_list);
        back_btn = (ImageButton)findViewById(R.id.arrow_back);
        next_btn = (Button)findViewById(R.id.btn_next);
    }

  protected void fillData(){
      strFacialPalsy = SharedPref.read(SharedPref.FACIAL_PALSY_RACE_TEXT, "");
      strArmMotorImp = SharedPref.read(SharedPref.ARM_MOTOR_IMPAIR_TEXT, "");
      strLegMotorImp = SharedPref.read(SharedPref.LEG_MOTOR_IMPAIR_TEXT, "");
      strDeviation = SharedPref.read(SharedPref.HEAD_GAZE_DEVIATE_TEXT, "");

      if(strFacialPalsy != null){
         if(strFacialPalsy.equals("Absent")){
              facialPalsyAdapter.setSelectedIndex(0);
              facialPalsyAdapter.notifyDataSetChanged();
          }
          if(strFacialPalsy.equals("Mild")){
              facialPalsyAdapter.setSelectedIndex(1);
              facialPalsyAdapter.notifyDataSetChanged();
          }
          if(strFacialPalsy.equals("Mod-Severe")){
              facialPalsyAdapter.setSelectedIndex(2);
              facialPalsyAdapter.notifyDataSetChanged();
          }
      }

      if(strArmMotorImp != null) {
          if (strArmMotorImp.equals("Normal-Mild")) {
              armMotorImpairmentAdapter.setSelectedIndex(0);
              armMotorImpairmentAdapter.notifyDataSetChanged();
          }
          if (strArmMotorImp.equals("Mod")) {
              armMotorImpairmentAdapter.setSelectedIndex(1);
              armMotorImpairmentAdapter.notifyDataSetChanged();
          }
          if (strArmMotorImp.equals("Severe")) {
              armMotorImpairmentAdapter.setSelectedIndex(2);
              armMotorImpairmentAdapter.notifyDataSetChanged();
          }
      }
      if(strLegMotorImp != null){
          if(strLegMotorImp.equals("Normal-Mild")){
              legMotorImpairmentAdapter.setSelectedIndex(0);
              legMotorImpairmentAdapter.notifyDataSetChanged();
          }
          if(strLegMotorImp.equals("Mod")){
              legMotorImpairmentAdapter.setSelectedIndex(1);
              legMotorImpairmentAdapter.notifyDataSetChanged();
          }
          if(strLegMotorImp.equals("Severe")){
              legMotorImpairmentAdapter.setSelectedIndex(2);
              legMotorImpairmentAdapter.notifyDataSetChanged();
          }
      }
      if(strDeviation != null){
          if(strDeviation.equals("Absent")){
              deviationAdapter.setSelectedIndex(0);
              deviationAdapter.notifyDataSetChanged();
          }
          if(strDeviation.equals("Present")){
              deviationAdapter.setSelectedIndex(1);
              deviationAdapter.notifyDataSetChanged();
          }
      }
  }

}
