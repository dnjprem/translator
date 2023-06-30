package com.voicetranslator.translate.languagetranslator.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.Ads.ADCLBAppLoadAds;
import com.voicetranslator.translate.languagetranslator.MyApplication;

public class Constant {

    static {
        System.loadLibrary("native-lib");
        Log.d("=======> native-lib -> ","Library Loaded");
    }

    public static native String getMain();
    public static native String getKey1();
    public static native String getKey2();

    public static void  showExitDialog(Activity activity) {

        try {
            Log.d("=======> native-lib -> ","Main is -> "+ getMain());
            Log.d("=======> native-lib -> ","getKey1 is -> "+ getKey1());
            Log.d("=======> native-lib -> ","getKey2 is -> "+ getKey2());
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setOnShowListener(dialogInterface -> {
                if (MyApplication.getAdModel().getAdsExit().equalsIgnoreCase("YES")) {
                    dialog.findViewById(R.id.cardViewAdsMain).setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> ADCLBAppLoadAds.getInstance().displayBottomAdsAds(activity, dialog.findViewById(R.id.frameViewAdsMain), "Yes"), 500);
                } else {
                    dialog.findViewById(R.id.cardViewAdsMain).setVisibility(View.GONE);
                }
            });
            dialog.findViewById(R.id.btnYes).setOnClickListener(view -> {
                dialog.dismiss();
                activity.finishAffinity();
            });

            dialog.findViewById(R.id.btnNo).setOnClickListener(view -> {
                dialog.dismiss();
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
