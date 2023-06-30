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

package com.voicetranslator.translate.languagetranslator.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.voicetranslator.translate.BuildConfig;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;
import com.voicetranslator.translate.languagetranslator.database.DataBaseHelper;
import com.voicetranslator.translate.languagetranslator.listener.TranslateListener;
import com.voicetranslator.translate.languagetranslator.model.History;
import com.voicetranslator.translate.languagetranslator.model.Language;
import com.voicetranslator.translate.languagetranslator.utils.Constant;
import com.voicetranslator.translate.languagetranslator.utils.ConvertTextToSpeech;
import com.voicetranslator.translate.languagetranslator.utils.DataTranslator;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends RootActivity {

    TextToSpeech textToSpeech;
    RelativeLayout layout_source_selector, layout_target_selector;
    TextView sourceLangSelector;
    TextView targetLangSelector;
    TextInputEditText srcTextView;
    ImageView switchButton;
    TextView txt_show_selected_language;
    TextView txt_show_target_language;
    Language sourceLanguage;
    Language targetLanguage;
    TextView targetTextView;
    ImageView img_copy_source, img_copy_target, img_mic_source, img_share;
    ImageView img_source_speak, img_target_speak;
    MaterialButton btn_translate;
    LinearLayout phrases_layout;
    LinearLayout conversation_layout;
    LinearLayout setting_layout;
    MutableLiveData<List<String>> availableModels = new MutableLiveData<>();
    ArrayList<String> outputResult = null;
    ConvertTextToSpeech convertSourceTextToSpeech;
    ConvertTextToSpeech convertTargetTextToSpeech;

    private RemoteModelManager modelManager;
    DataBaseHelper helper;

    ActivityResultLauncher<Intent> MicActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.d("==================> ", "In ----> MicActivityResultLauncher ");
        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
            outputResult = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            srcTextView.setText(outputResult.get(0));
            srcTextView.setFocusable(true);
            srcTextView.setSelection(outputResult.get(0).length());
        }
    });

    ActivityResultLauncher<Intent> TranslateActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Log.d("==================> ", "In ----> TranslateActivityResultLauncher ");
            setUpdatedLanguage();
        }
    });


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modelManager = RemoteModelManager.getInstance();
        fetchDownloadedModels();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_main);

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));

        switchButton = findViewById(R.id.buttonSwitchLang);
        srcTextView = findViewById(R.id.sourceText);
        layout_source_selector = findViewById(R.id.layout_source_selector);
        layout_target_selector = findViewById(R.id.layout_target_selector);
        targetTextView = findViewById(R.id.targetText);
        sourceLangSelector = findViewById(R.id.sourceLangSelector);
        targetLangSelector = findViewById(R.id.targetLangSelector);
        txt_show_selected_language = findViewById(R.id.txt_show_selected_language);
        txt_show_target_language = findViewById(R.id.txt_show_target_language);
        btn_translate = findViewById(R.id.btn_translate);
        img_mic_source = findViewById(R.id.img_mic_source);
        img_copy_source = findViewById(R.id.img_copy_source);
        img_copy_target = findViewById(R.id.img_copy_target);
        img_source_speak = findViewById(R.id.img_source_speak);
        img_target_speak = findViewById(R.id.img_target_speak);
        img_share = findViewById(R.id.img_share);
        phrases_layout = findViewById(R.id.phrases_layout);
        conversation_layout = findViewById(R.id.conversation_layout);
        setting_layout = findViewById(R.id.setting_layout);
        helper = new DataBaseHelper(this);

        // Get Languages Code from SharedPreferences
        // And Set to the TextView.
        setUpdatedLanguage();


        // Phrases tab
        phrases_layout.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, PhrasesActivity.class);
                startActivity(intent);
            });
        });

        // Conversation tab
        conversation_layout.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, ConversationActivity.class);
                TranslateActivityResultLauncher.launch(intent);
            });
        });

        // Setting tab
        setting_layout.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            });
        });

        img_target_speak.setOnClickListener(view -> {
            if (targetTextView.getText().toString().trim().length() > 0) {
                convertTargetTextToSpeech.textToSpeak(targetTextView.getText().toString());
            }
        });

        img_source_speak.setOnClickListener(view -> {
            if (srcTextView.getText().toString().trim().length() > 0) {
                convertSourceTextToSpeech.textToSpeak(srcTextView.getText().toString());
            }
        });


        // Open Mic for get SourceText from Google Speech
        // And Send Result to ActivityResult and Set to EditText.
        img_mic_source.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.text_say_something), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, sourceLanguage.getCode()); // Source Language code

            if (intent.resolveActivity(getPackageManager()) != null) {
                setResult(RESULT_OK);
                MicActivityResultLauncher.launch(intent);
            } else {
                setResult(RESULT_CANCELED);
                Toast.makeText(this, getString(R.string.text_device_not_support), Toast.LENGTH_SHORT).show();
            }
        });

        img_share.setOnClickListener(view -> {
            if (targetTextView.getText().toString().trim().length() > 0) {
                shareTranslatedText(targetTextView.getText().toString());
            }
        });

        layout_source_selector.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {
                Intent intent = new Intent(this, TranslateActivity.class);
                intent.putExtra("isSource", true);
                intent.putExtra("UniqueId", "From_Main_Activity");
                TranslateActivityResultLauncher.launch(intent);
            });
        });
        layout_target_selector.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenAds(this, () -> {

                Intent intent = new Intent(this, TranslateActivity.class);
                intent.putExtra("isSource", false);
                intent.putExtra("UniqueId", "From_Main_Activity");
                TranslateActivityResultLauncher.launch(intent);
            });
        });

        btn_translate.setOnClickListener(view ->
                DataTranslator.translate(sourceLanguage.getCode(), targetLanguage.getCode(), String.valueOf(srcTextView.getText()),
                        new TranslateListener() {
                            @Override
                            public void onStart() {
                                setProgressText(targetTextView);
                            }

                            @Override
                            public void onFailed(String message) {
                                targetTextView.setText(message);
                            }

                            @Override
                            public void onResult(String result) {
                                Date date = new Date();
                                targetTextView.setText(result);

                                History history = new History(
                                        sourceLanguage.getCode(),
                                        String.valueOf(srcTextView.getText()),
                                        targetLanguage.getCode(),
                                        result,
                                        GeneralPreference.convertDateToString(date));
                                helper.addHistoryData(history);
                            }
                        }
                )
        );

        // Copied text from TextView
        img_copy_source.setOnClickListener(view -> copyTextToClipBoard(srcTextView));
        img_copy_target.setOnClickListener(view -> copyTextToClipBoard(targetTextView));

        // Switch the languages
        switchButton.setOnClickListener(view -> {
            String targetText = targetTextView.getText().toString();
            Language temp = targetLanguage;
            targetLanguage = sourceLanguage;
            sourceLanguage = temp;
            preference.putObject("sourceLanguage", sourceLanguage);
            preference.putObject("targetLanguage", targetLanguage);
            sourceLangSelector.setText(sourceLanguage.getDisplayName());
            targetLangSelector.setText(targetLanguage.getDisplayName());
            srcTextView.setText(targetText);
            txt_show_selected_language.setText(sourceLanguage.getDisplayName());
            txt_show_target_language.setText(targetLanguage.getDisplayName());
            // Destroy the TextToSpeech Object
            // And Re-Initialize Both Object
            convertSourceTextToSpeech.destroyCovertTextToSpeech();
            convertTargetTextToSpeech.destroyCovertTextToSpeech();
            convertSourceTextToSpeech = new ConvertTextToSpeech(this, sourceLanguage.getCode());
            convertTargetTextToSpeech = new ConvertTextToSpeech(this, targetLanguage.getCode());
        });

    }


    private void copyTextToClipBoard(TextView textView) {
        if (textView.getText().toString().trim().length() > 0) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copiedText", textView.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, getString(R.string.text_copied), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.text_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchDownloadedModels() {
        modelManager.getDownloadedModels(TranslateRemoteModel.class).addOnSuccessListener(remoteModels -> {
            List<String> modelCodes = new ArrayList<>(remoteModels.size());
            for (TranslateRemoteModel model : remoteModels) {
                modelCodes.add(model.getLanguage());
            }
            Collections.sort(modelCodes);
            availableModels.setValue(modelCodes);
        });
    }

    private void setUpdatedLanguage() {
        sourceLanguage = preference.getObject("sourceLanguage", Language.class, GeneralPreference.getDefaultSource());
        targetLanguage = preference.getObject("targetLanguage", Language.class, GeneralPreference.getDefaultTarget());
        sourceLangSelector.setText(sourceLanguage.getDisplayName());
        targetLangSelector.setText(targetLanguage.getDisplayName());
        txt_show_selected_language.setText(sourceLanguage.getDisplayName());
        txt_show_target_language.setText(targetLanguage.getDisplayName());
        convertSourceTextToSpeech = new ConvertTextToSpeech(this, sourceLanguage.getCode());
        convertTargetTextToSpeech = new ConvertTextToSpeech(this, targetLanguage.getCode());
    }


    private void setProgressText(TextView tv) {
        tv.setText(getString(R.string.text_translating));
    }


    private void shareTranslatedText(String text) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = "Translated Text is " + text + " \n\n";
            shareMessage = (shareMessage + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID).trim();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Select App"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        convertSourceTextToSpeech = new ConvertTextToSpeech(this, sourceLanguage.getCode());
        convertTargetTextToSpeech = new ConvertTextToSpeech(this, targetLanguage.getCode());
        Log.d("============> Resume ", "TextToSpeech onResume() ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        convertSourceTextToSpeech.destroyCovertTextToSpeech();
        convertTargetTextToSpeech.destroyCovertTextToSpeech();
        Log.d("============> Pause ", "TextToSpeech onPause() ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        convertSourceTextToSpeech.destroyCovertTextToSpeech();
        convertTargetTextToSpeech.destroyCovertTextToSpeech();
        Log.d("============> Stop ", "TextToSpeech onStop() ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        convertSourceTextToSpeech.destroyCovertTextToSpeech();
        convertTargetTextToSpeech.destroyCovertTextToSpeech();
        Log.d("============> Destroy ", "TextToSpeech onDestroy() ");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Constant.showExitDialog(this);
        // For Back Inter Ads
        //   ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, this::finish);
    }
}
