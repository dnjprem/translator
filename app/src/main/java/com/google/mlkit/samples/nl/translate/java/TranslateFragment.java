/*
 * Copyright 2019 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.mlkit.samples.nl.translate.java;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.activity.TranslateActivity;
import com.google.mlkit.samples.nl.translate.java.model.Language;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;
import com.preference.PowerPreference;
import com.preference.Preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/***
 * Fragment view for handling translations
 */
public class TranslateFragment extends Fragment {

    Preference preference = PowerPreference.getFileByName("preferenceName");
    TextView sourceLangSelector;
    TextView targetLangSelector;
    Language sourceLanguage;
    Language targetLanguage;
    TextView targetTextView;
    MutableLiveData<List<String>> availableModels = new MutableLiveData<>();
    MutableLiveData<String> sourceText = new MutableLiveData<>();
    MutableLiveData<Language> sourceLang = new MutableLiveData<>();
    MutableLiveData<Language> targetLang = new MutableLiveData<>();
    MediatorLiveData<ResultOrError> translatedText = new MediatorLiveData<>();
    private static final int NUM_TRANSLATORS = 3;
    private RemoteModelManager modelManager;

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelManager = RemoteModelManager.getInstance();

        setHasOptionsMenu(false);
        fetchDownloadedModels();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.translate_fragment, container, false);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    updateLanguage();
                }
            });

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
        sourceLanguage = preference.getObject("sourceLanguage", Language.class, GeneralPreference.getDefaultSource());
        targetLanguage = preference.getObject("targetLanguage", Language.class, GeneralPreference.getDefaultTarget());

        sourceLangSelector.setText(sourceLanguage.getDisplayName());
        targetLangSelector.setText(targetLanguage.getDisplayName());
    }

    private final LruCache<TranslatorOptions, Translator> translators =
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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button switchButton = view.findViewById(R.id.buttonSwitchLang);
//        final ToggleButton sourceSyncButton = view.findViewById(R.id.buttonSyncSource);
//        final ToggleButton targetSyncButton = view.findViewById(R.id.buttonSyncTarget);
//        final TextView downloadedModelsTextView = view.findViewById(R.id.downloadedModels);
        final TextInputEditText srcTextView = view.findViewById(R.id.sourceText);
        targetTextView = view.findViewById(R.id.targetText);
        sourceLangSelector = view.findViewById(R.id.sourceLangSelector);
        targetLangSelector = view.findViewById(R.id.targetLangSelector);

        updateLanguage();

        sourceLangSelector.setOnClickListener(myView -> {
            Intent intent = new Intent(view.getContext(), TranslateActivity.class);
            intent.putExtra("isSource", true);
            someActivityResultLauncher.launch(intent);
        });
        targetLangSelector.setOnClickListener(myView -> {
            Intent intent = new Intent(view.getContext(), TranslateActivity.class);
            intent.putExtra("isSource", false);
            someActivityResultLauncher.launch(intent);
        });

        // Translate input text as it is typed
        srcTextView.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setProgressText(targetTextView);
                        sourceText.postValue(s.toString());
                    }
                });
        translatedText.observe(
                getViewLifecycleOwner(),
                new Observer<ResultOrError>() {
                    @Override
                    public void onChanged(ResultOrError resultOrError) {
                        if (resultOrError.error != null) {
                            srcTextView.setError(resultOrError.error.getLocalizedMessage());
                        } else {
                            targetTextView.setText(resultOrError.result);
                        }
                    }
                });


        final OnCompleteListener<String> processTranslation =
                task -> {
                    if (task.isSuccessful()) {
                        translatedText.setValue(new ResultOrError(task.getResult(), null));
//                            translatedText.setValue(this.ResultOrError(task.getResult(), null));
                    } else {
                        translatedText.setValue(new ResultOrError(null, task.getException()));
                        task.getException().printStackTrace();
                    }
                };

        // Start translation if any of the following change: input text, source lang, target lang.
        translatedText.addSource(
                sourceText,
                s -> translate().addOnCompleteListener(processTranslation));
        Observer<Language> languageObserver =
                language -> translate().addOnCompleteListener(processTranslation);
        translatedText.addSource(sourceLang, languageObserver);
        translatedText.addSource(targetLang, languageObserver);

        switchButton.setOnClickListener(
                v -> {
                    String targetText = targetTextView.getText().toString();
                    Language temp = targetLanguage;
                    targetLanguage = sourceLanguage;
                    sourceLanguage = temp;
                    preference.putObject("sourceLanguage", sourceLanguage);
                    preference.putObject("targetLanguage", targetLanguage);
                    sourceLangSelector.setText(sourceLanguage.getDisplayName());
                    targetLangSelector.setText(targetLanguage.getDisplayName());
                    srcTextView.setText(targetText);
                    sourceText.setValue(targetText);

                });


    }

    private void setProgressText(TextView tv) {
        tv.setText("Translating");
    }


    public Task<String> translate() {
        Log.d("========> sourceText", sourceText.getValue());
//        Log.d("========> sourceLang", sourceLang.getValue().toString());
//        Log.d("========> targetLang", targetLang.getValue().toString());

        final String text = sourceText.getValue();

        final Language source = sourceLanguage;
        final Language target = targetLanguage;
        if (source == null || target == null || text == null || text.isEmpty()) {
            return Tasks.forResult("");
        }
        String sourceLangCode = TranslateLanguage.fromLanguageTag(source.getCode());
        String targetLangCode = TranslateLanguage.fromLanguageTag(target.getCode());
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLangCode)
                        .setTargetLanguage(targetLangCode)
                        .build();
        return translators
                .get(options)
                .downloadModelIfNeeded()
                .continueWithTask(
                        task -> {
                            if (task.isSuccessful()) {
                                return translators.get(options).translate(text);
                            } else {
                                Exception e = task.getException();
                                if (e == null) {
                                    e = new Exception(getContext().getString(R.string.unknown_error));
                                }
                                return Tasks.forException(e);
                            }
                        });
    }

    static class ResultOrError {
        final String result;
        final Exception error;

        ResultOrError(@Nullable String result, @Nullable Exception error) {
            this.result = result;
            this.error = error;
        }
    }
}
