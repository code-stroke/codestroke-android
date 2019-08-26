package com.simi.codestrokealert;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    public static SharedPreferences mSharedPref;
    public static final String FIRST_NAME          = "first_name";
    public static final String LAST_NAME           = "last_name";
    public static final String DOB                 = "dob";
    public static final String ADDRESS             = "address";
    public static final String GENDER              = "gender";
    public static final String LAST_WELL           = "last_well";
    public static final String NOK                 = "nok";
    public static final String NOK_PHONE           = "nok_phone";
    public static final String STATUS              = "status";
    public static final String PMHX                = "pmhx";
    public static final String PMHX_TEXT           = "ed_pmhx";
    public static final String MEDS                = "meds";
    public static final String MED_TEXT            = "ed_med";
    public static final String ANTICOAGS           = "anticoags";
    public static final String ANTICOAGS_LAST_DOSE = "anticoags_last_dose";
    public static final String HOPC                = "hopc";
    public static final String WEIGHT              = "weight";
    public static final String LAST_MEAL           = "last_meal";
    public static final String AF                  = "af";
    public static final String IHD                 = "ihd";
    public static final String DM                  = "dm";
    public static final String STROKE             = "stroke";
    public static final String EPILEPSY            = "epilepsy";
    public static final String OTHER_NEU           = "other";
    public static final String APIXABAN           = "Apixaban";
    public static final String RIVAROXABAN         = "Rivaroxaban";
    public static final String WARFARIN            = "Warfarin";
    public static final String DABIGATRAN          = "Dabigatran";
    public static final String HEPARIN             = "Heparin";
    public static final String FACIAL_DROOP        = "facial_droop";
    public static final String ARM_DRIFT           = "arm_drift";
    public static final String WEAK_GRIP           = "weak_grip";
    public static final String SPEECH_DIFFICULTY   = "speech_difficulty";
    public static final String CANNULA             = "cannula";
    public static final String FACIAL_PALSY_RACE   = "facial_palsy_race";
    public static final String ARM_MOTOR_IMPAIR    = "arm_motor_impair";
    public static final String LEG_MOTOR_IMPAIR    = "leg_motor_impair";
    public static final String HEAD_GAZE_DEVIATE   = "head_gaze_deviate";
    public static final String FACIAL_PALSY_RACE_TEXT   = "facial_palsy_race_txt";
    public static final String ARM_MOTOR_IMPAIR_TEXT    = "arm_motor_impair_txt";
    public static final String LEG_MOTOR_IMPAIR_TEXT    = "leg_motor_impair_txt";
    public static final String HEAD_GAZE_DEVIATE_TEXT   = "head_gaze_deviate_txt";
    public static final String BP_SYSTOLIC         = "bp_systolic";
    public static final String BP_DIASTOLIC        = "bp_diastolic";
    public static final String HEART_RATE          = "heart_rate";
    public static final String HEART_RHYTHM        = "heart_rhythm";
    public static final String RR                  = "rr";
    public static final String O2STAS              = "o2sats";
    public static final String TEMP                = "temp";
    public static final String EYE                 = "eye";
    public static final String VERBAL              = "verbal";
    public static final String MOTOR               = "motor";
    public static final String EYE_INDEX           = "index_eye";
    public static final String VERBAL_INDEX        = "index_verbal";
    public static final String MOTOR_INDEX         = "index_motor";
    public static final String GCS_TEXT            = "gcs_text";
    public static final String GCS                 = "gcs";
    public static final String BLOOD_GLOUCE        = "blood_glucose";
    public static final String INCOMING_CASES       = "incoming";
    public static final String PATIANT_ID           = "id";
    public static final String CASE_ID              = "case_id";
    public static final String AGE                  = "age";
    public static final String SIGNOFF_FIRST_NAME   = "first_name";
    public static final String SIGNOFF_LAST_NAME    = "last_name";
    public static final String SIGNOFF_ROLE         = "role";
    public static final String USERNAME             = "user_id";
    public static final String PASSWORD             = "password";
    public static final String PASS                 = "user_pass";
    public static final String SHARED_SECRET        = "shared_secret";
    public static final String IS_SIGNED_IN        = "is_new_signed_in";
//    public static final String IS_SIGNED_IN        = "is_signed_in";

    public static void init(Context context){
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue){
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue){
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue){
        return  mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, int value){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

}
