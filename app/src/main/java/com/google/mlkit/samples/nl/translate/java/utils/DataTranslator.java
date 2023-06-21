package com.google.mlkit.samples.nl.translate.java.utils;

import android.util.Log;
import android.util.LruCache;

import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.listener.TranslateListener;

public class DataTranslator {
    private static final int NUM_TRANSLATORS = 3;

    private static LruCache<TranslatorOptions, Translator> translators =
            new LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
                @Override
                public Translator create(TranslatorOptions options) {
                    return Translation.getClient(options);
                }

                @Override
                public void entryRemoved(
                        boolean evicted, TranslatorOptions key, Translator oldValue, Translator newValue) {
                    oldValue.close();
                }
            };


    public static void translate(String sourceCode, String targetCode, String text, TranslateListener listener) {
        Log.d("================>", "Source =====>  " + text);
        if (sourceCode == null || targetCode == null || text == null || text.isEmpty()) {
            listener.onFailed("Failed");
            return;
        }
        listener.onStart();
        TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(sourceCode).setTargetLanguage(targetCode).build();
        translators.get(options).downloadModelIfNeeded().continueWithTask(task -> {
            if (task.isSuccessful()) {
                return translators.get(options).translate(text);
            } else {
                Exception e = task.getException();
                if (e == null) {
                    e = new Exception(String.valueOf(R.string.unknown_error));
                }
                return Tasks.forException(e);
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("================>", "Result =====>  " + task.getResult());
                listener.onResult(task.getResult());
            } else {
                listener.onFailed("Failed");
            }
        });
    }
}
