package com.ISquared.linkcleaner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    String TAG = "LinkCleaner";
    String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String activityPref = prefs.getString("activity", null);
        String filters = prefs.getString("filters", "invalid");
        Uri startIntentData;
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
            /*if (Url.contains("PARM1=")) {
                Url = Url.split("PARM1=")[1];
                Log.d(TAG, Url);
            }
            if (Url.contains("url=")) {
                Url = Url.split("url=")[1];
                Log.d(TAG, Url);
            }
            if (Url.contains("clicks.slickdeals.net/i.php?u1=http")) {
                Url = Url.split("u2=")[1];
                Log.d(TAG, Url);
            }
            if (startIntentData.toString().contains("1225267-11965372?")) {
                Url = "https://staples.com" + Url;
                Log.d(TAG, Url);
            }
            if (Url.contains("?gclid")) {
                Url = Url.split("&url=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("link=")) {
                Url = Url.split("link=")[1];
                Log.d(TAG, Url);
            }
            if (Url.contains("u=")) {//brickseek
                if (Uri.parse(Url.split("u=")[1]).getHost() != null)
                    Url = Url.split("u=")[1];
                Log.d(TAG, Url);
            }
            if (Url.contains("h=")) {
                Url = Url.split("h=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("utm_")) {
                Url = Url.split("utm_")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("&nm_"))// newegg
            {
                Url = Url.split("&nm_")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("amp/s"))//amp pages
            {
                Url = Url.split("amp/s/")[1];
                Url = Url.replace("amp/", "");
                Log.d(TAG, Url);
            }
            if (Url.contains("bhphotovideo.com")) {
                Url = Url.split(".html/")[0] + ".html/";
                Log.d(TAG, Url);
            }
            if (Url.contains("ref=")) {
                Url = Url.split("ref=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("u1=")) {
                Url = Url.split("u1=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("mpre=")) {
                Url = Url.split("ref=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("&a=")) {
                Url = Url.split("&a=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("q=")) {
                Url = Url.split("q=")[1];
                Log.d(TAG, Url);
            }
            if (Url.contains("token=")) {
                Url = Url.split("token=")[0];
                Log.d(TAG, Url);
            }
            Url = decodeHTML(Url);
            Log.d(TAG, Url);
            if (Url.contains("src=")) {
                Url = Url.split("src=")[0];
                Log.d(TAG, Url);
            }
            if (Url.contains("&sa=D&")) {
                Url = Url.split("&sa=D&")[0];
                Log.d(TAG, Url);
            }*/
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
                            if (customFilters.length ==3)
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
                            if (Uri.parse(customFilters[2]+Url).getHost() != null)
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
