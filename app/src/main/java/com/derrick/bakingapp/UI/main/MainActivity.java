package com.derrick.bakingapp.UI.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.UI.details.DetailsActivity;
import com.derrick.bakingapp.utils.LogUtils;

import static com.derrick.bakingapp.UI.details.DetailsActivity.*;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecipeClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void itemSelected(int recipeId, String title) {
        LogUtils.showLog(LOG_TAG, "@Listener recipeId::" + recipeId);
        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
        i.putExtra(EXTRA_DATA, recipeId);
        i.putExtra(EXTRA_TITLE, title);
        startActivity(i);
    }
}
