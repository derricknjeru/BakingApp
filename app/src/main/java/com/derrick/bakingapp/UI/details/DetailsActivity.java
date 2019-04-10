package com.derrick.bakingapp.UI.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.UI.steps.StepsActivity;
import com.derrick.bakingapp.UI.steps.StepsFragment;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.LogUtils;

public class DetailsActivity extends AppCompatActivity implements MasterListFragment.OnMasterListStepCLick {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_ID = "id";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private String title;


    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean isTablet;


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            title = getIntent().getStringExtra(EXTRA_TITLE);
            getSupportActionBar().setTitle(title);
        }

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            if (savedInstanceState == null) {
                AddStepperListFragment();
            }
        }

        toolbar.getTitle();


    }

    private void AddStepperListFragment() {

        BakingPreference.setStepQuery(this, 0);

        // In two-pane mode, add initial BodyPartFragments to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Creating a new Master list fragment
        StepsFragment stepsFragment = new StepsFragment();

        // Add the fragment to its container using a transaction
        fragmentManager.beginTransaction()
                .add(R.id.step_frag_container, stepsFragment)
                .commit();
    }

    @Override
    public void OnStepClicked(int pos, long recipe_id) {

        LogUtils.showLog(LOG_TAG, "@Details OnStepClicked pos::" + pos + " id::" + recipe_id);

        BakingPreference.setStepQuery(this, pos);

        if (isTablet) {

            // In two-pane mode, add initial BodyPartFragments to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Creating a new Master list fragment
            StepsFragment stepsFragment = new StepsFragment();

            // Add the fragment to its container using a transaction
            fragmentManager.beginTransaction()
                    .replace(R.id.step_frag_container, stepsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepsActivity.class);
            startActivity(intent);
        }

    }


}
