package com.google.mlkit.samples.nl.translate.java.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBAppLoadAds;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBEasyAES;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBModelAd;
import com.google.mlkit.samples.nl.translate.java.MyApplication;
import com.google.mlkit.samples.nl.translate.java.retrofitClient.ADCLBRetrofitClient;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;
import com.preference.PowerPreference;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends RootActivity {

    private RemoteModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GeneralPreference.showStatusBar(this, R.color.white);

        modelManager = RemoteModelManager.getInstance();
        downloadLanguage();

        PowerPreference.getDefaultFile().putBoolean("isFirstTime", true);
        PowerPreference.getDefaultFile().putBoolean("isBackFirstTime", true);

//        new Thread() {
//            public void run() {
//                try {
//                    sleep(3000);
//                    Intent main = new Intent(SplashActivity.this, CountryActivity.class);
//                    startActivity(main);
//                    finish();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    finish();
//                }
//            }
//        }.start();

        checkUpdate();

    }

    public void downloadLanguage() {
        String hindiCode = "hi";
        TranslateRemoteModel model = new TranslateRemoteModel.Builder(hindiCode).build();
        modelManager.download(model, new DownloadConditions.Builder().build())
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Log.d("==============>", "Hindi Language Download");
                            }
                        });

    }

    public void checkUpdate() {
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        JsonObject object = new JsonObject();
        try {
            object.addProperty("AndroidId", android_id);
            object.addProperty("VersionCode", 1);
            object.addProperty("PkgName", "com.data.sdk.adcode");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ADCLBRetrofitClient.getInstance().getMyApi().data(ADCLBEasyAES.encryptString(object.toString()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            MyApplication.adModel = null;
                            JSONObject jsonObject = new JSONObject(ADCLBEasyAES.decryptString(response.body().string()));
                            ADCLBModelAd model = new Gson().fromJson(jsonObject.toString(), ADCLBModelAd.class);
                            model.setAdsBanner("Google");
                            model.setAdsInterstitial("Google");
                            model.setAdsInterstitialBack("Google");
                            model.setAdsNative("Google");
                            model.setAdsAppOpen("Google");
                            PowerPreference.getDefaultFile().putObject(MyApplication.TAG, model);
                            PowerPreference.getDefaultFile().putInt(MyApplication.ADS_COUNT_SHOW, 0);
                            PowerPreference.getDefaultFile().putInt(MyApplication.ADS_COUNT_BACK_SHOW, 0);
                            if (MyApplication.getAdModel().getAdsAppUpDate().equalsIgnoreCase("Yes")) {
                                showUpdateDialog();
                            } else {
                                gotoSkip();
                            }
                        } catch (Exception e) {
                            Log.e("=============> ERR", e.toString());
                            e.printStackTrace();
                            gotoSkip();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("========> FAILED ", t.getMessage());
                        gotoSkip();
                    }
                });

    }

    public void gotoSkip() {

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", MyApplication.getAdModel().getAdsAppId());
        } catch (Exception e) {
            Log.e("Failed to load", e.getMessage());
        }

        MobileAds.initialize(getApplicationContext(), initializationStatus -> {

        });

        if (MyApplication.getAdModel().getAdsNativePreload().equalsIgnoreCase("Yes") && !MyApplication.getAdModel().getAdsNativeId().equalsIgnoreCase("None")) {
            ADCLBAppLoadAds.getInstance().loadGoogleAdNativePerLoadOne(SplashActivity.this);
        }

        ADCLBAppLoadAds.getInstance().loadInterstitialAds(SplashActivity.this);

        if (MyApplication.getAdModel().getAdsSplash().equalsIgnoreCase("AppOpen")) {
            MyApplication.getInstance().showAdIfAvailable(SplashActivity.this, () -> {
                MyApplication.booleanSplashAds = false;
                goNext();
            });
        } else {
            MyApplication.booleanSplashAds = false;
            goNext();
        }
    }

    public void showUpdateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.findViewById(R.id.btnUpdate)
                .setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.getAdModel().getAdsAppUpDateLink())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void goNext() {
        startActivity(new Intent(SplashActivity.this, CountryActivity.class));
//        finish();
    }
}