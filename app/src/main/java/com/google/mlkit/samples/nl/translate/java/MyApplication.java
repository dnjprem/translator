package com.google.mlkit.samples.nl.translate.java;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBAppOpenManager;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBModelAd;
import com.preference.PowerPreference;

import java.util.Collections;
import java.util.List;

public class MyApplication extends Application {

    public static final String ADS_COUNT_SHOW = "ads_count_show";
    public static final String ADS_COUNT_BACK_SHOW = "ads_count_back_show";

    public static final boolean IS_APP_OPEN_SHOWING = true;
    public static final boolean IS_NATIVE_AD_LAST = false;

    public static boolean booleanSplashAds = false;
    public ADCLBAppOpenManager ADCLBAppOpenManager;

    public static MyApplication application;
    public static final String TAG = MyApplication.class.getSimpleName();

    public static ADCLBModelAd adModel;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MobileAds.initialize(this, initializationStatus -> {
        });
        List<String> testDeviceIds = Collections.singletonList("D06CA2FCBE5B9DDE5A705F263842E3F9");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        ADCLBAppOpenManager = new ADCLBAppOpenManager(this);
    }


    public static ADCLBModelAd getAdModel() {
        if (adModel == null) {
            adModel = PowerPreference.getDefaultFile().getObject(MyApplication.TAG, ADCLBModelAd.class, new ADCLBModelAd());
        }
        return adModel;
    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApp;
        synchronized (MyApplication.class) {
            myApp = application;
        }
        return myApp;
    }

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        ADCLBAppOpenManager.showAdIfSplashAvailable(activity, onShowAdCompleteListener);
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

}
