package com.derrick.bakingapp.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.derrick.bakingapp.utils.InjectorUtils;

public class RecipeIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            InjectorUtils.provideNetworkDataSource(this).fetchRecipes();
        }
    }
}
