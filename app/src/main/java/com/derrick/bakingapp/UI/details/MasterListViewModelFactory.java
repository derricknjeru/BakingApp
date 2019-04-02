package com.derrick.bakingapp.UI.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.derrick.bakingapp.UI.main.MainActivityViewModel;
import com.derrick.bakingapp.data.RecipeRepository;

public class MasterListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository mRepository;
    private int recipe_id;

    public MasterListViewModelFactory(RecipeRepository repository, int id) {
        this.mRepository = repository;
        this.recipe_id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MasterListViewModel(mRepository, recipe_id);
    }
}
