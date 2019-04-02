package com.derrick.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.data.local.RecipeDao;
import com.derrick.bakingapp.data.local.RecipeDatabase;
import com.derrick.bakingapp.data.network.RecipeNetworkDataSource;
import com.derrick.bakingapp.utils.AppExecutors;
import com.derrick.bakingapp.utils.LogUtils;

import java.util.List;


public class RecipeRepository {
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();
    private static RecipeRepository sInstance;

    private final RecipeDao mRecipeDao;
    private final RecipeNetworkDataSource mRecipeNetworkDatasource;
    private AppExecutors mAppExecutors;
    private Context mContext;

    public RecipeRepository(RecipeDatabase database, RecipeNetworkDataSource networkDataSource, AppExecutors executors, Context context) {
        mRecipeDao = database.recipeDao();
        mRecipeNetworkDatasource = networkDataSource;
        mAppExecutors = executors;
        mContext = context;
        saveRecipe();
    }

    private void saveRecipe() {
        LiveData<List<Recipe>> mRecipeLiveData = mRecipeNetworkDatasource.getRecipeResponseList();
        mRecipeLiveData.observeForever(recipes -> mAppExecutors.diskIO().execute(() -> {
            long[] insert = mRecipeDao.bulkInsertRecipes(recipes);
            LogUtils.showLog(LOG_TAG, "@Recipe insert::" + insert);
        }));
    }

    public synchronized static RecipeRepository getsInstance(RecipeDatabase recipeDatabase, RecipeNetworkDataSource networkDataSource, AppExecutors appExecutors, Context context) {
        if (sInstance == null) {
            //new repository is created
            synchronized (LOCK) {
                sInstance = new RecipeRepository(recipeDatabase, networkDataSource, appExecutors, context);
            }

        }
        return sInstance;
    }

    public LiveData<List<Recipe>> fetchRecipe() {
        initializeData();
        return mRecipeDao.getRecipeDetailsLiveData();
    }

    public LiveData<List<Recipe>> fetchSteps(int id) {
        return mRecipeDao.getStepsLiveData(id);
    }

    public LiveData<List<Recipe>> fetchStepSpecific(int id) {
        return mRecipeDao.getStepsLiveData(id);
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {
        //not needed for this task
        //scheduleRecurringFetchMovieSync();

        mAppExecutors.diskIO().execute(() -> {
            if (!localDbHasData()) {
                //There are no recipes in the local db, so we fetch some
                LogUtils.showLog(LOG_TAG, "@Recipe fetching...::");
                mRecipeNetworkDatasource.StartService();
            } else {
                LogUtils.showLog(LOG_TAG, "@Recipe database has data::");
            }
        });


    }

    private boolean localDbHasData() {
        return mRecipeDao.getAllRecipes() != null && mRecipeDao.getAllRecipes().size() > 0;
    }

    public List<Recipe> fetIngredients(int recipe_id) {
        return mRecipeDao.getIngredients(recipe_id);
    }
}
