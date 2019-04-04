package com.derrick.bakingapp.UI.steps;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.utils.BakingPreference;

public class StepsActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "step_id";
    public static final String EXTRA_POS = "pos";
    public static final String EXTRA_TOTAL_STEPS = "steps_total";
    private static final String LOG_TAG = StepsActivity.class.getSimpleName();
    private long recipe_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Toolbar toolbar = findViewById(R.id.toolbar);
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


}
