package com.google.mlkit.samples.nl.translate.java.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.Ads.ADCLBAppLoadAds;
import com.google.mlkit.samples.nl.translate.java.MyApplication;

public class Constant {

    public static native String getMain();

    public static void showExitDialog(Activity activity) {

        try {
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
