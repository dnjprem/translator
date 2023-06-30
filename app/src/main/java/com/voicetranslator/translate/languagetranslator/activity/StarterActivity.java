package com.voicetranslator.translate.languagetranslator.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.voicetranslator.translate.BuildConfig;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

public class StarterActivity extends AppCompatActivity {

    ImageView img_back;
    MaterialCardView card_start;
    MaterialCardView card_rate;
    MaterialCardView card_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        GeneralPreference.showStatusBar(this, R.color.white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);   // to make status bar icon dark
        }

        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));


        card_start = findViewById(R.id.card_start);
        card_rate = findViewById(R.id.card_rate);
        card_share = findViewById(R.id.card_share);

        card_start.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        card_rate.setOnClickListener(view -> rateUs());
        card_share.setOnClickListener(view -> shareApp());

    }

    private void rateUs() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + this.getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName()));
                startActivity(intent);
            } catch (Exception xe) {
                e.printStackTrace();
            }
        }
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = "Check out this Language Translator App \n\n";
            shareMessage = (shareMessage + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID).trim();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Select App"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}


