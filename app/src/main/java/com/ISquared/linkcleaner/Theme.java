package com.ISquared.linkcleaner;

import android.content.res.Configuration;

public class Theme {
    public int returnTheme(int theme, int systemTheme) {
        switch (theme) {

            case 1:
                return R.style.AppTheme;

            case 2:
                return R.style.amoledBlack;

            case 3:
                return systemTheme == Configuration.UI_MODE_NIGHT_NO ? R.style.AppTheme : R.style.darkMode;

            default:
                return R.style.darkMode;
        }
    }
}
