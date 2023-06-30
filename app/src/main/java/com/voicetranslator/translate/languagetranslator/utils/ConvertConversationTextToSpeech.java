package com.voicetranslator.translate.languagetranslator.utils;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class ConvertConversationTextToSpeech implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private final String languageCode;
    String text;

    public ConvertConversationTextToSpeech(Context context, String languageCode, String text) {
        this.languageCode = languageCode;
        this.text = text;
        this.textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int i) {
        Log.d("=====================> ", " ConvertConversationTextToSpeech SUCCESS -> Initialize Successfully ");
        try {
            if (i == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.forLanguageTag(languageCode));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("=====================> ", " ConvertConversationTextToSpeech ERROR -> This Language is not supported");
                } else {
                    textToSpeak(text);
                    Log.d("=====================> ", " ConvertConversationTextToSpeech SUCCESS -> This Language is supported");
                }
            } else {
                Log.e("=====================> ", " ConvertConversationTextToSpeech FAILED -> Initialization Failed!");
            }
        } catch (Exception ex) {
            Log.e("=====================> ", " ConvertConversationTextToSpeech ERROR -> " + ex.getMessage());
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
            Log.e("=====================> ", " ConvertConversationTextToSpeech ERROR -> " + ex.getMessage());
        }
    }

    public void destroyCovertTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
            Log.d("=====================> ", " ConvertConversationTextToSpeech SUCCESS -> Destroyed Successfully ");
        }
    }
}
