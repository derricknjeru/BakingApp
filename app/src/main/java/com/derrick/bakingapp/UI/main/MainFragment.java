package com.derrick.bakingapp.UI.main;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.databinding.FragmentMainBinding;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.GridSpacingItemDecoration;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;

import java.util.List;

/**
 * A simple {@link MainFragment}
 */
public class MainFragment extends Fragment implements RecipeListAdapter.RecipeCLickListener {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    FragmentMainBinding binding;

    private RecipeListAdapter mListAdapter;

    OnRecipeClickListener mCallBack;
    private int numberOfColumns = 3;

    public interface OnRecipeClickListener {
        void itemSelected(int recipeId, String title);
    }

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
                showRecipeDataView(recipes);

            } else showLoading();
        });
    }

    private void showLoading() {
        binding.contentProgressBar.show();
    }

    private void showRecipeDataView(List<Recipe> recipes) {
        binding.contentProgressBar.hide();
        mListAdapter.setRecipeList(recipes);
    }

    private void initializeViews() {


        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        RecyclerView.LayoutManager mLayoutManager;

        if (isTablet) {
            mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);

        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        binding.mainList.setLayoutManager(mLayoutManager);
        binding.mainList.setHasFixedSize(true);
        binding.mainList.setItemAnimator(new DefaultItemAnimator());


        mListAdapter = new RecipeListAdapter();
        binding.mainList.setAdapter(mListAdapter);


        mListAdapter.setRecipeCLickListener(this);

        binding.contentProgressBar.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //This makes sure the host activity has implemented the callback interface
        //if not it throws an exception
        try {
            mCallBack = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onClickListener(int id, String title, int totalSteps) {
        BakingPreference.setTotalStepQuery(getActivity(), totalSteps);
        mCallBack.itemSelected(id, title);
    }
}
