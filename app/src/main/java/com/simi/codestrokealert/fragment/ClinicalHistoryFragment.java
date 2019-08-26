package com.simi.codestrokealert.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.simi.codestrokealert.model.CaseHistoriesResponse;
import com.simi.codestrokealert.model.rest.RequestInterface;

import java.util.List;

import androidx.fragment.app.Fragment;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.simi.codestrokealert.Constants.AUTH_HEADER;


public class ClinicalHistoryFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private EditText past_medical_history, medications, last_dose, situation, weight, last_meal;
    private Button submit;
    private RadioButton yes_anticoagulants, no_anticoagulants, unknown_anticoagulants;
    private ImageButton calender;
    private CaseHistories caseHistories = new CaseHistories();
    private int id;
    private String strPMH, strmed, strLastDose, strSituation, strWeight, strLastMeal, strAnti, signoff_first_name, signoff_last_name, signoff_role;
    private enum Anticoags{
        yes, no, unknown
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_clinical_history, container, false);
        initViews();
        getClinicalHistoriesInfo();

        return rootView;
    }

    protected void initViews(){
        past_medical_history = (EditText)rootView.findViewById(R.id.past_medical_history);
        medications = (EditText)rootView.findViewById(R.id.medications);
        last_dose = (EditText)rootView.findViewById(R.id.et_last_dose);
        situation = (EditText)rootView.findViewById(R.id.situation);
        weight = (EditText)rootView.findViewById(R.id.weight);
        last_meal = (EditText)rootView.findViewById(R.id.last_meal);
        submit = (Button) rootView.findViewById(R.id.submit);
        yes_anticoagulants = (RadioButton)rootView.findViewById(R.id.yes_anticoagulants);
        no_anticoagulants = (RadioButton)rootView.findViewById(R.id.no_anticoagulants);
        unknown_anticoagulants = (RadioButton)rootView.findViewById(R.id.unknown_anticoagulants);
        calender = (ImageButton)rootView.findViewById(R.id.calender);
        yes_anticoagulants.setOnClickListener(this);
        no_anticoagulants.setOnClickListener(this);
        unknown_anticoagulants.setOnClickListener(this);
        submit.setOnClickListener(this);
        calender.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                updateHistory();
                break;

            case R.id.yes_anticoagulants:
                strAnti = Anticoags.yes.name();
                break;

            case R.id.no_anticoagulants:
                strAnti = Anticoags.no.name();
                break;

            case R.id.unknown_anticoagulants:
                strAnti = Anticoags.unknown.name();
                break;

            case R.id.calender:
                new CustomDatePicker(getActivity(), R.id.et_last_dose, R.id.calender);
                break;
        }
    }

    protected void getClinicalHistoriesInfo(){
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

        RequestInterface getCaseHistoriesApi = retrofit.create(RequestInterface.class);
        id = SharedPref.read(SharedPref.PATIANT_ID, -1);
        Call<CaseHistoriesResponse> responseCaseHistories =  getCaseHistoriesApi.getCaseHistories(id);
        responseCaseHistories.enqueue(new Callback<CaseHistoriesResponse>() {
            @Override
            public void onResponse(Call<CaseHistoriesResponse> call, Response<CaseHistoriesResponse> response) {
                List<CaseHistories> resp = response.body().getResult();
                if(resp != null){
                    caseHistories = resp.get(0);
                    updateViews(caseHistories);
                }
            }

            @Override
            public void onFailure(Call<CaseHistoriesResponse> call, Throwable t) {
                if(getActivity() != null) {
                    Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void updateViews(CaseHistories caseHistories){
        String pmhx = caseHistories.getPmhx();
        if(pmhx != null){
            past_medical_history.setText(pmhx);
        }

        String medos = caseHistories.getMeds();
        if(medos != null){
            medications.setText(medos);
        }

        String lastDose = caseHistories.getAnticoags_last_dose();
        if(lastDose != null){
            last_dose.setText(lastDose);
        }

        String hopc = caseHistories.getHopc();
        if(hopc != null){
            situation.setText(hopc);
        }

        Float wgt = caseHistories.getWeight();
        if(wgt != null){
            weight.setText(String.valueOf(wgt));
        }

        String lastMeal = caseHistories.getLast_meal();
        if(lastMeal != null){
            last_meal.setText(lastMeal);
        }

        String anti =  caseHistories.getAnticoags();
        if(anti != null) {
            if (anti.equals("yes")) {
                yes_anticoagulants.setChecked(true);
            }
            if (anti.equals("no")) {
                no_anticoagulants.setChecked(true);
            }
            if (anti.equals("unknown")) {
                unknown_anticoagulants.setChecked(true);
            }
        }

    }

    protected void updateHistory(){

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

        RequestInterface updateCaseHistoriesApi = retrofit.create(RequestInterface.class);
        id = SharedPref.read(SharedPref.PATIANT_ID, -1);
        strPMH = past_medical_history.getText().toString();
        caseHistories.setPmhx(strPMH);
        strmed = medications.getText().toString();
        caseHistories.setPmhx(strmed);
        strLastDose = last_dose.getText().toString();
        if(!strLastDose.isEmpty()) {
            caseHistories.setAnticoags_last_dose(strLastDose);
        }
        strLastMeal = last_meal.getText().toString();
      //  caseHistories.setLast_meal(strLastMeal);
        strSituation = situation.getText().toString();
        caseHistories.setPmhx(strSituation);
        strWeight = weight.getText().toString();
        if(!strWeight.isEmpty()) {
            caseHistories.setWeight(Float.parseFloat(strWeight));
        }

        if(yes_anticoagulants.isChecked()){
            caseHistories.setAnticoags(Anticoags.yes.name());
        }
        if(no_anticoagulants.isChecked()){
            caseHistories.setAnticoags(Anticoags.no.name());
        }
        if(unknown_anticoagulants.isChecked()){
            caseHistories.setAnticoags(Anticoags.unknown.name());
        }

        caseHistories.setCase_id(id);
        signoff_first_name = SharedPref.read(SharedPref.SIGNOFF_FIRST_NAME, null);
        signoff_last_name = SharedPref.read(SharedPref.SIGNOFF_LAST_NAME, null);
        signoff_role = SharedPref.read(SharedPref.SIGNOFF_ROLE, null);
        caseHistories.setSignoff_first_name(signoff_first_name);
        caseHistories.setSignoff_last_name(signoff_last_name);
        caseHistories.setSignoff_role(signoff_role);


        Call<CaseHistories> response = updateCaseHistoriesApi.updateCase(id, caseHistories);
        response.enqueue(new Callback<CaseHistories>() {
            @Override
            public void onResponse(Call<CaseHistories> call, Response<CaseHistories> response) {
                CaseHistories resp = response.body();
                if(resp != null){
                    Toast.makeText(getActivity(), "Clinical History updated!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CaseHistories> call, Throwable t) {
                if(getActivity() != null) {
                    Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
