package com.voicetranslator.translate.languagetranslator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.card.MaterialCardView;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;
import com.voicetranslator.translate.languagetranslator.model.Language;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhrasesActivity extends RootActivity {

    ImageView img_back;
    MaterialCardView card_essential;
    TextView sourceLangSelector;
    TextView targetLangSelector;
    TextView txt_total_phrase;
    TextView txt_sub_title;
    MutableLiveData<List<String>> availableModels = new MutableLiveData<>();

    Language sourceLanguagePhrase;
    Language targetLanguagePhrase;

    private RemoteModelManager modelManager;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    updateLanguage();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modelManager = RemoteModelManager.getInstance();
        fetchDownloadedModels();

        setContentView(R.layout.activity_phrases);
        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // to make status bar icon dark
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ADCLBAppLoadAds.getInstance().displayMatchBottomAds(this, findViewById(R.id.frameViewAdsMain));


        img_back = findViewById(R.id.img_back);
        card_essential = findViewById(R.id.card_essential);
        sourceLangSelector = findViewById(R.id.sourceLangSelector);
        targetLangSelector = findViewById(R.id.targetLangSelector);
        txt_total_phrase = findViewById(R.id.txt_total_phrase);
        txt_sub_title = findViewById(R.id.txt_sub_title);
        final ImageView switchButton = findViewById(R.id.buttonSwitchLang);
        updateLanguage();


        txt_sub_title.setText(getString(R.string.text_greetings) + ", " + getString(R.string.text_basic));
        img_back.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, () -> {
//            setResult(RESULT_CANCELED);
            finish();
            });
        });

        card_essential.setOnClickListener(view -> {
            Intent intent = new Intent(this, EssentialActivity.class);
            someActivityResultLauncher.launch(intent);
        });

        sourceLangSelector.setOnClickListener(myView -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, TranslateActivity.class);
                intent.putExtra("isPhrases", true);
                intent.putExtra("UniqueId", "From_Phrases_Activity");
                someActivityResultLauncher.launch(intent);
            });

        });
        targetLangSelector.setOnClickListener(myView -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, TranslateActivity.class);
                intent.putExtra("isPhrases", false);
                intent.putExtra("UniqueId", "From_Phrases_Activity");
                someActivityResultLauncher.launch(intent);
            });
        });

        switchButton.setOnClickListener(
                v -> {
                    Language temp = targetLanguagePhrase;
                    targetLanguagePhrase = sourceLanguagePhrase;
                    sourceLanguagePhrase = temp;
                    preference.putObject("sourceLanguagePhrase", sourceLanguagePhrase);
                    preference.putObject("targetLanguagePhrase", targetLanguagePhrase);
                    sourceLangSelector.setText(sourceLanguagePhrase.getDisplayName());
                    targetLangSelector.setText(targetLanguagePhrase.getDisplayName());
                });
    }

    public void fetchDownloadedModels() {
        modelManager
                .getDownloadedModels(TranslateRemoteModel.class)
                .addOnSuccessListener(
                        remoteModels -> {
                            List<String> modelCodes = new ArrayList<>(remoteModels.size());
                            for (TranslateRemoteModel model : remoteModels) {
                                modelCodes.add(model.getLanguage());
                            }
                            Collections.sort(modelCodes);
                            availableModels.setValue(modelCodes);
                        });
    }

    private void updateLanguage() {
        sourceLanguagePhrase = preference.getObject("sourceLanguagePhrase", Language.class, GeneralPreference.getDefaultSource());
        targetLanguagePhrase = preference.getObject("targetLanguagePhrase", Language.class, GeneralPreference.getDefaultTarget());
        sourceLangSelector.setText(sourceLanguagePhrase.getDisplayName());
        targetLangSelector.setText(targetLanguagePhrase.getDisplayName());
    }

    @Override
    public void onBackPressed() {
        ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, () -> {
            finish();
            super.onBackPressed();
        });
    }
}