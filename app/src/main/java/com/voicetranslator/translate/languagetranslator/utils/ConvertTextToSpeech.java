package com.voicetranslator.translate.languagetranslator.utils;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class ConvertTextToSpeech implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private final String languageCode;

    public ConvertTextToSpeech(Context context, String languageCode) {
        this.languageCode = languageCode;
        this.textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int i) {
        Log.d("=====================> ", " TextToSpeech SUCCESS -> Initialize Successfully ");
        try {
            if (i == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.forLanguageTag(languageCode));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("=====================> ", " TextToSpeech ERROR -> This Language is not supported");
                } else {
                    Log.d("=====================> ", " TextToSpeech SUCCESS -> This Language is supported");
                }
            } else {
                Log.e("=====================> ", " TextToSpeech FAILED -> Initialization Failed!");
            }
        } catch (Exception ex) {
            Log.e("=====================> ", "TextToSpeech ERROR -> " + ex.getMessage());
        }
    }


    public void textToSpeak(String text) {
        if ("".equals(text)) {
            text = "Please enter some text to speak.";
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Exception ex) {
            Log.e("=====================> ", "TextToSpeech ERROR -> " + ex.getMessage());
        }
    }

    public void destroyCovertTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
            Log.d("=====================> ", " TextToSpeech SUCCESS -> Destroyed Successfully ");
        }
    }
}
