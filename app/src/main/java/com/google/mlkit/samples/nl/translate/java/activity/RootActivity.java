package com.google.mlkit.samples.nl.translate.java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;
import com.preference.PowerPreference;
import com.preference.Preference;

import java.util.Locale;

public class RootActivity extends AppCompatActivity {

    public Preference preference = PowerPreference.getFileByName("preferenceName");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralPreference.setLanguageForApp(this, preference.getString("defaultLanguage", Locale.getDefault().getLanguage()));
    }
}