package com.example.calculatenow.view;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;

import com.example.calculatenow.R;

import java.util.Objects;

/**
 * Created by NinpoU-u on 21/01/20.
 */

public class SettingsActivity extends AppCompatActivity {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("pref_dark", false))
            switch (Objects.requireNonNull(sp.getString("pref_theme", "0"))) {
                case "0":
                    setTheme(R.style.AppTheme_Dark_Blue);
                    break;
                case "1":
                    setTheme(R.style.AppTheme_Dark_Cyan);
                    break;
                case "2":
                    setTheme(R.style.AppTheme_Dark_Gray);
                    break;
                case "3":
                    setTheme(R.style.AppTheme_Dark_Green);
                    break;
                case "4":
                    setTheme(R.style.AppTheme_Dark_Purple);
                    break;
                case "5":
                    setTheme(R.style.AppTheme_Dark_Red);
                    break;
            }
        else
            switch (Objects.requireNonNull(sp.getString("pref_theme", "0"))) {
                case "0":
                    setTheme(R.style.AppTheme_Light_Blue);
                    break;
                case "1":
                    setTheme(R.style.AppTheme_Light_Cyan);
                    break;
                case "2":
                    setTheme(R.style.AppTheme_Light_Gray);
                    break;
                case "3":
                    setTheme(R.style.AppTheme_Light_Green);
                    break;
                case "4":
                    setTheme(R.style.AppTheme_Light_Purple);
                    break;
                case "5":
                    setTheme(R.style.AppTheme_Light_Red);
                    break;
            }

        setContentView(R.layout.activity_settings);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            findPreference("pref_theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    ListPreference lp = (ListPreference) findPreference("pref_theme");
                    String url = getActivity().getPackageName();
                    String previous = url + ".MainActivity" + lp.getEntry();
                    String next = url + ".MainActivity" + lp.getEntries()[Integer.parseInt(o.toString())];
                    getActivity().getPackageManager().setComponentEnabledSetting(
                            new ComponentName(url, previous),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    getActivity().getPackageManager().setComponentEnabledSetting(
                            new ComponentName(url, next),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    getActivity().finish();
                    TaskStackBuilder.create(getActivity())
                            .addNextIntent(new Intent(getActivity(), MainActivity.class))
                            .addNextIntent(getActivity().getIntent())
                            .startActivities();
                    return true;
                }
            });
            findPreference("pref_dark").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    getActivity().finish();
                    TaskStackBuilder.create(getActivity())
                            .addNextIntent(new Intent(getActivity(), MainActivity.class))
                            .addNextIntent(getActivity().getIntent())
                            .startActivities();
                    return true;
                }
            });
        }
    }
}