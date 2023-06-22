package com.google.mlkit.samples.nl.translate.java.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.samples.nl.translate.R;
import com.google.mlkit.samples.nl.translate.java.model.Conversation;
import com.google.mlkit.samples.nl.translate.java.model.History;
import com.google.mlkit.samples.nl.translate.java.model.Language;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GeneralPreference {

    public static Language getDefaultSource() {
        return new Language("English", "en", R.drawable.logo_mlkit, false, false);
    }

    public static Language getDefaultTarget() {
        return new Language("Hindi", "hi", R.drawable.logo_mlkit, false, false);
    }

    public static List<Language> getAvailableLanguages() {
        List<Language> languages = new ArrayList<>();
        List<String> languageIds = TranslateLanguage.getAllLanguages();
        for (String languageId : languageIds) {
            languages.add(new Language("", TranslateLanguage.fromLanguageTag(languageId), R.drawable.ic_baseline_translate_32, false, false));
        }
        return languages;
    }

    public static void showStatusBar(Activity activity, int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Window window = activity.getWindow();
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
        window.setNavigationBarColor(ContextCompat.getColor(activity, color));
//        }
    }

    public static void setLanguageForApp(Context context, String languageToLoad) {
        Locale locale;
        if (languageToLoad.equals("")) {
            //use any value for default
            locale = Locale.getDefault();
        } else {
            locale = new Locale(languageToLoad);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    @SuppressLint("SimpleDateFormat")
    public static String convertDateToString(Date d) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        return dateFormat.format(d);
    }


    public static ArrayList<Object> sortConversions(ArrayList<Conversation> conversions) {
        ArrayList<Object> finalList = new ArrayList<>();
        for (Conversation item : conversions) {
            if (isExist(finalList, getDate(item))) {
                finalList.add(item);
            } else {
                finalList.add(getDate(item));
                finalList.add(item);
            }
        }
        return finalList;
    }

    public static ArrayList<Object> sortHistories(ArrayList<History> histories) {
        ArrayList<Object> finalHistory = new ArrayList<>();
        for (History item : histories) {
            if (isExist(finalHistory, format(item.getSourceDate()))) { // HisObj, Jul 13, 2023
                finalHistory.add(item);
            } else {
                finalHistory.add(format(item.getSourceDate()));
                finalHistory.add(item);
            }
        }
        return finalHistory;
    }


    public static void addConversation(ArrayList<Object> conversions, Conversation conv) {
        if (isExist(conversions, getDate(conv))) {
            conversions.add(conv);
        } else {
            conversions.add(getDate(conv));
            conversions.add(conv);
        }
    }


    private static String getDate(Conversation item) {
        if (item.getIsSource() == 0) {
            return format(item.getSourceDate());
        } else {
            return format(item.getTargetDate());
        }
    }

    private static boolean isExist(ArrayList<Object> finalList, String date) {
        for (Object item : finalList) {
            if (item instanceof String && item.equals(date)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    public static String format(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date = dateFormat.parse(time);
            assert date != null;
            timeFormat.format(date);
            return timeFormat.format(date);
        } catch (Exception e) {
            Log.d("=========> Time ERR ", e.getMessage());
            return "00:00";
        }
    }


}

