package com.derrick.bakingapp.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.utils.LogUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.derrick.bakingapp.data.network.RecipeIntentService.FETCH_RECIPE_FROM_INTERNET;

public class RecipeNetworkDataSource {

    private static final String LOG_TAG = RecipeNetworkDataSource.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RecipeNetworkDataSource sInstance;

    private Context mContext;


    // LiveData storing api response
    private final MutableLiveData<List<Recipe>> recipeResponseList;

    private RecipeNetworkDataSource(Context context) {
        mContext = context;
        recipeResponseList = new MutableLiveData<>();
    }


    public static RecipeNetworkDataSource getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeNetworkDataSource(context);
            }
        }
        return sInstance;
    }

    void fetchRecipes() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> resultCall = apiService.getRecipes();

        resultCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                LogUtils.showLog(LOG_TAG, "@Recipe response::" + response.body());
                if (response.body() != null && response.body().size() > 0) {
                    recipeResponseList.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                LogUtils.showLog(LOG_TAG, "@Recipe t::" + t);
            }
        });

    }

    public MutableLiveData<List<Recipe>> getRecipeResponseList() {
        return recipeResponseList;
    }

    /**
     * Starting a service
     */
    public void StartService() {
        Intent i = new Intent(mContext, RecipeIntentService.class);
        i.setAction(FETCH_RECIPE_FROM_INTERNET);
        mContext.startService(i);
    }

    public void scheduleRecurringFetchMovieSync() {

    }
}
