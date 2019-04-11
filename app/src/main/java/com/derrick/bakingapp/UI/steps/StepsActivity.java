package com.derrick.bakingapp.UI.steps;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.utils.BakingPreference;

public class StepsActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setStepperFragment();

        getSupportActionBar().setTitle(BakingPreference.getStepperTitle(this));

    }


    private void setStepperFragment() {
        // Create a new head StepsFragment
        StepsFragment stepFragment = new StepsFragment();

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.step_container, stepFragment)
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideViews(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            hideViews(View.VISIBLE);
        }
    }

    private void hideViews(int visibility) {
        toolbar.setVisibility(visibility);
    }


}
