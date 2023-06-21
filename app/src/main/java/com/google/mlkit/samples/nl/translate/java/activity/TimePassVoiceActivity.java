package com.google.mlkit.samples.nl.translate.java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.samples.nl.translate.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class TimePassVoiceActivity extends AppCompatActivity {


    TextView txtView;
    ArrayList<String> result = null;
    ImageView mic, wp, twtr, more;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_pass_voice);

        txtView = (TextView) findViewById(R.id.textView);
        mic = (ImageView) findViewById(R.id.mic);
        wp = (ImageView) findViewById(R.id.whatsapp);
        twtr = (ImageView) findViewById(R.id.twitter);
        more = (ImageView) findViewById(R.id.more);


        mic.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.text_say_something), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi"); // Source Language code

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 5);
            } else {
                Toast.makeText(this, getString(R.string.text_device_not_support), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.v("UTF ENCODING EXCEPTION", "UTF-8 should always be supported", e);
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            if (resultCode == RESULT_OK && data != null) {
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtView.setText(result.get(0));
            }
        }
    }
}