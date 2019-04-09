package com.derrick.bakingapp.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.UI.details.DetailsActivity;
import com.derrick.bakingapp.data.local.Ingredient;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.derrick.bakingapp.UI.details.DetailsActivity.EXTRA_TITLE;

public class ListWidgetService extends RemoteViewsService {
    public static final String LIST_EXTRA = "list";
    private static final String LOG_TAG = ListWidgetService.class.getSimpleName();


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        List<Ingredient> mRecipeRecipeList = null;

        public ListRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;

            LogUtils.showLog(LOG_TAG, "@widget is called constructor intent");

            String jsonString = BakingPreference.getSavedJson(mContext);
            LogUtils.showLog(LOG_TAG, "@widget is called onGetViewFactory jsonString" + jsonString);

            if (!TextUtils.isEmpty(jsonString)) {
                mRecipeRecipeList = getRecipeList(jsonString);
            }

            LogUtils.showLog(LOG_TAG, "@widget is called onGetViewFactory mRecipeRecipeList" + mRecipeRecipeList);

        }

        private List<Ingredient> getRecipeList(String jsonString) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Ingredient>>() {
            }.getType();

            return gson.fromJson(jsonString, listType);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            String jsonString = BakingPreference.getSavedJson(mContext);

            LogUtils.showLog(LOG_TAG, "@widget is called onDataSetChanged jsonString" + jsonString);

            if (!TextUtils.isEmpty(jsonString)) {
                mRecipeRecipeList = getRecipeList(jsonString);
            }

            LogUtils.showLog(LOG_TAG, "@widget is called onDataSetChanged mRecipeRecipeList" + mRecipeRecipeList);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mRecipeRecipeList == null) return 0;
            return mRecipeRecipeList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            LogUtils.showLog(LOG_TAG, "@widget is called position" + position);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.row_widget);

            Ingredient mIngredient = mRecipeRecipeList.get(position);

            views.setTextViewText(R.id.widget_item_title, "\u2022 " + mIngredient.getIngredient() + " " +
                    mContext.getString(R.string.open_blacket) + mIngredient.getQuantity() + " "
                    + mIngredient.getMeasure() + mContext.getString(R.string.closing_blacket));

            String name = BakingPreference.getDisplayedWidget(mContext);


            Intent i = new Intent(mContext, DetailsActivity.class);
            Bundle extras = new Bundle();
            extras.putString(EXTRA_TITLE, name);
            i.putExtras(extras);
            views.setOnClickFillInIntent(R.id.row_id, i);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

