package com.simi.codestrokealert.notification;

import android.app.Application;
import android.content.Context;

//import com.buglife.sdk.Buglife;
//import com.buglife.sdk.InvocationMethod;
import com.crashlytics.android.Crashlytics;
import com.onesignal.OneSignal;
import com.simi.codestrokealert.SharedPref;
import io.fabric.sdk.android.Fabric;


public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
        SharedPref.init(getApplicationContext());
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .init();
 //       Buglife.initWithApiKey(this, "");
 //       Buglife.setInvocationMethod(InvocationMethod.SHAKE);
 //       String email = "test@test.com";
 //       Buglife.setUserEmail(email);
    }
}
