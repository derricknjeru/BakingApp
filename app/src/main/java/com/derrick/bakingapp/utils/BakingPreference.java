package com.derrick.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.derrick.bakingapp.R;

public class BakingPreference {
    public static int getStepPosQuery(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForStep = context.getString(R.string.pref_step_key);

        return prefs.getInt(keyForStep, 0);

    }

    public static void setStepQuery(Context context, int step_pos) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForStep = context.getString(R.string.pref_step_key);

        editor.putInt(keyForStep, step_pos);

        editor.commit();
    }

    public static int getTotalStepPosQuery(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForTotalStep = context.getString(R.string.pref_total_step_key);

        return prefs.getInt(keyForTotalStep, 0);

    }

    public static void setTotalStepQuery(Context context, int total_step_pos) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForTotalStep = context.getString(R.string.pref_total_step_key);

        editor.putInt(keyForTotalStep, total_step_pos);

        editor.commit();
    }

    public static String getStepperTitle(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForTotalStep = context.getString(R.string.pref_tittle_step_key);

        return prefs.getString(keyForTotalStep,"");

    }

    public static void setSetterTitle(Context context, String title_step_pos) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForTotalStep = context.getString(R.string.pref_tittle_step_key);

        editor.putString(keyForTotalStep, title_step_pos);

        editor.commit();
    }
}
