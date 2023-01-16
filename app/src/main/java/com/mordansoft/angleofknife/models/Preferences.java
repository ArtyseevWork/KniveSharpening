package com.mordansoft.angleofknife.models;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String fileName = "preferences";
    private boolean firstStart;
    private static final boolean firstStartDefault = true;
    private static final String firstStartKey = "FIRST_START";


    public Preferences(boolean firstStart) {
        this.firstStart = firstStart;
    }

    public static boolean firstRunWizard(Context context){
        boolean isFirstStart = false;
        Preferences preferences = Preferences.getPreferencesFromFile(context);
        if (preferences.isFirstStart()){
            Knife.insertConstantsData(context);
            preferences.setCountdown(context,false);
            isFirstStart = true;
        }
        return isFirstStart;
    }

    public boolean isFirstStart() {
        return firstStart;
    }

    public void setCountdown(Context context, boolean firstStart) {
        this.firstStart = firstStart;
        SharedPreferences sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(firstStartKey, firstStart);
        editor.apply();
    }

    public static Preferences getPreferencesFromFile(Context context) {
       boolean firstStart;
       SharedPreferences sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
       firstStart = sharedPref.getBoolean(firstStartKey, firstStartDefault);
       return new Preferences(firstStart);
    }
}
