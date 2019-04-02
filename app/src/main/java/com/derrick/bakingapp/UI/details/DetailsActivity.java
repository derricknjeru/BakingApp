package com.derrick.bakingapp.UI.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.UI.steps.StepsActivity;
import com.derrick.bakingapp.utils.LogUtils;

import static com.derrick.bakingapp.UI.steps.StepsActivity.EXTRA_ID;
import static com.derrick.bakingapp.UI.steps.StepsActivity.EXTRA_POS;
import static com.derrick.bakingapp.UI.steps.StepsActivity.EXTRA_TOTAL_STEPS;

public class DetailsActivity extends AppCompatActivity implements MasterListFragment.OnMasterListStepCLick {

    public static final String EXTRA_DATA = "recipe_id";
    public static final String EXTRA_TITLE = "title";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private String title;

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            title = getIntent().getStringExtra(EXTRA_TITLE);
        }

        getSupportActionBar().setTitle(title);

    }

    @Override
    public void OnStepClicked(int pos, long recipe_id, int total_steps) {

        LogUtils.showLog(LOG_TAG, "@Details OnStepClicked pos::" + pos + " id::" + recipe_id);
        if (mTwoPane) {

        } else {
            // Put this information in a Bundle and attach it to an Intent that will launch an AndroidMeActivity
            Bundle b = new Bundle();
            b.putInt(EXTRA_POS, pos);
            b.putLong(EXTRA_ID, recipe_id);
            b.putInt(EXTRA_TOTAL_STEPS, total_steps);

            // Attach the Bundle to an intent
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }

    }
}
