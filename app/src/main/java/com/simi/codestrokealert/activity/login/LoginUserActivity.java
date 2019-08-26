package com.simi.codestrokealert.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

// import com.buglife.sdk.Buglife;
// import com.buglife.sdk.InvocationMethod;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.simi.codestrokealert.ConnectionDetector;
import com.simi.codestrokealert.Constants;
import com.simi.codestrokealert.Entry;
import com.simi.codestrokealert.R;
import com.simi.codestrokealert.SharedPref;
import com.simi.codestrokealert.TokenCalculator;
import com.simi.codestrokealert.UserFunctions;
import com.simi.codestrokealert.activity.clinicians.HomeScreenActivity;
import com.simi.codestrokealert.activity.paramedics.PatientDetailsActivity;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class LoginUserActivity extends Activity {

    private EditText et_first_name, et_last_name, et_password;
    private Button btn_login, btn_register;
    private String firstName,lastName, password, role, category;
    private Spinner spinner_role, spinner_category;
    ConnectionDetector cd;
    private String TAG = "QR Code";
    UserFunctions UF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get translucent status bar and push activity layout to status bar for the gradient to work for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_login);
        initViews();

        /*btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = et_first_name.getText().toString();
                lastName = et_last_name.getText().toString();
                password = et_password.getText().toString();
                role = "paramedic";
                category = "Paramedic";

                //Check whether username or password is empty or not
                if(!firstName.isEmpty() &&  !lastName.isEmpty() && !password.isEmpty() && !category.isEmpty() && !role.isEmpty() ) {
                    category = spinner_category.getSelectedItem().toString();
                    role = spinner_role.getSelectedItem().toString();
                    loginProcess(firstName, lastName, password, role, category);

                }else {
                    Toast.makeText(getBaseContext(), "Field is empty !" , Toast.LENGTH_SHORT).show();

                }
            }
        });*/

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanNow();
            }
        });

        /*try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_role);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }*/
    }

    protected void initViews(){

        cd = new ConnectionDetector(LoginUserActivity.this);
        UF = new UserFunctions(LoginUserActivity.this);

//        Buglife.initWithApiKey(this, "");
//        Buglife.setInvocationMethod(InvocationMethod.SCREENSHOT);

//        et_first_name = (EditText)findViewById(R.id.et_first_name);
//        et_last_name = (EditText)findViewById(R.id.et_last_name);
//        et_password = (EditText)findViewById(R.id.et_password);
//        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
//        spinner_role = (Spinner)findViewById(R.id.spinner_role);
//        spinner_category = (Spinner)findViewById(R.id.spinner_category);

        boolean check_sign_in = SharedPref.read(SharedPref.IS_SIGNED_IN, false);
        if(check_sign_in){
            Constants.shared_secret = SharedPref.read(SharedPref.SHARED_SECRET, "");
            Constants.username = SharedPref.read(SharedPref.USERNAME, "");
            Constants.password = SharedPref.read(SharedPref.PASS, "");
            startActivity(new Intent(getBaseContext(), HomeScreenActivity.class));
            finish();
        }
    }

    protected void loginProcess(String firstname, String lastname, String pass, String strRole, String strCategory){

        //User validation
        if(pass.equals("changethislater")){
            SharedPref.write(SharedPref.SIGNOFF_ROLE, strRole);
            if(strCategory.equals("Clinician")) {
                 startActivity(new Intent(getBaseContext(), HomeScreenActivity.class));
            }
            if(strCategory.equals("Paramedic")) {
                 startActivity(new Intent(getBaseContext(), PatientDetailsActivity.class));
            }
        } else{
            Toast.makeText(getBaseContext(), "password incorrect." , Toast.LENGTH_SHORT).show();
        }
    }

    public void scanNow(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a QR Code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);
//                integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(LoginUserActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        } else {
            ActivityCompat.requestPermissions(LoginUserActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    /**
     * function handle scan result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    String scanContent = "";
    Entry e;
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            scanContent = scanningResult.getContents();
//            String scanFormat = scanningResult.getFormatName();

            // display it on screen
//            scan_format.setText("FORMAT: " + scanFormat);
//            scan_content.setText("CONTENT: " + scanContent);
            if (scanContent == null || scanContent.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginUserActivity.this);
                builder.setMessage("What you want?")
                        .setPositiveButton("Scan QR Code", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                dialog.dismiss();
                                IntentIntegrator integrator = new IntentIntegrator(LoginUserActivity.this);
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                                integrator.setPrompt("Scan a QR Code");
                                integrator.setCameraId(0);  // Use a specific camera of the device
                                integrator.setBeepEnabled(true);
                                integrator.setOrientationLocked(false);
                                integrator.setBarcodeImageEnabled(false);
                                integrator.initiateScan();
                            }
                        })

                        .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                dialog.dismiss();
//                                finish();
                            }
                        });
                // Create the AlertDialog object and return it
//            builder.create();
                AlertDialog dialog = builder.create();
                dialog.setTitle("No scan data received!");
                dialog.show();
            } else {
                try {
                    scanContent = new JSONTokener(scanContent).nextValue().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Constants.username = "";
                Constants.password = "";
                try {
                    JSONObject obj = new JSONObject(scanContent);
                    Constants.username = obj.getString("username");
                    Constants.password = obj.getString("password");

                }catch (JSONException je){
                    je.printStackTrace();
                }

                //Toast.makeText(getApplicationContext(),""+scanContent, Toast.LENGTH_LONG).show();

                try {
                    if (cd.isConnectingToInternet()) {
                        new OrderListService().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),"Internet Connection not available.", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //get driver order list service
    private class OrderListService extends AsyncTask<Void, Void, String> {
        String json = "";
        @Override
        protected String doInBackground(Void... params) {

            json = UF.LoginUser(Constants.BASE_URL+"clinicians/pair/",scanContent);
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
                    Constants.shared_secret = obj.getString("shared_secret").toString();
//                    e = new Entry(Entry.OTPType.TOTP, Constants.shared_secret, 300, 6, "TOTP", TokenCalculator.HashAlgorithm.SHA1);
//                    e.updateOTP();
//                    e.setLastUsed(System.currentTimeMillis());
//                    String otp = e.getCurrentOTP();
//                    Toast.makeText(getApplicationContext(),otp,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), PasswordActivity.class));
                    finish();
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
