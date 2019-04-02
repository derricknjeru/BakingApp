package com.derrick.bakingapp.UI.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.derrick.bakingapp.data.RecipeRepository;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.data.local.Step;

import java.util.List;

public class MasterListViewModel extends ViewModel {
    public LiveData<List<Recipe>> getStepsListLiveData() {
        return mListLiveData;
    }

    LiveData<List<Recipe>> mListLiveData;

    public MasterListViewModel(RecipeRepository mRepository, int recipe_id) {
        mListLiveData = mRepository.fetchSteps(recipe_id);
    }
}
