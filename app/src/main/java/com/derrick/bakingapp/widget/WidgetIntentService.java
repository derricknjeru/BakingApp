package com.derrick.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;

public class WidgetIntentService extends JobIntentService {
    public static final String UPDATE_THE_WIDGET = "update_widget";
    private static final String LOG_TAG = WidgetIntentService.class.getSimpleName();
    private static final int JOB_ID = 1;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.contentEquals(UPDATE_THE_WIDGET)) {
                updateTheWidget();
            }
        }
    }

    private void updateTheWidget() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        LogUtils.showLog(LOG_TAG, "@widget recipeList.....");

        String name = BakingPreference.getDisplayedWidget(this);

        InjectorUtils.provideRepository(this).fetchWidgetRecipe(name).observeForever(recipeList -> {
            LogUtils.showLog(LOG_TAG, "@widget recipeList" + recipeList.size());
            if (recipeList != null && recipeList.size() > 0) {

                LogUtils.showLog(LOG_TAG, "@widget recipeList" + recipeList.size());

                BakingPreference.setStepRecipeQuery(this, recipeList.get(0).getId());

                LogUtils.showLog(LOG_TAG, "@widget recipeList id"  + recipeList.get(0).getId());

                //Trigger data update to handle the ListView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list);

                //Now update all widgets
                BakingAppWidget.updateWidget(getApplicationContext(), appWidgetManager, recipeList, appWidgetIds);
            }
        });


    }


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, WidgetIntentService.class, JOB_ID, work);
    }
}
