package com.google.mlkit.samples.nl.translate.java.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBAppLoadAds;
import com.google.mlkit.samples.nl.translate.java.adapter.OnBoardingAdapter;
import com.google.mlkit.samples.nl.translate.java.model.OnBoarding;
import com.google.mlkit.samples.nl.translate.java.utils.GeneralPreference;

import java.util.ArrayList;

public class OnBoardingActivity extends RootActivity {

    ViewPager view_pager;
    TabLayout tab_indicator;
    Button btn_next;
    OnBoardingAdapter adapter;
    ArrayList<OnBoarding> onBoardings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(this, findViewById(R.id.frameViewAds));


        GeneralPreference.showStatusBar(this, R.color.white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);   // to make status bar icon dark
        }
        if (preference.getBoolean("isOnBoardingFirstTime", false)) {
            Intent intent = new Intent(this, StarterActivity.class);
            startActivity(intent);
            finish();
        }

        view_pager = findViewById(R.id.view_pager);
        tab_indicator = findViewById(R.id.tab_indicator);
        btn_next = findViewById(R.id.btn_next);

        onBoardings.add(new OnBoarding(R.drawable.fg_first_on_boarding_screen, getString(R.string.text_onboard_first_title), getString(R.string.text_fir_onboard_first_sub_title), getString(R.string.text_sec_onboard_first_sub_title)));
        onBoardings.add(new OnBoarding(R.drawable.fg_second_on_boarding_screen, getString(R.string.text_onboard_second_title), getString(R.string.text_fir_onboard_sec_sub_title), getString(R.string.text_sec_onboard_sec_sub_title)));
        onBoardings.add(new OnBoarding(R.drawable.fg_third_on_boarding_screen, getString(R.string.text_onboard_third_title), getString(R.string.text_fir_onboard_third_sub_title), getString(R.string.text_sec_onboard_third_sub_title)));

        adapter = new OnBoardingAdapter(this, onBoardings);
        view_pager.setAdapter(adapter);
        tab_indicator.setupWithViewPager(view_pager);


        tab_indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == onBoardings.size() - 1) {
                    tab_indicator.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                } else {
                    tab_indicator.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btn_next.setOnClickListener(view -> {
            preference.setBoolean("isOnBoardingFirstTime", true);
            Intent intent = new Intent(this, StarterActivity.class);
            startActivity(intent);
            finish();
        });


    }

}