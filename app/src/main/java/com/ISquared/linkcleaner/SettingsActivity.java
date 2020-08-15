package com.ISquared.linkcleaner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.ISquared.linkcleaner.Constants.ACTIVITY;
import static com.ISquared.linkcleaner.Constants.AMOLED;
import static com.ISquared.linkcleaner.Constants.AMOLEDV;
import static com.ISquared.linkcleaner.Constants.DARK;
import static com.ISquared.linkcleaner.Constants.DARKV;
import static com.ISquared.linkcleaner.Constants.INTRO;
import static com.ISquared.linkcleaner.Constants.LIGHT;
import static com.ISquared.linkcleaner.Constants.LIGHTV;
import static com.ISquared.linkcleaner.Constants.SYSTEM;
import static com.ISquared.linkcleaner.Constants.SYSTEMV;
import static com.ISquared.linkcleaner.Constants.THEME;
import static com.ISquared.linkcleaner.Constants.THEME_SETTING;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!prefs.getBoolean(INTRO, false)) {
            Intent intent = new Intent(getApplicationContext(), AppIntroTutorial.class);
            startActivity(intent);
            finish();
        } else {
            Theme theme = new Theme();
            int themeValue = theme.returnTheme(prefs.getInt(THEME, 0), getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);
            setTheme(themeValue);
            setContentView(R.layout.activity_settings);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
            setTitle(R.string.settings_header);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        SharedPreferences prefs;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                    Intent intent = new Intent(getContext(), AppIntroTutorial.class);
                    startActivity(intent);
                    return false;
                }
            });
            final ListPreference theme = findPreference(getString(R.string.themeButton));
            switch (prefs.getInt(THEME, 0)) {

                case 1:
                    theme.setSummary(getString(THEME_SETTING) + getString(LIGHT));
                    theme.setValue(LIGHTV);
                    break;

                case 2:
                    theme.setSummary(getString(THEME_SETTING) + getString(AMOLED));
                    theme.setValue(AMOLEDV);
                    break;

                case 3:
                    theme.setSummary(getString(THEME_SETTING) + getString(SYSTEM));
                    theme.setValue(SYSTEMV);
                    break;

                default:
                    theme.setSummary(getString(THEME_SETTING) + getString(DARK));
                    theme.setValue(DARKV);
                    break;

            }
            theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    switch ((String) newValue) {

                        case LIGHTV:
                            prefs.edit().putInt(THEME, 1).apply();
                            theme.setSummary(getString(THEME_SETTING) + getString(LIGHT));
                            restartActivity(getActivity());
                            break;

                        case AMOLEDV:
                            prefs.edit().putInt(THEME, 2).apply();
                            theme.setSummary(getString(THEME_SETTING) + getString(AMOLED));
                            restartActivity(getActivity());
                            break;

                        case SYSTEMV:
                            prefs.edit().putInt(THEME, 3).apply();
                            theme.setSummary(getString(THEME_SETTING) + getString(SYSTEM));
                            restartActivity(getActivity());
                            break;

                        default:
                            prefs.edit().putInt(THEME, 0).apply();
                            theme.setSummary(getString(THEME_SETTING) + getString(DARK));
                            restartActivity(getActivity());
                            break;

                    }
                    return false;
                }
            });

            final ListPreference browser = findPreference("browsers");
            final SwitchPreference enable = findPreference("enableBrowser");
            browser.setEnabled(enable.isChecked());
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
            Preference about = findPreference(getString(R.string.aboutButton));
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    credits(getContext());
                    return false;
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
                    SharedPreferences.Editor editor = prefs.edit();
                    browser.setValue((String) newValue);
                    editor.putString(ACTIVITY, (String) newValue);
                    editor.apply();
                    return false;
                }
            });
            enable.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    browser.setEnabled(enable.isChecked());
                    if (!enable.isChecked()) {
                        SharedPreferences.Editor editor = prefs.edit();
                        browser.setValue(null);
                        editor.putString(ACTIVITY, null
                        );
                        editor.apply();
                    }
                    return false;
                }
            });
        }

        private void restartActivity(Activity activity) {
            activity.recreate();
        }

        private void credits(Context context) {
            Intent intent = new Intent(context, Credits.class);
            context.startActivity(intent);
        }
    }
}