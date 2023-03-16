package com.example.steamprototype.data_op;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class SettingMethods {
    public SettingMethods() {

    }

    public void setLanguage(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Language", 0);
        String langCode = sharedPreferences.getString("lang_code", "");
        if (!langCode.equals("")) {
            setLocale(activity, langCode);
        }
    }

    private void setLocale(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences sharedPref = activity.getSharedPreferences("Language",0);
        sharedPref.edit().putString("lang_code", langCode).apply();
    }
}
