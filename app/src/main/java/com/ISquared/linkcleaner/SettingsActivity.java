package com.ISquared.linkcleaner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!pref.getBoolean("IntroTutSeen",false)){
        Intent intent = new Intent(getApplicationContext(),AppIntroTutorial.class);
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.settings_activity);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
            setTitle(R.string.settings_header);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        SharedPreferences pref;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference button = findPreference(getString(R.string.filterButton));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //code for what you want it to do
                    Intent myIntent = new Intent(getContext(), Filters.class);
                    Objects.requireNonNull(getActivity()).startActivity(myIntent);
                    getActivity().finish();
                    return true;
                }
            });
            Preference tutorial = findPreference(getString(R.string.tutButton));
            tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getContext(),AppIntroTutorial.class);
                    startActivity(intent);
                    return false;
                }
            });
            final ListPreference browser = findPreference("browsers");
            final SwitchPreference enable = findPreference("enableBrowser");
            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.mozilla.org"));
            List<ResolveInfo> resolveInfos = getContext().getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_ALL);
            Collections.sort(resolveInfos, new Comparator<ResolveInfo>() {
                @Override
                public int compare(ResolveInfo rI1, ResolveInfo rI2) {
                    return new AppAdapter.App(getContext(), rI1.activityInfo).getLabel()
                            .compareTo(new AppAdapter.App(getContext(), rI2.activityInfo).getLabel());
                }
            });
            String[] appnames = new String[resolveInfos.size() - 1];
            String[] activities = new String[resolveInfos.size() - 1];
            int i = -1;
            for (ResolveInfo info : resolveInfos) {
                if (info.activityInfo.packageName.equals(getActivity().getPackageName()))
                    continue;
                appnames[++i] = new AppAdapter.App(getContext(), info.activityInfo).getLabel();
                activities[i] = info.activityInfo.packageName;

            }

            browser.setEntries(appnames);
            browser.setEntryValues(activities);
            browser.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences.Editor editor = pref.edit();
                    browser.setValue((String) newValue);
                    editor.putString("activity", (String) newValue);
                    editor.apply();
                    return false;
                }
            });
            enable.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    browser.setEnabled(enable.isChecked());
                    if (!enable.isChecked()) {
                        SharedPreferences.Editor editor = pref.edit();
                        browser.setValue(null);
                        editor.putString("activity", null
                        );
                        editor.apply();
                    }
                    return false;
                }
            });
        }
    }
}