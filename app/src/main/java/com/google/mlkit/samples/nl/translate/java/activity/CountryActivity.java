package com.google.mlkit.samples.nl.translate.java.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBAppLoadAds;
import com.google.mlkit.samples.nl.translate.java.adapter.CountryAdapter;
import com.google.mlkit.samples.nl.translate.java.listener.CountryClickListener;
import com.google.mlkit.samples.nl.translate.java.model.Country;
import com.google.mlkit.samples.nl.translate.java.utils.CountrySelector;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;

import java.util.ArrayList;

public class CountryActivity extends RootActivity implements CountryClickListener {

    CircularProgressIndicator loading;
    EditText text_country_select;
    ImageView ima_mic;
    ImageView img_country_select;
    RecyclerView country_rcv;
    ArrayList<Country> countries = new ArrayList<>();
    CountryAdapter adapter;
    boolean isCountrySelected = false;
    ArrayList<String> outputResult = null;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        // For Dynamic Ads
        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));


        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // to make status bar icon dark
        }
        if (preference.getBoolean("isCountryOpenFirstTime", false)) {
            Intent intent = new Intent(this, SelectLanguageActivity.class);
            startActivity(intent);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }

        countries = CountrySelector.getCountryData();
        for (Country item : countries) {
            item.setCountryFlag(CountrySelector.getFlagMasterResID(item));
        }

        loading = findViewById(R.id.loading);
        text_country_select = findViewById(R.id.text_country_select);
        ima_mic = findViewById(R.id.ima_mic);
        img_country_select = findViewById(R.id.img_country_select);
        country_rcv = findViewById(R.id.country_rcv);
        loading.setVisibility(View.VISIBLE);


        adapter = new CountryAdapter(countries, this);
        country_rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainHandler.postDelayed(() -> {
            country_rcv.setAdapter(adapter);
            loading.setVisibility(View.GONE);
        }, 3000);

        ima_mic.setOnClickListener(view -> {
            Toast.makeText(this, getString(R.string.text_say_something), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en"); // Target Language code

            if (intent.resolveActivity(getPackageManager()) != null) {
                setResult(RESULT_OK);
                someActivityResultLauncher.launch(intent);
            } else {
                setResult(RESULT_CANCELED);
                Toast.makeText(this, getString(R.string.text_device_not_support), Toast.LENGTH_SHORT).show();
            }
        });


        img_country_select.setOnClickListener(view -> {
            if (isCountrySelected) {
                preference.setBoolean("isCountryOpenFirstTime", true);
                Intent intent = new Intent(this, SelectLanguageActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, getString(R.string.text_select_the_country), Toast.LENGTH_SHORT).show();
            }
        });

        text_country_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filterList(editable.toString());
            }
        });

    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            outputResult = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("=======> Speech Text ", outputResult.get(0));
            text_country_select.setText(outputResult.get(0));
            text_country_select.setSelection(outputResult.get(0).length());
        }
    });


    @Override
    public void onClick(Country country, int position) {
        if (country.isSelected()) {
            isCountrySelected = true;
        }
    }
}