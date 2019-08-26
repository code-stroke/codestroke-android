package com.simi.codestrokealert.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simi.codestrokealert.ConnectionDetector;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.Entry;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.TokenCalculator;
import com.simi.codestrokealert.UserFunctions;
import com.simi.codestrokealert.activity.clinicians.HomeScreenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Sandip on 12/2/2018.
 */

public class PasswordActivity extends AppCompatActivity {

    private EditText et_password, et_conf_password;
    private Button btn_continue;
    ConnectionDetector cd;
    private String TAG = "QR Code";
    UserFunctions UF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.password_layout);

        cd = new ConnectionDetector(PasswordActivity.this);
        UF = new UserFunctions(PasswordActivity.this);

        et_password = (EditText)findViewById(R.id.et_password);
        et_conf_password = (EditText)findViewById(R.id.et_conf_password);
        btn_continue = (Button)findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_password.getText().toString().equals(et_conf_password.getText().toString())){
                    try {
                        JSONObject prms = new JSONObject();
                        prms.put("new_password", et_password.getText().toString());
                        prm = prms.toString();
                        e = new Entry(Entry.OTPType.TOTP, Constants.shared_secret, 300, 6, "TOTP", TokenCalculator.HashAlgorithm.SHA1);
                        e.updateOTP();
                        e.setLastUsed(System.currentTimeMillis());
                        Constants.otp = e.getCurrentOTP();
                        if (cd.isConnectingToInternet()) {
                            new OrderListService().execute();
                        } else {
                            Toast.makeText(getApplicationContext(),"Internet Connection not available.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Password not match!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    String prm = "";
    Entry e;
    //get driver order list service
    private class OrderListService extends AsyncTask<Void, Void, String> {
        String json = "";
        @Override
        protected String doInBackground(Void... params) {

            json = UF.PasswordUser(Constants.BASE_URL+"clinicians/set_password/",prm);
            Log.e("Response ---- ", json);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(json);
                boolean success = obj.getBoolean("success");
                if(success){

//                    SharedPref.write(SharedPref.SIGNOFF_ROLE, "");
//                    SharedPref.write(SharedPref.USERNAME, Constants.username);
//                    SharedPref.write(SharedPref.PASSWORD, Constants.password);
//                    SharedPref.write(SharedPref.PASS, et_password.getText().toString());
//                    SharedPref.write(SharedPref.SHARED_SECRET, Constants.shared_secret);
//                    SharedPref.write(SharedPref.IS_SIGNED_IN, true);
                    e = new Entry(Entry.OTPType.TOTP, Constants.shared_secret, 300, 6, "TOTP", TokenCalculator.HashAlgorithm.SHA1);
                    e.updateOTP();
                    e.setLastUsed(System.currentTimeMillis());
                    Constants.otp = e.getCurrentOTP();
                    Constants.password = et_password.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        new LoginService().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),"Internet Connection not available.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String message = obj.getString("debugmsg");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException je){
                je.printStackTrace();
            }

        }
    }

    private class LoginService extends AsyncTask<Void, Void, String> {
        String json = "";
        String prm1 = "";
        @Override
        protected String doInBackground(Void... params) {

            json = UF.ProfileUser(Constants.BASE_URL+"clinicians/profile/",prm1);
            Log.e("Response ---- ", json);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(json);
                boolean success = obj.getBoolean("success");
                if(success){

                    SharedPref.write(SharedPref.SIGNOFF_ROLE, "");
                    SharedPref.write(SharedPref.USERNAME, Constants.username);
                    SharedPref.write(SharedPref.PASSWORD, Constants.password);
                    SharedPref.write(SharedPref.PASS, et_password.getText().toString());
                    SharedPref.write(SharedPref.SHARED_SECRET, Constants.shared_secret);
                    SharedPref.write(SharedPref.IS_SIGNED_IN, true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                    builder.setMessage("Login successfull")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    startActivity(new Intent(getBaseContext(), HomeScreenActivity.class));
                                    finish();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.setTitle("CodeStrokeAlert");
                    dialog.show();
                } else {
                    String message = obj.getString("debugmsg");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException je){
                je.printStackTrace();
            }

        }
    }
}
