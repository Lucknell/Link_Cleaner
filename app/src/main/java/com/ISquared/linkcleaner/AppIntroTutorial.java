package com.ISquared.linkcleaner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class AppIntroTutorial extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Welcome to LinkCleaner");
        sliderPage.setDescription("LinkCleaner is an app that will clean links");
        sliderPage.setImageDrawable(R.drawable.icon_fg);
        //sliderPage.setBgColor(backgroundColor);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Link Cleaning?");
        sliderPage.setImageDrawable(R.drawable.linkcleaning);
        sliderPage.setDescription("Some applications add references to links that are clicked within" +
                " them. These references are used to track clicks and can cause additional loading or " +
                "may not work if you use an ad-blocker. This app can remove those references.");
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("How to get started");
        sliderPage.setDescription("Just click on a link! You can choose to set this application as your default web browser" +
                " or share links to LinkCleaner via the share menu.");
        sliderPage.setImageDrawable(R.drawable.clicklink);
        showSkipButton(true);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Share menu");
        sliderPage.setDescription("Sharing a link to LinkCleaner will clean the link and send you to your web browser of choice.");
        sliderPage.setImageDrawable(R.drawable.sharemenu);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Custom Filters");
        sliderPage.setDescription("You can edit the filters that the application uses by tapping on " +
                "custom filters and adding your filters to the list.");
        sliderPage.setImageDrawable(R.drawable.customfilters);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Filter types");
        sliderPage.setDescription("\nSplit\nReplace\nAppend\nPrepend\n" +
                "These 4 filters types are what help LinkCleaner remove references. All filters are " +
                "comma delimited.");
        sliderPage.setImageDrawable(0);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Split");
        sliderPage.setDescription("To use Split start a new line with Split, then what part of the link you " +
                "want to match, then the part of the link you want to separate into a list, and then what number minus one to " +
                "choose from the list.\nex. Match url=, separate the list at url=," +
                " equals then take the second item on the list\nSplit,url=,url=,1");
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Replace");
        sliderPage.setDescription("To use Replace start a new line with Replace, then the part of " +
                "the link you want to match, then part of the url you want to replace, and finally" +
                "the part that will take the place of what you wanted to replace.\nex. Match www, " +
                "replace www, with https://www\nReplace,www,www,https://wwww");
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Append");
        sliderPage.setDescription("To Use Append start a new line with Append, then the part of the " +
                "link you want to match, then whatever you would like to add to the end of the link" +
                "\nex. Match mytestsite.com/index, append .html/\nAppend,mytestsite.com/index,.html");
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("Prepend");
        sliderPage.setDescription("To Use Prepend start a new line with Prepend, then the part of the " +
        "link you want to match, then whatever you would like to add to the beginning of the link" +
                "\nex. Match mytestsite.com/index.html, prepend https://\nPrepend,mytestsite.com/index.html,https://");
        addSlide(AppIntroFragment.newInstance(sliderPage));
        sliderPage.setTitle("All set!");
        sliderPage.setDescription("Go get em!");
        setProgressButtonEnabled(true);
        addSlide(AppIntroFragment.newInstance(sliderPage));



        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(intent);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.edit().putBoolean("IntroTutSeen",true).apply();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(intent);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.edit().putBoolean("IntroTutSeen",true).apply();
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
