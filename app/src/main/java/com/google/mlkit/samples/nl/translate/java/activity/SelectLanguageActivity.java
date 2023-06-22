package com.google.mlkit.samples.nl.translate.java.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBAppLoadAds;
import com.google.mlkit.samples.nl.translate.java.adapter.SelectLanguageAdapter;
import com.google.mlkit.samples.nl.translate.java.listener.SelectLanguageClickListener;
import com.google.mlkit.samples.nl.translate.java.model.SelectLanguage;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectLanguageActivity extends RootActivity implements SelectLanguageClickListener {

    List<SelectLanguage> languageList = new ArrayList<>();
    SelectLanguageAdapter adapter;
    RecyclerView language_rcv;
    TextView txt_language_name;
    ImageView img_flag, img_selected, language_selected;
    String defaultLanguage, selectedLanguage = null;
    ConstraintLayout layout_default_language;
    boolean isLanguageSelectedFromList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        // For WRAP_CONTENT ADS
        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));

        GeneralPreference.showStatusBar(this, R.color.white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);   // to make status bar icon dark
        }

        defaultLanguage = preference.getString("defaultLanguage", Locale.getDefault().getLanguage()); // byDefault -> en


        if (preference.getBoolean("isSelectLanguageOpenFirstTime", false)) {
            Intent intent = new Intent(this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        languageList.add(new SelectLanguage("en", "English", R.drawable.flag_united_states_of_america, false));
        languageList.add(new SelectLanguage("hi", "Hindi", R.drawable.flag_india, false));
        languageList.add(new SelectLanguage("es", "Spanish", R.drawable.flag_spain, false));
        languageList.add(new SelectLanguage("fr", "French", R.drawable.flag_france, false));
        languageList.add(new SelectLanguage("de", "German", R.drawable.flag_germany, false));
        languageList.add(new SelectLanguage("it", "Italian", R.drawable.flag_italy, false));
        languageList.add(new SelectLanguage("ja", "Japanese", R.drawable.flag_japan, false));
        languageList.add(new SelectLanguage("sv", "Swedish", R.drawable.flag_sweden, false));
        languageList.add(new SelectLanguage("da", "Danish", R.drawable.flag_denmark, false));
        languageList.add(new SelectLanguage("nl", "Dutch", R.drawable.flag_netherlands, false));
        languageList.add(new SelectLanguage("fi", "Finnish", R.drawable.flag_finland, false));
        languageList.add(new SelectLanguage("pt", "Portuguese", R.drawable.flag_portugal, false));
        languageList.add(new SelectLanguage("vi", "Vietnamese", R.drawable.flag_vietnam, false));
        languageList.add(new SelectLanguage("tr", "Turkish", R.drawable.flag_turkey, false));
        languageList.add(new SelectLanguage("th", "Thai", R.drawable.flag_thailand, false));
        languageList.add(new SelectLanguage("el", "Greek", R.drawable.flag_greece, false));
        languageList.add(new SelectLanguage("ro", "Romanian", R.drawable.flag_romania, false));
        languageList.add(new SelectLanguage("fil", "Filipino", R.drawable.flag_philippines, false));
        languageList.add(new SelectLanguage("id", "Indonesian", R.drawable.flag_indonesia, false));
        languageList.add(new SelectLanguage("ha", "Hausa", R.drawable.flag_nigeria, false)); // Doubt
        languageList.add(new SelectLanguage("bg", "Bulgarian", R.drawable.flag_bulgaria, false));
        languageList.add(new SelectLanguage("lv", "Latvian", R.drawable.flag_latvia, false));
        languageList.add(new SelectLanguage("ms", "Malay", R.drawable.flag_malaysia, false));
        languageList.add(new SelectLanguage("pl", "Polish", R.drawable.flag_poland, false));
        languageList.add(new SelectLanguage("sk", "Slovak", R.drawable.flag_slovakia, false));

        language_rcv = findViewById(R.id.language_rcv);
        txt_language_name = findViewById(R.id.txt_language_name);
        img_flag = findViewById(R.id.img_flag);
        img_selected = findViewById(R.id.img_selected);
        language_selected = findViewById(R.id.language_selected);
        layout_default_language = findViewById(R.id.layout_default_language);

        // Default Language is Selected by Default that is English [en].
        // and Set this Default Language to the SharedPreferences.
        // and Set data to the System Default layout in UI.
        for (SelectLanguage e : languageList) {
            if (e.getCode().equals(defaultLanguage)) {
                e.setSelected(true);
                txt_language_name.setText(e.getDisplayName());
                img_flag.setImageResource(e.getResource());
                preference.putString("defaultLanguage", defaultLanguage); // Set Default Language to the SharedPreferences.
            }
        }


        // Retrieve Default Language from SharedPreferences,
        // And Remove that particular Language from languageList,
        // And then send to the RecycleView Adapter.
        String getLanguage = preference.getString("defaultLanguage");
        for (int i = 0; i < languageList.size(); i++) {
            SelectLanguage e = languageList.get(i);
            if (e.getCode().equals(getLanguage)) {
                languageList.remove(e);
            }
        }
        language_rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SelectLanguageAdapter(languageList, this);
        language_rcv.setAdapter(adapter);

        language_selected.setOnClickListener(view -> {
            if (isLanguageSelectedFromList) {
                preference.putString("defaultLanguage", selectedLanguage);
                Log.d("===========> In If", "true");
                Log.d("===========> DEF LAN ", " Default Language is Selected Language ====> " + preference.getString("defaultLanguage"));

            } else {
                preference.putString("defaultLanguage", defaultLanguage);
                Log.d("===========> In Else", "false");
                Log.d("===========> DEF LAN ", " Default Language is Default Language ====> " + preference.getString("defaultLanguage"));
            }
            GeneralPreference.setLanguageForApp(this, preference.getString("defaultLanguage"));
            preference.setBoolean("isSelectLanguageOpenFirstTime", true);
            Intent intent = new Intent(this, OnBoardingActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        layout_default_language.setOnClickListener(view -> {
            if (isLanguageSelectedFromList) {
                img_selected.setImageResource(R.drawable.ic_check_fill);
                adapter.selectionClear();
                isLanguageSelectedFromList = !isLanguageSelectedFromList;
            }
        });

    }

    @Override
    public void onClick(SelectLanguage selectLanguage, int position) {
        Log.d("========> U SELECT LAN ", selectLanguage.getCode());
        if (selectLanguage.isSelected()) {
            selectedLanguage = selectLanguage.getCode();
            img_selected.setImageResource(R.drawable.ic_uncheck);
            isLanguageSelectedFromList = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String strData = getIntent().getExtras().getString("UniqueId");
        if (strData.equals("From_Setting_Activity")) {
            preference.setBoolean("isSelectLanguageOpenFirstTime", true);
            finish();
        }
    }
}

