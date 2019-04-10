package com.derrick.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.UI.details.DetailsActivity;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.LogUtils;
import com.google.gson.Gson;

import java.util.List;

import static com.derrick.bakingapp.UI.details.DetailsActivity.EXTRA_TITLE;
import static com.derrick.bakingapp.widget.WidgetIntentService.UPDATE_THE_WIDGET;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String LOG_TAG = BakingAppWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                List<Recipe> recipe, int appWidgetId) {

        // Create an Intent to launch MainActivity when clicked

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        views.setTextViewText(R.id.widget_recipe_title, recipe.get(0).getName());

        Intent i = new Intent(context, ListWidgetService.class);
        Gson gson = new Gson();

        BakingPreference.saveJsonName(context, gson.toJson(recipe.get(0).getIngredients()));

        views.setRemoteAdapter(R.id.appwidget_list, i);

        // This section makes it possible for items to have individualized behavior.
        // It does this by setting up a pending intent template. Individuals items of a collection
        // cannot set up their own pending intents. Instead, the collection as a whole sets
        // up a pending intent template, and the individual items set a fillInIntent
        // to create unique behavior on an item-by-item basis.
        Intent clickIntent = new Intent(context, DetailsActivity.class);

        PendingIntent clickPI = PendingIntent.getActivity(context, 0,
                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.appwidget_list, clickPI);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, List<Recipe> recipe, int[] appWidgetIds) {
        LogUtils.showLog(LOG_TAG, "@widget appWidgetIds size::" + appWidgetIds.length);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent i = new Intent();
        i.setAction(UPDATE_THE_WIDGET);
        WidgetIntentService.enqueueWork(context, i);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

