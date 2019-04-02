package com.derrick.bakingapp.UI.steps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.derrick.bakingapp.data.RecipeRepository;
import com.derrick.bakingapp.data.local.Recipe;

import java.util.List;

public class StepsFragmentViewModel extends ViewModel {
    public LiveData<List<Recipe>> getStepsListLiveData() {
        return mListLiveData;
    }

    LiveData<List<Recipe>> mListLiveData;

    public StepsFragmentViewModel(RecipeRepository mRepository, int recipe_id) {
        mListLiveData = mRepository.fetchStepSpecific(recipe_id);
    }
}
