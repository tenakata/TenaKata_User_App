package com.tenakata.Utilities;

import android.graphics.Typeface;

import com.tenakata.Application.App;

public class FontFamily {


    public static Typeface getMonBold() {
        return Typeface.createFromAsset(App.getInstance().getAssets(), "font/montserrat_bold.otf");

    }

    public static Typeface getMonRegular() {
        return Typeface.createFromAsset(App.getInstance().getAssets(), "font/montserrat_regular.otf");
    }

    public static Typeface getPtBold() {
        return Typeface.createFromAsset(App.getInstance().getAssets(), "font/pt_sans_bold.ttf");

    }

    public static Typeface getPtRegular() {
        return Typeface.createFromAsset(App.getInstance().getAssets(), "font/pt_sans_regular.ttf");

    }

}
