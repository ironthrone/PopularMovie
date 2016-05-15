package com.example.popularmovies.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.popularmovies.R;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
