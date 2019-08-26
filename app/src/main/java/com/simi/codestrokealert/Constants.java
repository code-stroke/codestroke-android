package com.simi.codestrokealert;


import android.util.Base64;
import com.simi.codestrokealert.Entry;

public class Constants {

//   public static final String BASE_URL = "http://codestroke.pythonanywhere.com/";
//   public static final String BASE_URL = "https://codefactor.pythonanywhere.com/";
   public static final String BASE_URL = "https://codestroke.austin.org.au/";
   public static String shared_secret = "";
   public static String username = "";
   public static String password = "";
   public static String otp = "";
   public static boolean check_back = false;
   public static boolean check_back_full = false;
  // public static final String BASE_URL = "http://10.0.2.2:5000/";
//   public Entry e = new Entry(Entry.OTPType.TOTP, Constants.shared_secret, 300, 6, "TOTP", TokenCalculator.HashAlgorithm.SHA1);
//   //public static Entry e = new Entry(Entry.OTPType.TOTP, Constants.shared_secret, 300, 6, "TOTP", TokenCalculator.HashAlgorithm.SHA1);
//   e.updateOTP();
//   e.setLastUsed(System.currentTimeMillis());
   static String base =  SharedPref.read("USERNAME","")+":"+SharedPref.read("PASSWORD","");
   public static String AUTH_HEADER = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//   public static String AUTH_HEADER = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
   public static final String HOSPITAL_LIST_URL = "https://codestroke-hospitals-codestroke-hospitals.193b.starter-ca-central-1.openshiftapps.com/";


}
