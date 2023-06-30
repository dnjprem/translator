package com.voicetranslator.translate.languagetranslator.listener;

public interface TranslateListener {
    void onStart();
    void onFailed(String message);
    void onResult( String result);
}
