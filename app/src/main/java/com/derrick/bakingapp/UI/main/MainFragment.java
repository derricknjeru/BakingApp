package com.derrick.bakingapp.UI.main;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.databinding.FragmentMainBinding;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;

import java.util.List;

/**
 * A simple {@link MainFragment}
 */
public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    FragmentMainBinding binding;

    private RecipeListAdapter mListAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        initializeViews();

        fetchRecipes();


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void fetchRecipes() {
        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(getActivity());
        MainActivityViewModel mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getRecipeListLiveData().observe(this, recipes -> {
            LogUtils.showLog(LOG_TAG, "@Recipe total size::" + recipes);

            if (recipes != null && recipes.size() > 0) {
                mListAdapter.setRecipeList(recipes);
            }
        });
    }

    private void initializeViews() {
        mListAdapter = new RecipeListAdapter();
        binding.mainList.setAdapter(mListAdapter);
        binding.mainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.mainList.setHasFixedSize(true);
    }

}
