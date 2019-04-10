package com.derrick.bakingapp.UI.details;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Step;
import com.derrick.bakingapp.databinding.FragmentMasterListBinding;
import com.derrick.bakingapp.utils.AppExecutors;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;
import com.derrick.bakingapp.widget.WidgetIntentService;

import java.util.ArrayList;
import java.util.List;

import static com.derrick.bakingapp.widget.WidgetIntentService.UPDATE_THE_WIDGET;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterListFragment extends Fragment implements MasterListAdapter.OnStepClickListener {
    private static final String LOG_TAG = MasterListFragment.class.getSimpleName();
    private int recipeId;
    private FragmentMasterListBinding binding;
    private MasterListAdapter mMasterListAdapter;
    private List<Step> mStepList = new ArrayList<>();

    OnMasterListStepCLick mCallBack;

    public interface OnMasterListStepCLick {
        void OnStepClicked(int pos, long step_id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MasterListFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getRecipeId();
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_master_list, container, false);

        initializeViews();

        fetSteps();


        return binding.getRoot();
    }

    private void fetSteps() {
        MasterListViewModelFactory factory = InjectorUtils.provideMasterListViewModelFactory(getActivity(), recipeId);
        MasterListViewModel mViewModel = ViewModelProviders.of(this, factory).get(MasterListViewModel.class);

        mViewModel.getStepsListLiveData().observe(this, recipeList -> {
            if (recipeList != null && recipeList.size() > 0) {
                //will always be one

                mStepList = recipeList.get(0).getSteps();
                mMasterListAdapter.setStepList(mStepList, recipeId);
                LogUtils.showLog(LOG_TAG, "@Master list size::" + mStepList.size());

            }


        });

    }

    private void initializeViews() {
        binding.masterListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.masterListRecyclerView.setHasFixedSize(true);
        mMasterListAdapter = new MasterListAdapter(getActivity());

        binding.masterListRecyclerView.setAdapter(mMasterListAdapter);

        mMasterListAdapter.setOnStepClickListener(this);
    }

    private void getRecipeId() {
        recipeId = BakingPreference.getStepRecipeIdQuery(getActivity());
        LogUtils.showLog(LOG_TAG, "@MasterList recipeId::" + recipeId);
    }

    @Override
    public void OnStepClicked(int pos, long step_id) {

        mCallBack.OnStepClicked(pos - 1, recipeId);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //This makes sure the host activity has implemented the callback interface
        //if not it throws an exception
        try {
            mCallBack = (MasterListFragment.OnMasterListStepCLick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement OnMasterListStepCLick");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_widget) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                String name = InjectorUtils.provideRepository(getActivity()).currentRecipeName(recipeId);
                LogUtils.showLog(LOG_TAG, "@widget Selected name" + name);
                BakingPreference.setWidgetName(getActivity(), name);
                Intent i = new Intent();
                i.setAction(UPDATE_THE_WIDGET);
                WidgetIntentService.enqueueWork(getActivity(), i);
            });

            Toast.makeText(getActivity(), "added to widget", Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
