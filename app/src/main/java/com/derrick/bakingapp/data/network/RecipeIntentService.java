package com.derrick.bakingapp.data.network;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;
import com.derrick.bakingapp.widget.BakingAppWidget;

public class RecipeIntentService extends IntentService {
    public static final String FETCH_RECIPE_FROM_INTERNET = "fetch";


    private static final String LOG_TAG = RecipeIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.contentEquals(FETCH_RECIPE_FROM_INTERNET)) {
                InjectorUtils.provideNetworkDataSource(this).fetchRecipes();
            }
        }
    }


}
