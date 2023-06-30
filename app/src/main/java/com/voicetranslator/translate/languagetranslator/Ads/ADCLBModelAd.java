package com.voicetranslator.translate.languagetranslator.Ads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ADCLBModelAd {

    @SerializedName("ads_app_open_id")
    @Expose
    private String adsAppOpenId = "None";
    @SerializedName("ads_interstitial_id")
    @Expose
    private String adsInterstitialId = "None";
    @SerializedName("ads_banner_id")
    @Expose
    private String adsBannerId = "None";
    @SerializedName("ads_native_id")
    @Expose
    private String adsNativeId = "None";


    @SerializedName("ads_splash")
    @Expose
    private String adsSplash = "None";

    @SerializedName("ads_exit")
    @Expose
    private String adsExit = "None";


    @SerializedName("ads_banner")
    @Expose
    private String adsBanner = "Google";
    @SerializedName("ads_interstitial")
    @Expose
    private String adsInterstitial = "Google";
    @SerializedName("ads_interstitial_back")
    @Expose
    private String adsInterstitialBack = "Google";
    @SerializedName("ads_native")
    @Expose
    private String adsNative = "Google";
    @SerializedName("ads_app_open")
    @Expose
    private String adsAppOpen = "Google";


    @SerializedName("ads_interstitial_count")
    @Expose
    private Integer adsInterstitialCount = 3;
    @SerializedName("ads_interstitial_count_show")
    @Expose
    private Integer adsInterstitialCountShow = 3;
    @SerializedName("ads_interstitial_back_count")
    @Expose
    private Integer adsInterstitialBackCount = 3;
    @SerializedName("ads_interstitial_back_count_show")
    @Expose
    private Integer adsInterstitialBackCountShow = 3;
    @SerializedName("ads_native_view_id")
    @Expose
    private Integer adsNativeViewId = 1;
    @SerializedName("ads_native_color")
    @Expose
    private String adsNativeColor = "None";
    @SerializedName("ads_native_preload")
    @Expose
    private String adsNativePreload = "None";
    @SerializedName("ads_app_id")
    @Expose
    private String adsAppId = "None";
    @SerializedName("ads_app_up_date")
    @Expose
    private String adsAppUpDate = "None";
    @SerializedName("ads_app_up_date_link")
    @Expose
    private String adsAppUpDateLink = "None";


    public void setAdsBanner(String adsBanner) {
        this.adsBanner = adsBanner;
    }

    public void setAdsInterstitial(String adsInterstitial) {
        this.adsInterstitial = adsInterstitial;
    }

    public void setAdsInterstitialBack(String adsInterstitialBack) {
        this.adsInterstitialBack = adsInterstitialBack;
    }

    public void setAdsNative(String adsNative) {
        this.adsNative = adsNative;
    }

    public void setAdsAppOpen(String adsAppOpen) {
        this.adsAppOpen = adsAppOpen;
    }

    public String getAdsAppUpDateLink() {
        return adsAppUpDateLink;
    }

    public void setAdsAppUpDateLink(String adsAppUpDateLink) {
        this.adsAppUpDateLink = adsAppUpDateLink;
    }

    public String getAdsBanner() {
        return adsBanner;
    }

    public String getAdsNative() {
        return adsNative;
    }

    public String getAdsAppOpen() {
        return adsAppOpen;
    }

    public String getAdsInterstitial() {
        return adsInterstitial;
    }

    public String getAdsInterstitialBack() {
        return adsInterstitialBack;
    }


    public String getAdsAppOpenId() {
        return adsAppOpenId;
    }

    public void setAdsAppOpenId(String adsAppOpenId) {
        this.adsAppOpenId = adsAppOpenId;
    }

    public String getAdsInterstitialId() {
        return adsInterstitialId;
    }

    public void setAdsInterstitialId(String adsInterstitialId) {
        this.adsInterstitialId = adsInterstitialId;
    }

    public String getAdsBannerId() {
        return adsBannerId;
    }

    public void setAdsBannerId(String adsBannerId) {
        this.adsBannerId = adsBannerId;
    }

    public String getAdsNativeId() {
        return adsNativeId;
    }

    public void setAdsNativeId(String adsNativeId) {
        this.adsNativeId = adsNativeId;
    }

    public String getAdsSplash() {
        return adsSplash;
    }

    public void setAdsSplash(String adsSplash) {
        this.adsSplash = adsSplash;
    }


    public String getAdsExit() {
        return adsExit;
    }

    public void setAdsExit(String adsExit) {
        this.adsExit = adsExit;
    }

    public Integer getAdsInterstitialCount() {
        return adsInterstitialCount;
    }

    public void setAdsInterstitialCount(Integer adsInterstitialCount) {
        this.adsInterstitialCount = adsInterstitialCount;
    }

    public Integer getAdsInterstitialCountShow() {
        return adsInterstitialCountShow;
    }

    public void setAdsInterstitialCountShow(Integer adsInterstitialCountShow) {
        this.adsInterstitialCountShow = adsInterstitialCountShow;
    }

    public Integer getAdsInterstitialBackCount() {
        return adsInterstitialBackCount;
    }

    public void setAdsInterstitialBackCount(Integer adsInterstitialBackCount) {
        this.adsInterstitialBackCount = adsInterstitialBackCount;
    }

    public Integer getAdsInterstitialBackCountShow() {
        return adsInterstitialBackCountShow;
    }

    public void setAdsInterstitialBackCountShow(Integer adsInterstitialBackCountShow) {
        this.adsInterstitialBackCountShow = adsInterstitialBackCountShow;
    }

    public Integer getAdsNativeViewId() {
        return adsNativeViewId;
    }

    public void setAdsNativeViewId(Integer adsNativeViewId) {
        this.adsNativeViewId = adsNativeViewId;
    }

    public String getAdsNativeColor() {
        return adsNativeColor;
    }

    public void setAdsNativeColor(String adsNativeColor) {
        this.adsNativeColor = adsNativeColor;
    }

    public String getAdsNativePreload() {
        return adsNativePreload;
    }

    public void setAdsNativePreload(String adsNativePreload) {
        this.adsNativePreload = adsNativePreload;
    }

    public String getAdsAppId() {
        return adsAppId;
    }

    public void setAdsAppId(String adsAppId) {
        this.adsAppId = adsAppId;
    }

    public String getAdsAppUpDate() {
        return adsAppUpDate;
    }

    public void setAdsAppUpDate(String adsAppUpDate) {
        this.adsAppUpDate = adsAppUpDate;
    }
}