package com.ISquared.linkcleaner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static com.ISquared.linkcleaner.Constants.ACTIVITY;
import static com.ISquared.linkcleaner.Constants.FILTER;
import static com.ISquared.linkcleaner.Constants.THEME;

public class MainActivity extends AppCompatActivity {

    String TAG = "LinkCleaner";
    String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String activityPref = prefs.getString(ACTIVITY, null);
        String filters = prefs.getString(FILTER, Constants.FILTERS);
        Uri startIntentData;
        Theme theme = new Theme();
        int themeValue = theme.returnTheme(prefs.getInt(THEME, 0), getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);
        setTheme(themeValue);
        if ("text/plain".equals(getIntent().getType())) {

            String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            startIntentData = Uri.parse(sharedText);
            if (startIntentData.getHost() == null)
                startIntentData = null;
        } else {
            startIntentData = getIntent().getData();
        }

        if (startIntentData != null) {
            Url = startIntentData.toString();
            Log.d(TAG, Url);
            Url = decodeHTML(Url);
            Log.d(TAG, Url);
            Url = decodeHTML(Url);
            if (!filters.equals("invalid")) {
                Scanner scanner = new Scanner(filters);
                while (scanner.hasNextLine()) {
                    String[] customFilters = scanner.nextLine().split(",");
                    if (customFilters[0].equalsIgnoreCase("split")) {
                        if (Url.contains(customFilters[1])) {
                            try {
                                int index = Integer.parseInt(customFilters[3]);
                                if (Uri.parse(Url.split(Pattern.quote(customFilters[2]))[index]).getHost() != null)
                                    Url = Url.split(Pattern.quote(customFilters[2]))[index];
                            } catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                            Log.d(TAG, Url);
                        }
                    } else if (customFilters[0].equalsIgnoreCase("replace")) {
                        if (Url.contains(customFilters[1])) {
                            String replace;
                            if (customFilters.length == 3)
                                replace = "";
                            else
                                replace = customFilters[3];
                            if (Uri.parse(Url.replace(customFilters[2],
                                    replace)).getHost() != null)
                                Url = Url.replace(customFilters[2], replace);
                            Log.d(TAG, Url);
                        }
                    } else if (customFilters[0].equalsIgnoreCase("prepend")) {
                        if (Url.contains(customFilters[1])) {
                            if (Uri.parse(customFilters[2] + Url).getHost() != null)
                                Url = customFilters[2] + Url;
                            Log.d(TAG, Url);
                        }
                    } else if (customFilters[0].equalsIgnoreCase("append")) {
                        if (Url.contains(customFilters[1])) {
                            if (Uri.parse(Url + customFilters[2]).getHost() != null)
                                Url += customFilters[2];
                            Log.d(TAG, Url);
                        }
                    }
                }
            }
        }
        if (Url != null) {
            if (!Url.startsWith("http://") && (!Url.startsWith("https://")))
                Url = "https://" + Url;
            if (activityPref != null && isPackageInstalled(activityPref, getPackageManager())) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                intent.setPackage(activityPref);
                startActivity(intent);
                finish();
            } else {
                CustomBrowserFragment fragment = CustomBrowserFragment
                        .newInstance(getAppDataForLink(getApplicationContext(), Url), Url, null);
                fragment.show(getSupportFragmentManager(), "Testfornow");
            }
        } else {
            Toast.makeText(getApplicationContext(), "An Error Has Occurred Invalid Data", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }

    private String decodeHTML(String url) {
        return url.replace("%2F", "/")
                .replace("%3A", ":").replace("%3F", "?")
                .replace("%3D", "=").replace("%26", "&")
                .replace("%2B", "+").replace("%23", "#")
                .replace("%7C", "|").replace("%24", "$")
                .replace("%27", "'").replace("%25", "%");
    }

    private ActivityInfo[] getAppDataForLink(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        List<ResolveInfo> resolveInfos = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_ALL);
        ActivityInfo[] activityInfo = new ActivityInfo[resolveInfos.size()];
        for (int i = 0; i < resolveInfos.size(); i++) {
            activityInfo[i] = resolveInfos.get(i).activityInfo;
        }
        if (resolveInfos.isEmpty())
            return null;
        else
            return activityInfo;
    }
}
