package com.voicetranslator.translate.languagetranslator.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voicetranslator.translate.BuildConfig;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

public class SettingActivity extends RootActivity {

    LinearLayout layout_app_theme;
    LinearLayout layout_history;
    LinearLayout layout_favorite;
    LinearLayout layout_app_language;
    LinearLayout layout_share_app;
    LinearLayout layout_rate;
    LinearLayout layout_privacy;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // to make status bar icon dark
        }
        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));


        img_back = findViewById(R.id.img_back);
        layout_app_theme = findViewById(R.id.layout_app_theme);
        layout_history = findViewById(R.id.layout_history);
        layout_favorite = findViewById(R.id.layout_favorite);
        layout_app_language = findViewById(R.id.layout_app_language);
        layout_share_app = findViewById(R.id.layout_share_app);
        layout_rate = findViewById(R.id.layout_rate);
        layout_privacy = findViewById(R.id.layout_privacy);

        ImageView theme_img_setting = layout_app_theme.findViewById(R.id.img_setting);
        ImageView history_img_setting = layout_history.findViewById(R.id.img_setting);
        ImageView favorite_img_setting = layout_favorite.findViewById(R.id.img_setting);
        ImageView app_language_img_setting = layout_app_language.findViewById(R.id.img_setting);
        ImageView share_app_img_setting = layout_share_app.findViewById(R.id.img_setting);
        ImageView rate_img_setting = layout_rate.findViewById(R.id.img_setting);
        ImageView privacy_img_setting = layout_privacy.findViewById(R.id.img_setting);

        TextView theme_text_setting = layout_app_theme.findViewById(R.id.text_setting);
        TextView history_text_setting = layout_history.findViewById(R.id.text_setting);
        TextView favorite_text_setting = layout_favorite.findViewById(R.id.text_setting);
        TextView app_language_text_setting = layout_app_language.findViewById(R.id.text_setting);
        TextView share_app_text_setting = layout_share_app.findViewById(R.id.text_setting);
        TextView rate_text_setting = layout_rate.findViewById(R.id.text_setting);
        TextView privacy_text_setting = layout_privacy.findViewById(R.id.text_setting);

        setImageToImageView(theme_img_setting, R.drawable.ic_setting_app_theme);
        setTextToTextView(theme_text_setting, R.string.text_app_themes);

        setImageToImageView(history_img_setting, R.drawable.ic_setting_history);
        setTextToTextView(history_text_setting, R.string.text_history);

        setImageToImageView(favorite_img_setting, R.drawable.ic_setting_fav);
        setTextToTextView(favorite_text_setting, R.string.text_favorite);

        setImageToImageView(app_language_img_setting, R.drawable.ic_setting_language);
        setTextToTextView(app_language_text_setting, R.string.select_app_language);

        setImageToImageView(share_app_img_setting, R.drawable.ic_setting_share);
        setTextToTextView(share_app_text_setting, R.string.text_share_app);

        setImageToImageView(rate_img_setting, R.drawable.ic_setting_rate);
        setTextToTextView(rate_text_setting, R.string.text_rate);

        setImageToImageView(privacy_img_setting, R.drawable.ic_setting_lock);
        setTextToTextView(privacy_text_setting, R.string.text_privacy);

        layout_history.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("isFavoriteScreen", false);
                startActivity(intent);
            });
        });

        layout_favorite.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("isFavoriteScreen", true);
                startActivity(intent);
            });
        });

        layout_app_language.setOnClickListener(view -> {
            Intent intent = new Intent(this, SelectLanguageActivity.class);
            intent.putExtra(Intent.EXTRA_TITLE, true);
            startActivity(intent);
        });

        layout_share_app.setOnClickListener(view -> shareApp());
        layout_rate.setOnClickListener(view -> rateUs());
        layout_privacy.setOnClickListener(view -> privacyPolicy());
        img_back.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, () -> {
                setResult(RESULT_CANCELED);
                finish();
            });
        });
    }

    private void setTextToTextView(TextView textView, int text) {
        textView.setText(text);
    }

    private void setImageToImageView(ImageView img, int drawable) {
        img.setImageResource(drawable);
    }

    private void privacyPolicy() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.google.com"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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


    @Override
    public void onBackPressed() {
        ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, () -> {
            finish();
            super.onBackPressed();
        });
    }
}