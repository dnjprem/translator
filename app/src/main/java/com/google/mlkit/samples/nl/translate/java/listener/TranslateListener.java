package com.google.mlkit.samples.nl.translate.java.listener;

public interface TranslateListener {
    void onStart();
    void onFailed(String message);
    void onResult( String result);
}
