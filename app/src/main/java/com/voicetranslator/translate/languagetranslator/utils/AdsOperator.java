package com.voicetranslator.translate.languagetranslator.utils;

import android.app.Activity;

import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;

public class AdsOperator {

    // Use with Fixed Size FrameLayout
    // To Display Fixed Size Ads
    // At Top, Bottom Center of layout
    public static void showMainAdsOnCreateActivity(Activity context) {
        ADCLBAppLoadAds.getInstance().displayMatchBottomAds(context, context.findViewById(R.id.frameViewAdsMain));
    }

    // To Display Dynamic Ads
    // At Top, Bottom Center of layout
    public static void showDynamicAdsOnCreateActivity(Activity context) {
        ADCLBAppLoadAds.getInstance().displayDyanamicBottomAds(context, context.findViewById(R.id.frameViewAds));
    }

    // For WRAP_CONTENT ADS
    // At Top, Bottom Center of layout
    public static void showWrapAdsOnCreateActivity(Activity context) {
        ADCLBAppLoadAds.getInstance().displayWrapBottomAds(context, context.findViewById(R.id.frameViewAds));
    }

    public static void showAdsOnStartActivity(Activity context) {
        ADCLBAppLoadAds.getInstance().displayFullScreenAds(context, () -> {});
    }

    public static void showAdsOnFinishActivity(Activity context) {
        ADCLBAppLoadAds.getInstance().displayFullScreenBackAds(context, () -> {
        });
    }
}
