package com.derrick.bakingapp.UI.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.derrick.bakingapp.data.RecipeRepository;
import com.derrick.bakingapp.data.local.Recipe;

import java.util.List;

/**
 * {@link ViewModel} for {@link MainActivity}
 */

public class MainActivityViewModel extends ViewModel {
    private LiveData<List<Recipe>> mRecipeListLiveData;

    public MainActivityViewModel(RecipeRepository repository) {
        mRecipeListLiveData = repository.fetchRecipe();
    }

    public LiveData<List<Recipe>> getRecipeListLiveData() {
        return mRecipeListLiveData;
    }
}
