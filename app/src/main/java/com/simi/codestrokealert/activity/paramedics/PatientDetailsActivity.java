package com.simi.codestrokealert.activity.paramedics;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.simi.codestrokealert.BasicAuthInterceptor;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.Entry;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.TokenCalculator;
import com.simi.codestrokealert.Utility;
import com.simi.codestrokealert.model.Cases;
import com.simi.codestrokealert.model.rest.RequestInterface;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.simi.codestrokealert.Constants.AUTH_HEADER;


public class PatientDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_next, btn_unknown_first_name, btn_unknown_surname, btn_unknown_dob, btn_unknown_address, btn_unspecified, btn_unknown_last_well, btn_unknown_nok, btn_unknown_nok_telephone;
    private EditText et_first_name, et_surname, et_address, et_next_of_kin, et_nok_telephone;
    private ToggleButton toggle_btn_gender;
    private ImageButton calenderImage;
    private LinearLayout date_and_time;
    private DatePickerDialog datePickerDialog;
    private TextView txtWeekDay, txtMonth,
            txtMonthDay, txtHour, txtMinute, txtAmPm, txtDOB;
    private int mYear, mDayWeek, mMonth, mDay, mHour, mMinute;
    private String unknown = "Unknown", strGender = Gender.m.name();
    private String first_name, last_name, address, nok, nok_phone, dob, last_well, status;
    private int age;
    private SimpleDateFormat orginalFormat, targetFormat;
    private Cases cases, cases1;
    public enum Gender {m, f, u}
    public enum Status {
        INCOMING("incoming", 0),
        ACTIVE("active", 1),
        COMPLETED("completed", 2);
        private String key;
        private int value;
        private Status(String key, int value) {
            key = key;
            value = value;
        }
    }
    private String hospital_name, signoff_first_name, signoff_last_name, signoff_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get translucent status bar and push activity layout to status bar for the gradient to work for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_patient_details);
        initViews();
        fillData();
        setCurrentTimeAndDate();

        toggle_btn_gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if(!isCheck){
                    strGender = Gender.m.name();
                }
                if(isCheck){
                    strGender = Gender.f.name();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.check_back_full){
            finish();
            Constants.check_back_full = false;
        }
    }

    protected void initViews() {
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_unknown_address = (Button) findViewById(R.id.btn_unknown_address);
        btn_unknown_dob = (Button) findViewById(R.id.btn_unknown_dob);
        btn_unknown_first_name = (Button) findViewById(R.id.btn_unknown_first_name);
        btn_unknown_last_well = (Button) findViewById(R.id.btn_unknown_last_well);
        btn_unknown_surname = (Button) findViewById(R.id.btn_unknown_surname);
        btn_unknown_nok = (Button) findViewById(R.id.btn_unknown_nok);
        btn_unknown_nok_telephone = (Button) findViewById(R.id.btn_unknown_nok_telephone);
        btn_unspecified = (Button) findViewById(R.id.btn_unspecified);
        calenderImage = (ImageButton) findViewById(R.id.calenderImage);
        txtDOB = (TextView) findViewById(R.id.DOB);
        date_and_time = (LinearLayout) findViewById(R.id.date_and_time);
        txtWeekDay = (TextView) findViewById(R.id.text_week_day);
        txtMonth = (TextView) findViewById(R.id.text_view_month);
        txtMonthDay = (TextView) findViewById(R.id.text_month_day);
        txtHour = (TextView) findViewById(R.id.text_hour);
        txtMinute = (TextView) findViewById(R.id.text_minute);
        txtAmPm = (TextView) findViewById(R.id.text_pm_am);
        et_surname = (EditText) findViewById(R.id.et_surname);
        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_next_of_kin = (EditText) findViewById(R.id.et_next_of_kin);
        et_nok_telephone = (EditText) findViewById(R.id.et_nok_telephone);
        toggle_btn_gender = (ToggleButton) findViewById(R.id.toggle_btn_gender);
        orginalFormat = new SimpleDateFormat("dd  MMM  yyyy", Locale.US);
        targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        cases = new Cases();
        cases1 = new Cases();
        btn_next.setOnClickListener(this);
        btn_unknown_address.setOnClickListener(this);
        btn_unknown_dob.setOnClickListener(this);
        btn_unknown_first_name.setOnClickListener(this);
        btn_unknown_surname.setOnClickListener(this);
        btn_unknown_last_well.setOnClickListener(this);
        btn_unknown_nok.setOnClickListener(this);
        btn_unknown_nok.setOnClickListener(this);
        btn_unknown_nok_telephone.setOnClickListener(this);
        toggle_btn_gender.setOnClickListener(this);
        calenderImage.setOnClickListener(this);
        date_and_time.setOnClickListener(this);

    }

    protected void setCurrentTimeAndDate(){
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        txtWeekDay.setText(new DateFormatSymbols().getShortWeekdays()[mDayWeek]);
        txtMonth.setText(new DateFormatSymbols().getShortMonths()[mMonth]);
        txtMonthDay.setText(String.valueOf(mDay));
        if (mHour > 12) {
            txtAmPm.setText("PM");
            txtHour.setText(String.valueOf(mHour - 12));
        } else if (mHour == 12) {
            txtAmPm.setText("PM");
            txtHour.setText("12");
        } else {
            txtAmPm.setText("AM");
            txtHour.setText(String.valueOf(mHour));
        }
        txtMinute.setText(String.valueOf(mMinute));
    }

    protected void setDateOfBirth(){
        final Calendar dateSelected = Calendar.getInstance();
        final Calendar now = Calendar.getInstance();
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        new DatePickerDialog(PatientDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear , dayOfMonth);
                if(dateSelected.after(now)){
                    Toast.makeText(getBaseContext(), "Can't be born in the future", Toast.LENGTH_SHORT).show();

                }else {
                    String birthdayDate = dateFormatter.format(dateSelected.getTime());
                    txtDOB.setText(birthdayDate);
                    age = new Utility().getAge(dateSelected);

                }
            }
        }, 1980, 00, 01).show();

    }

    protected void showDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(PatientDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                mYear = year;
                mMonth = month;
                txtMonth.setText(new DateFormatSymbols().getShortMonths()[month]);
                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, month, day - 1);
                txtWeekDay.setText(simpledateformat.format(date));
                txtMonthDay.setText(String.valueOf(day));

                timePicker();

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void timePicker() {

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        if (hourOfDay > 12) {
                            txtAmPm.setText("PM");
                            txtHour.setText(String.valueOf(hourOfDay - 12));
                        } else if (hourOfDay == 12) {
                            txtAmPm.setText("PM");
                            txtHour.setText("12");
                        } else {
                            txtAmPm.setText("AM");
                            txtHour.setText(String.valueOf(hourOfDay));
                        }
                        txtMinute.setText(String.valueOf(minute));

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next:
                goToNextActivity();
                break;

            case R.id.btn_unknown_address:
                et_address.setText(unknown);
                cases.setAddress(unknown);
                break;


            case R.id.btn_unknown_dob:
                txtDOB.setText("1901-01-01");
                cases.setDob("1901-01-01");
                Calendar calendar = Calendar.getInstance();
                calendar.set(1901,01,01);
                age = new Utility().getAge(calendar);
                break;

            case R.id.btn_unknown_first_name:
                et_first_name.setText(unknown);
                cases.setFirst_name(unknown);
                break;

            case R.id.btn_unknown_last_well:
                break;

            case R.id.btn_unknown_surname:
                et_surname.setText(unknown);
                cases.setLast_name(unknown);
                break;

            case R.id.btn_unknown_nok:
                et_next_of_kin.setText(unknown);
                cases.setNok(unknown);
                break;

            case R.id.btn_unknown_nok_telephone:
                et_nok_telephone.setText(unknown);
                cases.setNok_phone(unknown);
                break;

            case R.id.btn_unspecified:
                strGender = Gender.u.name();
                break;

            case R.id.calenderImage:
                //Show popup datePicker when click on calenderImage and store value in DOB
                setDateOfBirth();
                break;

            case R.id.date_and_time:
                //Set date and time picker
                showDatePicker();
                break;
        }

    }

    protected void goToNextActivity(){

        address = et_address.getText().toString();
        first_name = et_first_name.getText().toString();
        last_name = et_surname.getText().toString();
        nok = et_next_of_kin.getText().toString();
        nok_phone = et_nok_telephone.getText().toString();
        dob = txtDOB.getText().toString();
        last_well = String.valueOf(mYear) + "-" + String.valueOf(mMonth) + "-" +
                txtMonthDay.getText().toString() + " " + String.valueOf(mHour) + ":" +
                txtMinute.getText().toString();
        status = Status.INCOMING.name();

            if (!first_name.isEmpty() && !last_name.isEmpty() && !dob.isEmpty()
                    && !address.isEmpty() && !last_well.isEmpty() && !nok.isEmpty()
                    && !nok_phone.isEmpty() && !status.isEmpty() && !strGender.isEmpty()){

            saveCase(first_name, last_name, strGender, address,last_well, nok, nok_phone, dob, age);
//            Intent intent = new Intent(getBaseContext(), DestinationActivity.class);
//            startActivity(intent);

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

                RequestInterface casesApi = retrofit.create(RequestInterface.class);
                signoff_first_name = SharedPref.read(SharedPref.FIRST_NAME, null);
                signoff_last_name = SharedPref.read(SharedPref.LAST_NAME, null);
                signoff_role = SharedPref.read(SharedPref.SIGNOFF_ROLE, null);
                cases1.setHopspital_id(1);
                cases1.setSignoff_first_name(signoff_first_name);
                cases1.setSignoff_last_name(signoff_last_name);
                cases1.setSignoff_role(signoff_role);
                cases1.setFirst_name(SharedPref.read(SharedPref.FIRST_NAME, null));
                cases1.setLast_name(SharedPref.read(SharedPref.LAST_NAME, null));
                cases1.setDob(SharedPref.read(SharedPref.DOB, null));
                cases1.setAddress(SharedPref.read(SharedPref.ADDRESS, null));
                cases1.setLast_well(SharedPref.read(SharedPref.LAST_WELL, null));
                cases1.setNok(SharedPref.read(SharedPref.NOK, null));
                cases1.setNok_phone(SharedPref.read(SharedPref.NOK_PHONE, null));
                cases1.setStatus(SharedPref.read(SharedPref.STATUS, null));
                cases1.setGender(SharedPref.read(SharedPref.GENDER, null));

                Call<Cases> response = casesApi.addCase(cases1);
                response.enqueue(new Callback<Cases>() {
                    @Override
                    public void onResponse(Call<Cases> call, Response<Cases> response) {
                        Cases casesRes = response.body();
                        if (casesRes != null) {
                            SharedPref.write(SharedPref.CASE_ID, casesRes.getCase_id());
//                            startActivity(new Intent(getBaseContext(), ClinicalHistoryActivity.class));
                                Intent intent = new Intent(getBaseContext(), ClinicalHistoryActivity.class);
                                startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Cases> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            Toast.makeText(getBaseContext(), "Fields are empty !", Toast.LENGTH_SHORT).show();
        }

    }

    protected void saveCase(String first_name, String last_name, String gender,
                            String address, String last_well, String nok, String nok_phone, String date_of_birth, int age){
        SharedPref.write(SharedPref.FIRST_NAME, first_name);
        SharedPref.write(SharedPref.LAST_NAME, last_name);
        SharedPref.write(SharedPref.GENDER, gender);
        SharedPref.write(SharedPref.ADDRESS, address);
        SharedPref.write(SharedPref.LAST_WELL, last_well);
        SharedPref.write(SharedPref.DOB, date_of_birth);
        SharedPref.write(SharedPref.NOK, nok);
        SharedPref.write(SharedPref.NOK_PHONE, nok_phone);
        SharedPref.write(SharedPref.STATUS, status);
        SharedPref.write(SharedPref.AGE, String.valueOf(age));
    }

    protected void fillData(){
        first_name = SharedPref.read(SharedPref.FIRST_NAME,null );
        last_name =  SharedPref.read(SharedPref.LAST_NAME,null );
        dob = SharedPref.read(SharedPref.DOB, null);
        address = SharedPref.read(SharedPref.ADDRESS, null);
        strGender = SharedPref.read(SharedPref.GENDER, Gender.m.name());
        nok = SharedPref.read(SharedPref.NOK, null);
        nok_phone = SharedPref.read(SharedPref.NOK_PHONE, null);

        if(first_name != null){
            et_first_name.setText(first_name);
        }
        if(last_name != null){
            et_surname.setText(last_name);
        }
        if(dob != null){
            txtDOB.setText(dob);
        }
        if(address != null){
            et_address.setText(address);
        }
        if(nok != null){
            et_next_of_kin.setText(nok);
        }
        if(nok_phone != null){
            et_nok_telephone.setText(nok_phone);
        }
    }

}
