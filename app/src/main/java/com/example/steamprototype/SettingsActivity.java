package com.example.steamprototype;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private Spinner langSpinner;
    public static final String[] languages = {"Default","English", "Vietnamese", "Japaneses", "Chinese", "Spanish", "French"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        init();
        loadLanguages();

    }
    private void loadLanguages() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.lang_spinner, languages);
        adapter.setDropDownViewResource(R.layout.lang_spinner);
        langSpinner.setAdapter(adapter);
        SharedPreferences sharedPref = SettingsActivity.this.getSharedPreferences("Position", Context.MODE_PRIVATE);
        int spinnerValue = sharedPref.getInt("spinner_item",-1);
        if(spinnerValue != -1) {
            langSpinner.setSelection(spinnerValue,true);
        } else {
            langSpinner.setSelection(0);
        }
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                SharedPreferences sharedPref = SettingsActivity.this.getSharedPreferences("Position",0);
                sharedPref.edit().putInt("spinner_item", i).apply();
                switch (selectedLang) {
                    case "English":
                        setLocale(SettingsActivity.this, "en");
                        break;
                    case "Vietnamese":
                        setLocale(SettingsActivity.this, "vi");
                        break;
                    case "Japaneses":
                        setLocale(SettingsActivity.this, "ja");
                        break;
                    case "Chinese":
                        setLocale(SettingsActivity.this, "zh");
                        break;
                    case "Spanish":
                        setLocale(SettingsActivity.this, "es");
                        break;
                    case "French":
                        setLocale(SettingsActivity.this, "fr");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        sharedPref.edit().putBoolean("lang_change", true).apply();

        activity.finish();
        startActivity(activity.getIntent());
    }


    private void init() {
        langSpinner = findViewById(R.id.select_lang_spinner);
    }
}
