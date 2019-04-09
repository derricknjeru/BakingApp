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

        return prefs.getString(keyForTotalStep, "");

    }

    public static void setSetterTitle(Context context, String title_step_pos) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForTotalStep = context.getString(R.string.pref_tittle_step_key);

        editor.putString(keyForTotalStep, title_step_pos);

        editor.commit();
    }


    public static int getStepRecipeIdQuery(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForStep = context.getString(R.string.pref_step_recipe_key);

        return prefs.getInt(keyForStep, -1);

    }

    public static void setStepRecipeQuery(Context context, int recipe) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForStep = context.getString(R.string.pref_step_recipe_key);

        editor.putInt(keyForStep, recipe);

        editor.commit();
    }


    public static String getDisplayedWidget(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForTotalWidget = context.getString(R.string.pref_widget_key);

        return prefs.getString(keyForTotalWidget, context.getString(R.string.default_widget_recipe));
    }

    public static void setWidgetName(Context context, String widget_name) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForTotalWidget = context.getString(R.string.pref_widget_key);

        editor.putString(keyForTotalWidget, widget_name);

        editor.commit();
    }

    public static String getSavedJson(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForTotalJson = context.getString(R.string.pref_json_key);

        return prefs.getString(keyForTotalJson, "");
    }

    public static void saveJsonName(Context context, String json) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForTotalWidget = context.getString(R.string.pref_json_key);

        editor.putString(keyForTotalWidget, json);

        editor.commit();
    }
}
