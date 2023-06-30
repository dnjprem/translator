package com.voicetranslator.translate.languagetranslator.Ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.voicetranslator.translate.R;
import com.voicetranslator.translate.languagetranslator.MyApplication;
import com.preference.PowerPreference;

public class ADCLBAppLoadAds {
    public static ADCLBAppLoadAds appInstance;
    public static boolean appInterAdLoading = false;
    public MyCallback appCallback;
    public InterstitialAd appInterstitial = null;
    public static NativeAd appNativePreLoadOne = null, appNativePreLoadTwo = null;
    public AdLoader appLoaderNativeOne, appLoaderNativeTwo;

    public static ADCLBAppLoadAds getInstance() {
        if (appInstance == null) {
            appInstance = new ADCLBAppLoadAds();
        }
        return appInstance;
    }

    public interface MyCallback {
        void callbackCall();
    }

    public void loadInterstitialAds(final Activity context) {
            loadAdMobInterstitialAds(context);
    }

    public void loadBanner(final Activity activity, final LinearLayout linearLayout) {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(MyApplication.getAdModel().getAdsBannerId());
            AdSize adSize = getAdSize(activity);
            adView.setAdSize(adSize);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    linearLayout.removeAllViews();
                    linearLayout.addView(adView);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    loadBanner(activity, linearLayout);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }
            });
        }

    public void loadAdMobInterstitialAds(final Activity context) {
        if (appInterstitial == null && !appInterAdLoading ) {
            appInterAdLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, MyApplication.getAdModel().getAdsInterstitialId(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                    Toast.makeText(context, "Interstitial Ad Loaded", Toast.LENGTH_SHORT).show();
                    appInterstitial = interstitialAd;
                    appInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {

                            appInterstitial = null;
                            appInterAdLoading = false;

                                loadAdMobInterstitialAds(context);
                            if (appCallback != null) {
                                appCallback.callbackCall();
                                appCallback = null;
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appInterstitial = null;
                            appInterAdLoading = false;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            appInterstitial = null;
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    appInterstitial = null;
                    appInterAdLoading = false;
                }
            });
        }
    }


    public void displayFullScreenAds(Activity context, MyCallback _myCallback) {
        this.appCallback = _myCallback;
        if (PowerPreference.getDefaultFile().getInt(MyApplication.ADS_COUNT_SHOW, 0) == (PowerPreference.getDefaultFile().getBoolean("isFirstTime", false) ? MyApplication.getAdModel().getAdsInterstitialCountShow() : MyApplication.getAdModel().getAdsInterstitialCount())) {
            PowerPreference.getDefaultFile().putBoolean("isFirstTime", false);
            if (appInterstitial != null) {
                PowerPreference.getDefaultFile().putInt(MyApplication.ADS_COUNT_SHOW, 0);
                appInterstitial.show(context);
            } else {
                loadInterstitialAds(context);
                if (appCallback != null) {
                    appCallback.callbackCall();
                    appCallback = null;
                }
            }
        } else {
            PowerPreference.getDefaultFile().putInt(MyApplication.ADS_COUNT_SHOW, PowerPreference.getDefaultFile().getInt(MyApplication.ADS_COUNT_SHOW, 0) + 1);
            if (appCallback != null) {
                appCallback.callbackCall();
                appCallback = null;
            }
        }
    }

    public void displayFullScreenBackAds(Activity context, MyCallback _myCallback) {
        this.appCallback = _myCallback;
            if (PowerPreference.getDefaultFile().getInt(MyApplication.ADS_COUNT_BACK_SHOW, 0) == (PowerPreference.getDefaultFile().getBoolean("isBackFirstTime", false) ? MyApplication.getAdModel().getAdsInterstitialBackCountShow() : MyApplication.getAdModel().getAdsInterstitialBackCount())) {
                PowerPreference.getDefaultFile().putBoolean("isBackFirstTime", false);
                if (appInterstitial != null) {
                    PowerPreference.getDefaultFile().putInt(MyApplication.ADS_COUNT_BACK_SHOW, 0);
                    appInterstitial.show(context);
                } else {
                    loadInterstitialAds(context);
                    if (appCallback != null) {
                        appCallback.callbackCall();
                        appCallback = null;
                    }
                }
            } else {
                PowerPreference.getDefaultFile().putInt(MyApplication.ADS_COUNT_BACK_SHOW, PowerPreference.getDefaultFile().getInt(MyApplication.ADS_COUNT_BACK_SHOW, 0) + 1);
                if (appCallback != null) {
                    appCallback.callbackCall();
                    appCallback = null;
                }
            }
            if (appCallback != null) {
                appCallback.callbackCall();
                appCallback = null;
        }
    }

    public AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


    public void displayWrapBottomAds(Activity activity, FrameLayout frameViewAds) {
        displayBottomAdsAds(activity,frameViewAds,"Fix");
    }

    public void displayDyanamicBottomAds(Activity activity, FrameLayout frameViewAds) {
        displayBottomAdsAds(activity,frameViewAds,"No");
    }

    public void displayMatchBottomAds(Activity activity, FrameLayout frameViewAds) {
        displayBottomAdsAds(activity,frameViewAds,"Yes");
    }


    public void displayBottomAdsAds(Activity activity, FrameLayout frameViewAds, String showMedia) {
            if (ADCLBAppLoadAds.appNativePreLoadOne != null || ADCLBAppLoadAds.appNativePreLoadTwo != null) {
                loadGoogleNativePerLoadShow(activity, frameViewAds, showMedia);
            } else {
                loadGoogleAdNative(activity, frameViewAds, showMedia);
            }
        }

    public void loadGoogleNativePerLoadShow(Activity activity, FrameLayout frameViewAds, String showMedia) {
        if (appNativePreLoadOne != null) {

            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();
            NativeAdView adView = null;

            if (showMedia.equalsIgnoreCase("Yes")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native, null);
            } else if (showMedia.equalsIgnoreCase("No")) {
                if (MyApplication.getAdModel().getAdsNativeViewId() == 1) {
                    Log.d("=======> ViewId -> ", MyApplication.getAdModel().getAdsNativeViewId().toString());
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small, null);
                } else if (MyApplication.getAdModel().getAdsNativeViewId() == 2) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small_2, null);
                }
            } else if (showMedia.equalsIgnoreCase("Fix")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            }

            ADCLBAppLoadAds.getInstance().inflateGoogleNativeAd(activity, appNativePreLoadOne, adView);
            frameViewAds.addView(adView);

            if (!MyApplication.IS_NATIVE_AD_LAST) {
                appNativePreLoadTwo = null;
                loadGoogleAdNativePerLoadTwo(activity);
            }

        } else if (appNativePreLoadTwo != null) {
            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();

            NativeAdView adView = null;

            if (showMedia.equalsIgnoreCase("Yes")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native, null);
            } else if (showMedia.equalsIgnoreCase("No")) {
                if (MyApplication.getAdModel().getAdsNativeViewId() == 1) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small, null);
                } else if (MyApplication.getAdModel().getAdsNativeViewId() == 2) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small_2, null);
                }
            } else if (showMedia.equalsIgnoreCase("Fix")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            }

            ADCLBAppLoadAds.getInstance().inflateGoogleNativeAd(activity, appNativePreLoadTwo, adView);
            frameViewAds.addView(adView);

            if (!MyApplication.IS_NATIVE_AD_LAST) {
                appNativePreLoadOne = null;
                loadGoogleAdNativePerLoadOne(activity);
            }
        } else {
            loadGoogleAdNative(activity, frameViewAds, showMedia);
        }
    }

    public void loadGoogleAdNativePerLoadOne(Activity activity) {
        appNativePreLoadTwo = null;
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        appLoaderNativeOne = new AdLoader.Builder(activity, MyApplication.getAdModel().getAdsNativeId()).forNativeAd(nativeAd -> {
//            Toast.makeText(activity, "Loaded Native Ad One", Toast.LENGTH_SHORT).show();
            appNativePreLoadOne = nativeAd;
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        }).withNativeAdOptions(adOptions).build();

        AdRequest adRequest = new AdRequest.Builder().build();
        appLoaderNativeOne.loadAd(adRequest);
    }

    public void loadGoogleAdNativePerLoadTwo(Activity activity) {
        appNativePreLoadOne = null;
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        appLoaderNativeTwo = new AdLoader.Builder(activity, MyApplication.getAdModel().getAdsNativeId()).forNativeAd(nativeAd -> {
//            Toast.makeText(activity, "Loaded Native Ad Two", Toast.LENGTH_SHORT).show();
            appNativePreLoadTwo = nativeAd;
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        }).withNativeAdOptions(adOptions).build();
        AdRequest adRequest = new AdRequest.Builder().build();
        appLoaderNativeTwo.loadAd(adRequest);
    }

    public void loadGoogleAdNative(Activity activity, FrameLayout frameViewAds, String showMedia) {
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        AdLoader adLoaderNativeOne = new AdLoader.Builder(activity, MyApplication.getAdModel().getAdsNativeId()).forNativeAd(nativeAd -> {
            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();
//            Toast.makeText(activity, "Loaded Google Native Ad", Toast.LENGTH_SHORT).show();
            NativeAdView adView = null;
            if (showMedia.equalsIgnoreCase("Yes")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native, null);
            } else if (showMedia.equalsIgnoreCase("No")) {
                if (MyApplication.getAdModel().getAdsNativeViewId() == 1) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small, null);
                } else if (MyApplication.getAdModel().getAdsNativeViewId() == 2) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small_2, null);
                }
            } else if (showMedia.equalsIgnoreCase("Fix")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            }
            ADCLBAppLoadAds.getInstance().inflateGoogleNativeAd(activity, nativeAd, adView);
            frameViewAds.addView(adView);
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                displayBottomAdsAds(activity, frameViewAds, showMedia);
            }
        }).withNativeAdOptions(adOptions).build();

        AdRequest adRequest = new AdRequest.Builder().build();
        adLoaderNativeOne.loadAd(adRequest);
    }

    @SuppressLint("NewApi")
    public void inflateGoogleNativeAd(Activity activity, NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        //adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        AppCompatButton install = adView.findViewById(R.id.ad_call_to_action);
        install.setText(nativeAd.getCallToAction());
        if (MyApplication.getAdModel().getAdsNativeColor().equalsIgnoreCase("Yes")) {
            install.getBackground().setColorFilter(activity.getColor(R.color.color_1), PorterDuff.Mode.SRC_ATOP);
        } else if (MyApplication.getAdModel().getAdsNativeColor().equalsIgnoreCase("No")) {
            install.getBackground().setColorFilter(activity.getColor(R.color.color_2), PorterDuff.Mode.SRC_ATOP);
        } else {
            install.getBackground().setColorFilter(activity.getColor(R.color.color_1), PorterDuff.Mode.SRC_ATOP);
        }

        adView.setCallToActionView(install);

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
            CardView ad_app_icon_cards = adView.findViewById(R.id.ad_app_icon_cards);
            ad_app_icon_cards.setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
            CardView ad_app_icon_cards = adView.findViewById(R.id.ad_app_icon_cards);
            ad_app_icon_cards.setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getMediaContent().getVideoController();
        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }
}
