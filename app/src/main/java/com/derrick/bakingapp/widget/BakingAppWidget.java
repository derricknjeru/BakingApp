package com.derrick.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.UI.details.DetailsActivity;
import com.derrick.bakingapp.UI.main.MainActivity;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.LogUtils;
import com.google.gson.Gson;

import java.util.List;

import static com.derrick.bakingapp.UI.details.DetailsActivity.*;
import static com.derrick.bakingapp.UI.details.DetailsActivity.EXTRA_ID;
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

        //Open details activity
        Intent intent = new Intent(context, DetailsActivity.class);

        intent.putExtra(EXTRA_TITLE, recipe.get(0).getName());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.main_widget_view, pendingIntent);

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

