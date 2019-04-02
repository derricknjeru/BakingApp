package com.derrick.bakingapp.UI.steps;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.derrick.bakingapp.data.RecipeRepository;

public class StepsFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository mRepository;
    private int recipe_id;

    public StepsFragmentViewModelFactory(RecipeRepository repository, int id) {
        this.mRepository = repository;
        this.recipe_id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StepsFragmentViewModel(mRepository, recipe_id);
    }
}
