package com.simi.codestrokealert.activity.paramedics;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import androidx.appcompat.app.AppCompatActivity;


public class ClinicalAssessmentActivity extends AppCompatActivity {

    private ImageButton back_btn;
    private Button next_page, unknown;
    private RadioButton rd_yes_cannula, rd_no_cannula;
    private RadioGroup radio_grp_cannula;
    private String strCannula;
    private enum Cannula{yes, no}

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
        setContentView(R.layout.activity_clinical_assessment);
        initViews();
        fillData();

        rd_no_cannula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if(isCheck){
                    strCannula = Cannula.no.name();
                    SharedPref.write(SharedPref.CANNULA, strCannula);
                    unknown.setBackgroundResource(R.drawable.rounded_btn);
                }
            }
        });

        rd_yes_cannula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if(isCheck){
                    strCannula = Cannula.yes.name();
                    SharedPref.write(SharedPref.CANNULA, strCannula);
                    unknown.setBackgroundResource(R.drawable.rounded_btn);
                }
            }
        });

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strCannula = "unknown";
                SharedPref.write(SharedPref.CANNULA, strCannula);
                unknown.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                radio_grp_cannula.clearCheck();
            }
        });

        //Go to previous activity
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileSummaryActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void initViews(){
        back_btn = (ImageButton)findViewById(R.id.arrow_back);
        next_page = (Button)findViewById(R.id.btn_next);
        unknown   = (Button)findViewById(R.id.unknown);
        rd_no_cannula = (RadioButton)findViewById(R.id.rd_no_cannula);
        rd_yes_cannula = (RadioButton)findViewById(R.id.rd_yes_cannula);
        radio_grp_cannula = (RadioGroup)findViewById(R.id.radio_grp_cannula);
    }

    protected void fillData(){
        strCannula = SharedPref.read(SharedPref.CANNULA, "unknown");
        if(strCannula != null){
            if(strCannula.equals(Cannula.yes.name())){
                rd_yes_cannula.setChecked(true);
            }
            if(strCannula.equals(Cannula.no.name())){
                rd_no_cannula.setChecked(true);
            }
            if(strCannula.equals("unknown")){
                unknown.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

}
