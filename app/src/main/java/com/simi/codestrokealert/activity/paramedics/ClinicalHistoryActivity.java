package com.simi.codestrokealert.activity.paramedics;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;


import com.simi.codestrokealert.BasicAuthInterceptor;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.CustomDatePicker;
import com.simi.codestrokealert.Entry;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.TokenCalculator;
import com.simi.codestrokealert.model.CaseHistories;
import com.simi.codestrokealert.model.rest.RequestInterface;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.simi.codestrokealert.Constants.AUTH_HEADER;


public class ClinicalHistoryActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_next_page_clinical_history;
    int id;
    private ImageButton calendarImage;
    private EditText last_dose, past_medical_history, medications, situation, weight;
    private DatePickerDialog datePickerDialog;
    private RadioButton yes_anticoags, no_anticoags;
    private CheckBox checkBoxIHD, checkBoxDM, checkBoxStroke,
            checkBoxEpilepsy, checkBoxAF, checkBoxOtherNeurologicalConditions
            ,checkBoxApixaban, checkBoxRivaroxaban, checkBoxWarfarin
            ,checkBoxDabigatran, checkBoxHeparin;
    private enum Anticoags{
        no, yes, unknown
    }
    private String pastMdHistory = "", strMedication = "", strAnticoags, strLastDose,
            strHOPC, strWeight, signoff_first_name, signoff_last_name, signoff_role, strPMHX, strMed;
    private String strIHD, strDM, strStroke, strEpilepsy, strAF, strOtherNeu, strApixaban,
            strDabigatran, strRivaroxaban, strHeparin, strWarfarin;
    private Set<String> pmhx = new HashSet<String>();
    private String[] pmhx_array = new String[7];
    private CaseHistories caseHistories;
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

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
        setContentView(R.layout.activity_clinical_history);
        initViews();
        fillData();

        checkBoxIHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxIHD.isChecked()){
                    strIHD = "IHD, ";
                }else{
                    strIHD = "";
                }
                SharedPref.write(SharedPref.IHD, strIHD);
            }
        });

        checkBoxDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxDM.isChecked()){
                    strDM = "DM, ";
                }else{
                    strDM = "";
                }
                SharedPref.write(SharedPref.DM, strDM);
            }
        });

        checkBoxStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxStroke.isChecked()){
                    strStroke = "Stroke, ";
                }else{
                    strStroke = "";
                }
                SharedPref.write(SharedPref.STROKE, strStroke);
            }
        });

        checkBoxEpilepsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxEpilepsy.isChecked()){
                    strEpilepsy = "Epilepsy, ";
                }else{
                    strEpilepsy = "";
                }
                SharedPref.write(SharedPref.EPILEPSY, strEpilepsy);
            }
        });

        checkBoxAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxAF.isChecked()){
                    strAF= "AF, ";
                }else{
                    strAF = "";
                }
                SharedPref.write(SharedPref.AF, strAF);
            }
        });

        checkBoxOtherNeurologicalConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxOtherNeurologicalConditions.isChecked()){
                    strOtherNeu = "OtherNeurologicalConditions, ";
                }else{
                    strOtherNeu = "";
                }
                SharedPref.write(SharedPref.OTHER_NEU, strOtherNeu);
            }
        });

        checkBoxApixaban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxApixaban.isChecked()){
                    strApixaban = "Apixaban, ";
                }else{
                    strApixaban = "";
                }
                SharedPref.write(SharedPref.APIXABAN, strApixaban);
            }
        });

        checkBoxRivaroxaban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxApixaban.isChecked()){
                    strRivaroxaban = "Rivaroxaban, ";
                }else{
                    strRivaroxaban = "";
                }
                SharedPref.write(SharedPref.RIVAROXABAN, strRivaroxaban);
            }
        });

        checkBoxWarfarin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxWarfarin.isChecked()){
                    strWarfarin = "Warfarin, ";
                }else{
                    strWarfarin = "";
                }
                SharedPref.write(SharedPref.WARFARIN, strWarfarin);
            }
        });

        checkBoxDabigatran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxDabigatran.isChecked()){
                    strDabigatran = "Dabigatran, ";
                }else{
                    strDabigatran = "";
                }
                SharedPref.write(SharedPref.DABIGATRAN, strDabigatran);
            }
        });

        checkBoxHeparin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxHeparin.isChecked()){
                    strHeparin= "Heparin, ";
                }else{
                    strHeparin = "";
                }
                SharedPref.write(SharedPref.HEPARIN, strHeparin);
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.check_back = true;
        saveData();
    }

    protected void initViews(){
        caseHistories = new CaseHistories();
        btn_next_page_clinical_history = (Button)findViewById(R.id.btn_next);
        calendarImage = (ImageButton) findViewById(R.id.calenderImage);
        past_medical_history = (EditText) findViewById(R.id.past_medical_history);
        last_dose = (EditText) findViewById(R.id.et_last_dose);
        medications = (EditText) findViewById(R.id.medications);
        weight = (EditText) findViewById(R.id.weight);
        situation = (EditText)findViewById(R.id.situation);
        yes_anticoags = (RadioButton) findViewById(R.id.yes_anticoags);
        no_anticoags = (RadioButton) findViewById(R.id.no_anticoags);
        checkBoxIHD = (CheckBox) findViewById(R.id.checkBoxIHD);
        checkBoxDM = (CheckBox) findViewById(R.id.checkBoxDM);
        checkBoxStroke = (CheckBox) findViewById(R.id.checkBoxStroke);
        checkBoxEpilepsy = (CheckBox) findViewById(R.id.checkBoxEpilepsy);
        checkBoxAF = (CheckBox) findViewById(R.id.checkBoxAF);
        checkBoxOtherNeurologicalConditions = (CheckBox) findViewById(R.id.checkBoxOtherNeurologicalConditions);
        checkBoxApixaban = (CheckBox) findViewById(R.id.checkBoxApixaban);
        checkBoxRivaroxaban = (CheckBox) findViewById(R.id.checkBoxRivaroxaban);
        checkBoxWarfarin = (CheckBox) findViewById(R.id.checkBoxWarfarin);
        checkBoxDabigatran = (CheckBox) findViewById(R.id.checkBoxDabigatran);
        checkBoxHeparin = (CheckBox) findViewById(R.id.checkBoxHeparin);

        calendarImage.setOnClickListener(this);
        checkBoxIHD.setOnClickListener(this);
        checkBoxDM.setOnClickListener(this);
        checkBoxStroke.setOnClickListener(this);
        checkBoxEpilepsy.setOnClickListener(this);
        checkBoxAF.setOnClickListener(this);
        checkBoxOtherNeurologicalConditions.setOnClickListener(this);
        checkBoxApixaban.setOnClickListener(this);
        checkBoxRivaroxaban.setOnClickListener(this);
        checkBoxWarfarin.setOnClickListener(this);
        checkBoxDabigatran.setOnClickListener(this);
        checkBoxHeparin.setOnClickListener(this);
        yes_anticoags.setOnClickListener(this);
        no_anticoags.setOnClickListener(this);
        btn_next_page_clinical_history.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.calenderImage:
                new CustomDatePicker(ClinicalHistoryActivity.this, R.id.et_last_dose, R.id.calenderImage);
                break;

            case R.id.btn_next:
                goToNextActivity();
                break;
        }
    }

    protected void goToNextActivity(){

        Entry e = new Entry(Entry.OTPType.TOTP, Constants.shared_secret, 300, 6, "TOTP", TokenCalculator.HashAlgorithm.SHA1);
        e.updateOTP();
        e.setLastUsed(System.currentTimeMillis());

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(Constants.username, Constants.password, e.getCurrentOTP()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface updateCasesApi = retrofit.create(RequestInterface.class);
        saveData();
        int id = SharedPref.read(SharedPref.CASE_ID, -1);
        caseHistories.setCase_id(id);
        caseHistories.setPmhx(SharedPref.read(SharedPref.PMHX, ""));
        caseHistories.setMeds(SharedPref.read(SharedPref.MEDS, ""));
        caseHistories.setAnticoags(SharedPref.read(SharedPref.ANTICOAGS, "unknown"));
        strLastDose = SharedPref.read(SharedPref.ANTICOAGS_LAST_DOSE, "");
        if(!strLastDose.isEmpty()) {
            caseHistories.setAnticoags_last_dose(strLastDose);
        }
        caseHistories.setHopc(SharedPref.read(SharedPref.HOPC, ""));
        strWeight = SharedPref.read(SharedPref.WEIGHT, "");
        if(!strWeight.isEmpty()) {
            caseHistories.setWeight(Float.parseFloat(strWeight));
        }
        signoff_first_name = SharedPref.read(SharedPref.FIRST_NAME, null);
        signoff_last_name = SharedPref.read(SharedPref.LAST_NAME, null);
        signoff_role = SharedPref.read(SharedPref.SIGNOFF_ROLE, null);
        caseHistories.setSignoff_first_name(signoff_first_name);
        caseHistories.setSignoff_last_name(signoff_last_name);
        caseHistories.setSignoff_role(signoff_role);

        Call<CaseHistories> response = updateCasesApi.updateCase(id, caseHistories);
        response.enqueue(new Callback<CaseHistories>() {
            @Override
            public void onResponse(Call<CaseHistories> call, Response<CaseHistories> response) {
                CaseHistories resp = response.body();
                if(resp != null){
                   startActivity(new Intent(getBaseContext(), MassActivity.class));

                }

            }

            @Override
            public void onFailure(Call<CaseHistories> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void saveData( ){
        strPMHX = past_medical_history.getText().toString();
        strMed = medications.getText().toString();
        strLastDose = last_dose.getText().toString();
        strHOPC = situation.getText().toString();
        strWeight = weight.getText().toString();
        strIHD = SharedPref.read(SharedPref.IHD, "");
        strDM = SharedPref.read(SharedPref.DM, "");
        strStroke = SharedPref.read(SharedPref.STROKE, "");
        strEpilepsy = SharedPref.read(SharedPref.EPILEPSY, "");
        strAF = SharedPref.read(SharedPref.AF, "");
        strOtherNeu = SharedPref.read(SharedPref.OTHER_NEU, "");
        pastMdHistory = strIHD + strDM + strEpilepsy + strStroke + strAF + strOtherNeu + strPMHX;
        strApixaban = SharedPref.read(SharedPref.APIXABAN, "");
        strRivaroxaban = SharedPref.read(SharedPref.RIVAROXABAN, "");
        strWarfarin = SharedPref.read(SharedPref.WARFARIN, "");
        strDabigatran = SharedPref.read(SharedPref.DABIGATRAN, "");
        strHeparin = SharedPref.read(SharedPref.HEPARIN, "");
        strMedication = strApixaban + strRivaroxaban + strWarfarin + strDabigatran + strHeparin + strMed;
        SharedPref.write(SharedPref.PMHX, pastMdHistory);
        SharedPref.write(SharedPref.PMHX_TEXT, strPMHX);
        SharedPref.write(SharedPref.MEDS, strMedication);
        SharedPref.write(SharedPref.MED_TEXT, strMed);
        SharedPref.write(SharedPref.ANTICOAGS, strAnticoags);
        SharedPref.write(SharedPref.ANTICOAGS_LAST_DOSE, strLastDose);
        SharedPref.write(SharedPref.HOPC, strHOPC);
        SharedPref.write(SharedPref.WEIGHT, strWeight);
    }

    protected void fillData(){
        pastMdHistory = SharedPref.read(SharedPref.PMHX, "");
        strPMHX = SharedPref.read(SharedPref.PMHX_TEXT, "");
        strMedication = SharedPref.read(SharedPref.MEDS, "");
        strMed = SharedPref.read(SharedPref.MED_TEXT, "");
        strAnticoags = SharedPref.read(SharedPref.ANTICOAGS, null);
        strLastDose = SharedPref.read(SharedPref.ANTICOAGS_LAST_DOSE, null);
        strHOPC = SharedPref.read(SharedPref.HOPC, null);
        strWeight = SharedPref.read(SharedPref.WEIGHT, null);

        if(pastMdHistory != null){
            if(pastMdHistory.contains("IHD")){
                checkBoxIHD.setChecked(true);
            }
            if(pastMdHistory.contains("DM")){
                checkBoxDM.setChecked(true);
            }
            if(pastMdHistory.contains("Stroke")){
                checkBoxStroke.setChecked(true);
            }
            if(pastMdHistory.contains("Epilepsy")){
                checkBoxEpilepsy.setChecked(true);
            }
            if(pastMdHistory.contains("AF")){
                checkBoxAF.setChecked(true);
            }
            if(pastMdHistory.contains("Other neurological conditions")){
                checkBoxOtherNeurologicalConditions.setChecked(true);
            }
        }

        if(strPMHX != null){
            past_medical_history.setText(strPMHX);
        }

        if(strMedication != null){
            if(strMedication.contains("Apixaban")){
                checkBoxApixaban.setChecked(true);
            }
            if(strMedication.contains("Rivaroxaban")){
                checkBoxRivaroxaban.setChecked(true);
            }
            if(strMedication.contains("Warfarin")){
                checkBoxWarfarin.setChecked(true);
            }
            if(strMedication.contains("Dabigatran")){
                checkBoxDabigatran.setChecked(true);
            }
            if(strMedication.contains("Heparin")){
                checkBoxHeparin.setChecked(true);
            }
        }

        if(strMed != null){
            medications.setText(strMed);
        }

        if(strAnticoags != null){
            if(strAnticoags.equals("no")){
                no_anticoags.setChecked(true);
            }
            if(strAnticoags.equals("yes")){
                yes_anticoags.setChecked(true);
            }
        }

        if(strLastDose != null){
            last_dose.setText(strLastDose);
        }

        if(strHOPC != null){
            situation.setText(strHOPC);
        }

        if(strWeight != null){
            weight.setText(strWeight);
        }
    }
}