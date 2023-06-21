package com.google.mlkit.samples.nl.translate.java.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.adapter.LanguageAdapter;
import com.google.mlkit.samples.nl.translate.java.listener.LanguageClickListener;
import com.google.mlkit.samples.nl.translate.java.model.Language;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TranslateActivity extends RootActivity implements LanguageClickListener {
    TextView txt_language_name;
    ImageView img_flag;
    ImageView img_selected;
    ImageView img_back;
    boolean isSource = true;
    boolean isPhrases = true;
    Language selectedLanguage;
    LanguageAdapter adapter;
    RecyclerView sourceLangSelector;
    CircularProgressIndicator loading;
    MutableLiveData<List<String>> availableModels = new MutableLiveData<>();
    private RemoteModelManager modelManager;
    List<Language> languageList = GeneralPreference.getAvailableLanguages();
    String strData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        World.init(this);
        modelManager = RemoteModelManager.getInstance();
        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // to make status bar icon dark
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        sourceLangSelector = findViewById(R.id.sourceLangSelector);
        txt_language_name = findViewById(R.id.txt_language_name);
        img_flag = findViewById(R.id.img_flag);
        img_back = findViewById(R.id.img_back);
        loading = findViewById(R.id.loading);
        img_selected = findViewById(R.id.img_selected);

        fetchDownloadedModels(this);
        strData = getIntent().getExtras().getString("UniqueId");


        if (strData.equals("From_Main_Activity")) {
            isSource = getIntent().getBooleanExtra("isSource", true);
            if (isSource) {
                selectedLanguage = preference.getObject("sourceLanguage", Language.class, GeneralPreference.getDefaultSource());
            } else {
                selectedLanguage = preference.getObject("targetLanguage", Language.class, GeneralPreference.getDefaultTarget());
            }
        }
        if (strData.equals("From_Phrases_Activity")) {
            isPhrases = getIntent().getBooleanExtra("isPhrases", true);
            if (isPhrases) {
                selectedLanguage = preference.getObject("sourceLanguagePhrase", Language.class, GeneralPreference.getDefaultSource());
            } else {
                selectedLanguage = preference.getObject("targetLanguagePhrase", Language.class, GeneralPreference.getDefaultTarget());
            }
        }

        img_back.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        sourceLangSelector.setLayoutManager(new LinearLayoutManager(this));
        setData();

//        checkDownloadedLanguages();

    }

    private void checkDownloadedLanguages() {
        for (int i = 0; i < languageList.size(); i++) {
            for (int j = 0; j < availableModels.getValue().size(); j++) {
                if (languageList.get(i).getCode().equalsIgnoreCase(availableModels.getValue().get(j))) {
                    languageList.get(i).setDownloaded(true);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setData() {
        if (strData.equals("From_Main_Activity")) {
            if (isSource) {
                selectedLanguage = preference.getObject("sourceLanguage", Language.class, GeneralPreference.getDefaultSource());
            } else {
                selectedLanguage = preference.getObject("targetLanguage", Language.class, GeneralPreference.getDefaultTarget());
            }
        }
        if (strData.equals("From_Phrases_Activity")) {
            if (isPhrases) {
                selectedLanguage = preference.getObject("sourceLanguagePhrase", Language.class, GeneralPreference.getDefaultSource());
            } else {
                selectedLanguage = preference.getObject("targetLanguagePhrase", Language.class, GeneralPreference.getDefaultTarget());
            }
        }
        txt_language_name.setText(selectedLanguage.getDisplayName());
        int flag = World.getFlagOf(selectedLanguage.getCode());
        int icon = getIcon(selectedLanguage.getCode());
        if (icon == -1) {
            img_flag.setImageResource(flag);
        } else {
            img_flag.setImageResource(icon);
        }
    }

    @Override
    public void onClick(Language language, int position) {
        if (strData.equals("From_Main_Activity")) {
            Language prevSource = preference.getObject("sourceLanguage", Language.class, GeneralPreference.getDefaultSource());
            Language prevTarget = preference.getObject("targetLanguage", Language.class, GeneralPreference.getDefaultTarget());
            if (isSource) {
                if (language.getCode().equalsIgnoreCase(prevTarget.getCode())) {
                    preference.putObject("targetLanguage", prevSource);
                }
                preference.putObject("sourceLanguage", language);
            } else {
                if (language.getCode().equalsIgnoreCase(prevSource.getCode())) {
                    preference.putObject("sourceLanguage", prevTarget);
                }
                preference.putObject("targetLanguage", language);
            }
        }

        if (strData.equals("From_Phrases_Activity")) {
            Language prevSourcePhrase = preference.getObject("sourceLanguagePhrase", Language.class, GeneralPreference.getDefaultSource());
            Language prevTargetPhrase = preference.getObject("targetLanguagePhrase", Language.class, GeneralPreference.getDefaultTarget());

            if (isPhrases) {
                if (language.getCode().equalsIgnoreCase(prevTargetPhrase.getCode())) {
                    preference.putObject("targetLanguagePhrase", prevSourcePhrase);
                }
                preference.putObject("sourceLanguagePhrase", language);
            } else {
                if (language.getCode().equalsIgnoreCase(prevSourcePhrase.getCode())) {
                    preference.putObject("sourceLanguagePhrase", prevTargetPhrase);
                }
                preference.putObject("targetLanguagePhrase", language);
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    public void fetchDownloadedModels(LanguageClickListener listener) {
        loading.setVisibility(View.VISIBLE);
        modelManager.getDownloadedModels(TranslateRemoteModel.class).addOnSuccessListener(remoteModels -> {
            List<String> modelCodes = new ArrayList<>(remoteModels.size());
            for (TranslateRemoteModel model : remoteModels) {
                modelCodes.add(model.getLanguage());
            }
            Collections.sort(modelCodes);
            availableModels.setValue(modelCodes);
            loading.setVisibility(View.GONE);

            adapter = new LanguageAdapter(languageList, TranslateActivity.this, listener, selectedLanguage);
            sourceLangSelector.setAdapter(adapter);
            checkDownloadedLanguages();
        });

    }


    public static int getIcon(String id) {
        String[] codes = {
                "sq", "zh", "cs", "da", "en", "eo",
                "ka", "el", "he", "hi", "ja", "ko",
                "fa", "sw", "ta", "te", "uk", "ur",
                "gu","mr"
        };

        int[] resourceIds = {
                R.drawable.flag_albania, // sq -> Albanian
                R.drawable.flag_china, // zh -> Chinese
                R.drawable.flag_czech_republic, // cs -> czech
                R.drawable.flag_denmark, // da -> danish (Denmark)
                R.drawable.flag_united_kingdom, // en -> UnitedState
                R.drawable.flag_united_kingdom, // eo -> UnitedState
                R.drawable.flag_georgia, // ka -> Georgia
                R.drawable.flag_greece, // el -> Greek
                R.drawable.flag_israel, // he -> Hebrew (Israel)
                R.drawable.flag_india, // hi -> hindi (India)
                R.drawable.flag_japan, // ja -> Japan
                R.drawable.flag_north_korea, // ko -> South Korea
                R.drawable.flag_iran, // fa -> Farsi (Iran)
                R.drawable.flag_tanzania, // se -> Swahili
                R.drawable.flag_india, // ta -> Tamil (India)
                R.drawable.flag_india, //  te -> Telugu (India)
                R.drawable.flag_ukraine, // uk -> Ukraine
                R.drawable.flag_india, // ur -> Urdu
                R.drawable.flag_india, // gu -> Gujarati
                R.drawable.flag_india, // mr -> Marathi
        };

        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equalsIgnoreCase(id)) {
                return resourceIds[i];
            }
        }
        return -1;
    }

}