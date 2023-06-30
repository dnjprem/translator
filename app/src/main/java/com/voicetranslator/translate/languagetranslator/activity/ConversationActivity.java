package com.voicetranslator.translate.languagetranslator.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;
import com.voicetranslator.translate.languagetranslator.adapter.ConversationAdapter;
import com.voicetranslator.translate.languagetranslator.database.DataBaseHelper;
import com.voicetranslator.translate.languagetranslator.listener.TranslateListener;
import com.voicetranslator.translate.languagetranslator.model.Conversation;
import com.voicetranslator.translate.languagetranslator.model.Language;
import com.voicetranslator.translate.languagetranslator.utils.DataTranslator;
import com.voicetranslator.translate.languagetranslator.utils.GeneralPreference;

import java.util.ArrayList;
import java.util.Date;

public class ConversationActivity extends RootActivity {

    ImageView img_back;
    LinearLayout layout_background;
    RecyclerView conversation_rcv;
    ImageView img_mic_source;
    ImageView img_mic_target;
    TextView sourceLangSelector;
    TextView targetLangSelector;
    RelativeLayout layout_source_selector, layout_target_selector;
    Language sourceLanguage;
    Language targetLanguage;
    ArrayList<String> outputResult = null;
    DataBaseHelper helper;
    ArrayList<Conversation> conversationList = new ArrayList<>();
    ConversationAdapter adapter;
    int isSource;
    String sourceCode;
    String targetCode;
    boolean isDataChanged = false;


    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        GeneralPreference.showStatusBar(this, R.color.colorPrimaryLight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // to make status bar icon dark
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        helper = new DataBaseHelper(this);
        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));

        img_back = findViewById(R.id.img_back);
        layout_background = findViewById(R.id.layout_background);
        conversation_rcv = findViewById(R.id.conversation_rcv);
        img_mic_source = findViewById(R.id.img_mic_source);
        img_mic_target = findViewById(R.id.img_mic_target);
        sourceLangSelector = findViewById(R.id.sourceLangSelector);
        targetLangSelector = findViewById(R.id.targetLangSelector);
        layout_source_selector = findViewById(R.id.layout_source_selector);
        layout_target_selector = findViewById(R.id.layout_target_selector);
        final ImageView switchButton = findViewById(R.id.buttonSwitchLang);

        conversationList = helper.getAllConversation();

        if (conversationList.size() > 0) {
            layout_background.setVisibility(View.GONE);
            conversation_rcv.scrollToPosition(conversationList.size() - 1);
        }

        adapter = new ConversationAdapter(conversationList);
        conversation_rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        conversation_rcv.setAdapter(adapter);


        img_back.setOnClickListener(view -> {
            ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, () -> {
                if (isDataChanged) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });
        });

        setUpdatedLanguage();
        layout_source_selector.setOnClickListener(myView -> {
            Intent intent = new Intent(this, TranslateActivity.class);
            intent.putExtra("isSource", true);
            intent.putExtra("UniqueId", "From_Main_Activity");
            TranslateActivityResultLauncher.launch(intent);
        });
        layout_target_selector.setOnClickListener(myView -> {
            Intent intent = new Intent(this, TranslateActivity.class);
            intent.putExtra("isSource", false);
            intent.putExtra("UniqueId", "From_Main_Activity");
            TranslateActivityResultLauncher.launch(intent);
        });

        switchButton.setOnClickListener(v -> {
            Language temp = targetLanguage;
            targetLanguage = sourceLanguage;
            sourceLanguage = temp;
            preference.putObject("sourceLanguage", sourceLanguage);
            preference.putObject("targetLanguage", targetLanguage);
            sourceLangSelector.setText(sourceLanguage.getDisplayName());
            targetLangSelector.setText(targetLanguage.getDisplayName());
            isDataChanged = true;
        });

        // SourceText Mic, Get source text
        img_mic_source.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.text_say_something), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, sourceLanguage.getCode()); // Source Language code
            isSource = 0;
            if (intent.resolveActivity(getPackageManager()) != null) {
                setResult(RESULT_OK);
                MicActivityResultLauncher.launch(intent);
            } else {
                setResult(RESULT_CANCELED);
                Toast.makeText(this, getString(R.string.text_device_not_support), Toast.LENGTH_SHORT).show();
            }
        });

        // TargetText Mic, Get target text
        img_mic_target.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.text_say_something), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, targetLanguage.getCode()); // Target Language code
            isSource = 1;
            if (intent.resolveActivity(getPackageManager()) != null) {
                setResult(RESULT_OK);
                MicActivityResultLauncher.launch(intent);
            } else {
                setResult(RESULT_CANCELED);
                Toast.makeText(this, getString(R.string.text_device_not_support), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setUpdatedLanguage() {
        sourceLanguage = preference.getObject("sourceLanguage", Language.class, GeneralPreference.getDefaultSource());
        targetLanguage = preference.getObject("targetLanguage", Language.class, GeneralPreference.getDefaultTarget());
        sourceLangSelector.setText(sourceLanguage.getDisplayName());
        targetLangSelector.setText(targetLanguage.getDisplayName());
    }

    ActivityResultLauncher<Intent> TranslateActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.d("==================> ", "In ----> SomeActivityResultLauncher ");
        if (result.getResultCode() == Activity.RESULT_OK) {
            isDataChanged = true;
            setUpdatedLanguage();
        }
    });


    ActivityResultLauncher<Intent> MicActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.d("==================> ", "In ----> MicActivityResultLauncher ");
        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
            outputResult = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("=======> Speech Text ", outputResult.get(0));

            sourceCode = sourceLanguage.getCode();
            targetCode = targetLanguage.getCode();

            if (isSource == 1) {
                String temp = sourceCode;
                sourceCode = targetCode;
                targetCode = temp;
            }

            DataTranslator.translate(sourceCode, targetCode, outputResult.get(0), new TranslateListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFailed(String message) {

                }

                @Override
                public void onResult(String result) {
                    Date date = new Date();
                    Conversation conversation;
                    conversation = new Conversation(
                            sourceCode, // en
                            outputResult.get(0), // Hello
                            targetCode, // hi
                            result, // Hi hello
                            GeneralPreference.convertDateToString(date),
                            GeneralPreference.convertDateToString(date),
                            isSource
                    );
                    helper.addConversationData(conversation);
                    adapter.addRecord(conversation);
                    layout_background.setVisibility(View.GONE);
                    conversation_rcv.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            });
        }

    });

    @Override
    public void onBackPressed() {
        ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(this, () -> {
            if (isDataChanged) {
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        super.onBackPressed();
    }
}