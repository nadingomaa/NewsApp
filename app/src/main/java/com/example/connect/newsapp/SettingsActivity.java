package com.example.connect.newsapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_main);
            Preference topic = findPreference(getString(R.string.settings_topic_key));
            bindPreferenceSummaryToValue(topic);
            Preference section=findPreference("section");
            bindPreferenceSummaryToValue(section);



        }

        private void bindPreferenceSummaryToValue(Preference preference) {
             preference.setOnPreferenceChangeListener(this);
             SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
             String preferenceString = preferences.getString(preference.getKey(), "");
             onPreferenceChange(preference, preferenceString);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

                String stringValue = newValue.toString();
                preference.setSummary(stringValue);
                return true;
            }
        }
    }

