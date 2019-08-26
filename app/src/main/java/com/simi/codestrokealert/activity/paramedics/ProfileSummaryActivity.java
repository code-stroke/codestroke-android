package com.simi.codestrokealert.activity.paramedics;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;


import com.simi.codestrokealert.BasicAuthInterceptor;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.Entry;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.TokenCalculator;
import com.simi.codestrokealert.adapter.ExpandableListAdapter;
import com.simi.codestrokealert.model.CaseAssessments;
import com.simi.codestrokealert.model.Cases;
import com.simi.codestrokealert.model.Cases1;
import com.simi.codestrokealert.model.ChildItem;
import com.simi.codestrokealert.model.rest.RequestInterface;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.simi.codestrokealert.Constants.AUTH_HEADER;


public class ProfileSummaryActivity extends AppCompatActivity {

    private ImageButton back_btn;
    private Button drop_off;
    private CaseAssessments caseAssessments;
    private Cases cases = new Cases();
    int id;
    private String signoff_first_name, signoff_last_name, signoff_role;
    private byte indexFacialPalsy = (byte)255, indexArmMotorImp = (byte) 255,
            indexLegMotorImp = (byte) 255, indexDeviation = (byte) 255;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<ChildItem>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get translucent status bar and push activity layout to status bar for the gradient to work for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_profile_summary);
        initViews();

        caseAssessments = new CaseAssessments();

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        //Go to previous activity
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        drop_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showAlert();
            }
        });
    }

    protected void showAlert(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ProfileSummaryActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ProfileSummaryActivity.this);
        }
        builder.setTitle("Drop Off")
                .setMessage(R.string.drop_off_dialog)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendCaseAssessment(caseAssessments);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    protected void initViews(){
        expListView = (ExpandableListView) findViewById(R.id.list_view_exp);
        back_btn = (ImageButton)findViewById(R.id.arrow_back);
        drop_off = (Button)findViewById(R.id.drop_off);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ChildItem>>();

        // Adding child data
        listDataHeader.add("Details");
        listDataHeader.add("History");
        listDataHeader.add("Mass");
        listDataHeader.add("Vitals");
        listDataHeader.add("RACE");
        listDataHeader.add("18G IV");

        List<ChildItem> childItemsDetails = new ArrayList<>();
        childItemsDetails.add( new ChildItem("Name: ", SharedPref.read(SharedPref.FIRST_NAME, null) +
        " " + SharedPref.read(SharedPref.LAST_NAME, null)));
        childItemsDetails.add( new ChildItem("Age: ", SharedPref.read(SharedPref.AGE, null)));
        childItemsDetails.add( new ChildItem("Gender: ", SharedPref.read(SharedPref.GENDER, null)));
        childItemsDetails.add( new ChildItem("Address: ", SharedPref.read(SharedPref.ADDRESS, null)));
        childItemsDetails.add( new ChildItem("Last Seen: ", SharedPref.read(SharedPref.LAST_WELL, null)));
        childItemsDetails.add( new ChildItem("NOK: ", SharedPref.read(SharedPref.NOK, null)));
        childItemsDetails.add( new ChildItem("NOK Contact: ", SharedPref.read(SharedPref.NOK_PHONE, null)));

        List<ChildItem> childItemsHistory = new ArrayList<>();
        childItemsHistory.add( new ChildItem("Past Medical History: ", SharedPref.read(SharedPref.PMHX, null)));
        childItemsHistory.add( new ChildItem("Medication: ", SharedPref.read(SharedPref.MEDS, null)));
        childItemsHistory.add( new ChildItem("Anticoagulants: ", SharedPref.read(SharedPref.ANTICOAGS, null)));
        childItemsHistory.add( new ChildItem("Situation: ", SharedPref.read(SharedPref.HOPC, null)));
        childItemsHistory.add( new ChildItem("Weight: ", SharedPref.read(SharedPref.WEIGHT, null)));

        List<ChildItem> childItemsMass = new ArrayList<>();
        childItemsMass.add( new ChildItem("Facial droop: ", SharedPref.read(SharedPref.FACIAL_DROOP, null)));
        childItemsMass.add( new ChildItem("Arm drift: ", SharedPref.read(SharedPref.ARM_DRIFT, null)));
        childItemsMass.add( new ChildItem("Weak grip: ", SharedPref.read(SharedPref.WEAK_GRIP, null)));
        childItemsMass.add( new ChildItem("Speech Difficulty: ", SharedPref.read(SharedPref.SPEECH_DIFFICULTY, null)));

        List<ChildItem> childItemsVitals = new ArrayList<>();
        childItemsVitals.add( new ChildItem("Blood Pressure DIASTOLIC: ", String.valueOf(SharedPref.read(SharedPref.BP_DIASTOLIC, -1))));
        childItemsVitals.add(new ChildItem("Blood Pressure Systolic", String.valueOf(SharedPref.read(SharedPref.BP_SYSTOLIC, -1))));
        childItemsVitals.add( new ChildItem("Heart Rate: ", String.valueOf(SharedPref.read(SharedPref.HEART_RATE, -1))));
        childItemsVitals.add( new ChildItem("Heart Rhythm: ", SharedPref.read(SharedPref.HEART_RHYTHM, null)));
        childItemsVitals.add( new ChildItem("Respiratory Rate: ", String.valueOf(SharedPref.read(SharedPref.RR, -1))));
        childItemsVitals.add( new ChildItem("Oxygen Saturation: ", String.valueOf(SharedPref.read(SharedPref.O2STAS, -1))));
        childItemsVitals.add( new ChildItem("Temperature: ", String.valueOf(SharedPref.read(SharedPref.TEMP, -1))));
        childItemsVitals.add( new ChildItem("Blood Glucose: ", String.valueOf(SharedPref.read(SharedPref.BLOOD_GLOUCE, -1))));
        childItemsVitals.add( new ChildItem("GCS: ", SharedPref.read(SharedPref.GCS_TEXT, null)));

        List<ChildItem> childItemsRACE  = new ArrayList<>();
        childItemsRACE.add( new ChildItem("Facial Palsy: ", SharedPref.read(SharedPref.FACIAL_PALSY_RACE_TEXT, null)));
        childItemsRACE.add( new ChildItem("Arm Motor Impairment: ", SharedPref.read(SharedPref.ARM_MOTOR_IMPAIR_TEXT, null)));
        childItemsRACE.add( new ChildItem("Leg Motor Impairment: ", SharedPref.read(SharedPref.LEG_MOTOR_IMPAIR_TEXT, null)));
        childItemsRACE.add( new ChildItem("Head and Gaze Deviation: ", SharedPref.read(SharedPref.HEAD_GAZE_DEVIATE_TEXT, null)));

        List<ChildItem> childItems18GIV = new ArrayList<>();
        childItems18GIV.add( new ChildItem("18G IV Cannula in Cub Fossa: ", SharedPref.read(SharedPref.CANNULA, null)));

        listDataChild.put(listDataHeader.get(0), childItemsDetails);
        listDataChild.put(listDataHeader.get(1), childItemsHistory);
        listDataChild.put(listDataHeader.get(2), childItemsMass);
        listDataChild.put(listDataHeader.get(3), childItemsVitals);
        listDataChild.put(listDataHeader.get(4), childItemsRACE);
        listDataChild.put(listDataHeader.get(5), childItems18GIV);

    }

    protected void sendCaseAssessment(CaseAssessments caseAssessments){

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

        RequestInterface updateCasesAssessmentApi = retrofit.create(RequestInterface.class);
        id = SharedPref.read(SharedPref.CASE_ID, -1);
        int position_fp =  SharedPref.read(SharedPref.FACIAL_PALSY_RACE, -1);
        int position_arm =  SharedPref.read(SharedPref.ARM_MOTOR_IMPAIR, -1);
        int position_leg =  SharedPref.read(SharedPref.LEG_MOTOR_IMPAIR, -1);
        int position_head =  SharedPref.read(SharedPref.HEAD_GAZE_DEVIATE, -1);
        indexFacialPalsy = (byte)position_fp;
        indexArmMotorImp = (byte)position_arm;
        indexDeviation = (byte)position_head;
        indexLegMotorImp = (byte)position_leg;
        signoff_first_name = SharedPref.read(SharedPref.FIRST_NAME, null);
        signoff_last_name = SharedPref.read(SharedPref.LAST_NAME, null);
        signoff_role = SharedPref.read(SharedPref.SIGNOFF_ROLE, null);
        caseAssessments.setFacial_droop(SharedPref.read(SharedPref.FACIAL_DROOP, "unknown"));
        caseAssessments.setArm_drift(SharedPref.read(SharedPref.ARM_DRIFT, "unknown"));
        caseAssessments.setWeak_grip(SharedPref.read(SharedPref.WEAK_GRIP, "unknown"));
        caseAssessments.setSpeech_difficulty(SharedPref.read(SharedPref.SPEECH_DIFFICULTY, "unknown"));
        caseAssessments.setBp_diastolic(SharedPref.read(SharedPref.BP_DIASTOLIC, -1));
        caseAssessments.setBp_systolic(SharedPref.read(SharedPref.BP_SYSTOLIC, -1));
        caseAssessments.setHeart_rate(SharedPref.read(SharedPref.HEART_RATE, -1));
        caseAssessments.setHeart_rhythm(SharedPref.read(SharedPref.HEART_RHYTHM, "unknown"));
        caseAssessments.setRr(SharedPref.read(SharedPref.RR, -1));
        caseAssessments.setO2sats(SharedPref.read(SharedPref.O2STAS, -1));
        caseAssessments.setTemp(SharedPref.read(SharedPref.TEMP, -1));
        caseAssessments.setBlood_glucose(SharedPref.read(SharedPref.BLOOD_GLOUCE, -1));
        caseAssessments.setGcs(SharedPref.read(SharedPref.GCS, -1));
        caseAssessments.setFacial_palsy_race(indexFacialPalsy);
        caseAssessments.setArm_motor_impair(indexArmMotorImp);
        caseAssessments.setLeg_motor_impair(indexLegMotorImp);
        caseAssessments.setHead_gaze_deviate(indexDeviation);
        caseAssessments.setCannula(SharedPref.read(SharedPref.CANNULA, "unknown"));
//        caseAssessments.setSignoff_first_name(signoff_first_name);
//        caseAssessments.setSignoff_last_name(signoff_last_name);
//        caseAssessments.setSignoff_role(signoff_role);
        Call<CaseAssessments> response = updateCasesAssessmentApi.sendCaseAssessments(id, caseAssessments);

        response.enqueue(new Callback<CaseAssessments>() {
            @Override
            public void onResponse(Call<CaseAssessments> call, Response<CaseAssessments> response) {
                if(response.isSuccessful()){
                   // updateCaseStatus();
                    Cases1 cases = new Cases1();
                    sendCases(cases);
                }else {
                    Log.i("response: ", response.message());
                }

            }

            @Override
            public void onFailure(Call<CaseAssessments> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

    });
    }

    protected void sendCases(Cases1 caseAssessments){

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

        RequestInterface updateCasesAssessmentApi = retrofit.create(RequestInterface.class);
        id = SharedPref.read(SharedPref.CASE_ID, -1);

        caseAssessments.setStatus("active");
        Call<Cases1> response = updateCasesAssessmentApi.sendCases(id, caseAssessments);

        response.enqueue(new Callback<Cases1>() {
            @Override
            public void onResponse(Call<Cases1> call, Response<Cases1> response) {
                if(response.isSuccessful()){
                    // updateCaseStatus();
                    Toast.makeText(getApplicationContext(), "Data added Successfully", Toast.LENGTH_LONG).show();
                    Constants.username = SharedPref.read(SharedPref.USERNAME, "");
                    Constants.password = SharedPref.read(SharedPref.PASSWORD, "");
                    String pass = SharedPref.read(SharedPref.PASS, "");
                    Constants.shared_secret = SharedPref.read(SharedPref.SHARED_SECRET, "");
                    SharedPreferences.Editor editor = SharedPref.mSharedPref.edit();
                    editor.clear().commit();
                    SharedPref.write(SharedPref.SIGNOFF_ROLE, "");
                    SharedPref.write(SharedPref.USERNAME, Constants.username);
                    SharedPref.write(SharedPref.PASSWORD, Constants.password);
                    SharedPref.write(SharedPref.PASS, pass);
                    SharedPref.write(SharedPref.SHARED_SECRET, Constants.shared_secret);
                    SharedPref.write(SharedPref.IS_SIGNED_IN, true);
                    Constants.check_back_full = true;
                    finish();
                }else {
                    Log.i("response: ", response.message());
                }

            }

            @Override
            public void onFailure(Call<Cases1> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

 /*   protected void updateCaseStatus(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface updateCasesStatusApi = retrofit.create(RequestInterface.class);
        id = SharedPref.read(SharedPref.CASE_ID, -1);
        cases.setStatus("active");

        Call<Cases> resp = updateCasesStatusApi.updatePatientDetails(AUTH_HEADER, id, cases);
        resp.enqueue(new Callback<Cases>() {
            @Override
            public void onResponse(Call<Cases> call, Response<Cases> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Data added Successfully", Toast.LENGTH_LONG);
                    SharedPreferences.Editor editor = SharedPref.mSharedPref.edit();
                    editor.clear().commit();

                }else{
                    Log.i("response: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<Cases> call, Throwable t) {

            }
        });
    }*/

}

