package com.simi.codestrokealert.activity.clinicians;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.simi.codestrokealert.BasicAuthInterceptor;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.Entry;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.TokenCalculator;
import com.simi.codestrokealert.activity.paramedics.PatientDetailsActivity;
import com.simi.codestrokealert.adapter.CasesStatusViewPagerAdapter;
import com.simi.codestrokealert.fragment.ActiveCasesFragment;
import com.simi.codestrokealert.fragment.CompletedCasesFragment;
import com.simi.codestrokealert.fragment.IncomingCasesFragment;
import com.simi.codestrokealert.model.Cases;
import com.simi.codestrokealert.model.CasesResponse;
import com.simi.codestrokealert.model.rest.RequestInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.simi.codestrokealert.Constants.AUTH_HEADER;
import static com.simi.codestrokealert.SharedPref.PASSWORD;
import static com.simi.codestrokealert.SharedPref.USERNAME;


public class HomeScreenActivity extends AppCompatActivity {

    private ImageButton search_btn;
    ImageView btn_add;
    private TabLayout cases_status_tabs;
    private ViewPager viewPager;
    private List<Cases> incomingCases, activeCases, completedCases, cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get translucent status bar and push activity layout to status bar for the gradient to work for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_clinicians_home_screen);
        initViews();
        getCasesInfo();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        search_btn.setVisibility(View.GONE);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), PatientDetailsActivity.class));
            }
        });
    }

    protected void initViews(){
        search_btn = (ImageButton)findViewById(R.id.search_bar);
        btn_add = (ImageView)findViewById(R.id.btn_add);
        cases_status_tabs  = (TabLayout)findViewById(R.id.cases_status_tabs);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        incomingCases = new ArrayList<>();
        activeCases = new ArrayList<>();
        completedCases = new ArrayList<>();
    }

    //Defines the number of tabs by setting appropriate fragment and tab name
    protected void setupViewPager(ViewPager viewPager) {
        CasesStatusViewPagerAdapter adapter = new CasesStatusViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(createIncomingCases(), "Incoming");
        adapter.addFragment(createActiveCases(), "Active");
        adapter.addFragment(createCompletedCases(), "Completed");
        viewPager.setAdapter(adapter);

    }

    protected void getCasesInfo(){

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

        RequestInterface getCasesApi = retrofit.create(RequestInterface.class);

        //AUTH_HEADER = Credentials.basic(Constants.username, Constants.password+":"+e.getCurrentOTP());
        Call<CasesResponse> response = getCasesApi.getCases();
        response.enqueue(new Callback<CasesResponse>() {
            @Override
            public void onResponse(Call<CasesResponse> call, Response<CasesResponse> response) {
                try {
                    cases = response.body().getResults();
                    if (cases != null) {
                        categoriesCases(cases);
                    }

                    setupViewPager(viewPager);
                    //Assigns the ViewPager to TabLayout
                    cases_status_tabs.setupWithViewPager(viewPager);
                } catch (Exception e){
                    e.printStackTrace();
                    setupViewPager(viewPager);
                    //Assigns the ViewPager to TabLayout
                    cases_status_tabs.setupWithViewPager(viewPager);
                }

            }

            @Override
            public void onFailure(Call<CasesResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.i("fauilure",t.getLocalizedMessage() );
            }
        });

    }

    protected void categoriesCases(List<Cases> cases){


        for (int i = 0; i < cases.size(); i++) {
            String status = cases.get(i).getStatus();
            if(status != null) {
                switch (status) {
                    case "incoming":
                        incomingCases.add(cases.get(i));
                        break;
                    case "active":
                        activeCases.add(cases.get(i));
                        break;
                    case "completed":
                        completedCases.add(cases.get(i));
                        break;
                }
            }

        }
    }

    protected Fragment createIncomingCases(){
        Bundle bundle = new Bundle();
        String jsonIncoming = new Gson().toJson(incomingCases);
        bundle.putString("incoming",jsonIncoming);
        IncomingCasesFragment incomingCasesFragment = new IncomingCasesFragment();
        incomingCasesFragment.setArguments(bundle);
        return incomingCasesFragment;
    }

    protected Fragment createActiveCases(){
        Bundle bundle = new Bundle();
        String jsonActive = new Gson().toJson(activeCases);
        bundle.putString("active",jsonActive);
        ActiveCasesFragment activeCasesFragment  = new ActiveCasesFragment();
        activeCasesFragment.setArguments(bundle);
        return activeCasesFragment;
    }

    protected Fragment createCompletedCases(){
        Bundle bundle = new Bundle();
        String jsonCompleted = new Gson().toJson(completedCases);
        bundle.putString("completed",jsonCompleted);
        CompletedCasesFragment completedCasesFragment = new CompletedCasesFragment();
        completedCasesFragment.setArguments(bundle);
        return completedCasesFragment;
    }


}
