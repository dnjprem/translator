package com.google.mlkit.samples.nl.translate.java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;
import com.preference.PowerPreference;
import com.preference.Preference;

import java.util.Locale;

public class SplashActivity extends RootActivity {

    private RemoteModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GeneralPreference.showStatusBar(this, R.color.white);

        modelManager = RemoteModelManager.getInstance();
        downloadLanguage();

        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent main = new Intent(SplashActivity.this, CountryActivity.class);
                    startActivity(main);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        }.start();

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
}