package com.derrick.bakingapp.utils;

import android.content.Context;

import com.derrick.bakingapp.UI.main.MainViewModelFactory;
import com.derrick.bakingapp.data.RecipeRepository;
import com.derrick.bakingapp.data.local.RecipeDatabase;
import com.derrick.bakingapp.data.network.RecipeNetworkDataSource;

public class InjectorUtils {

    public static RecipeNetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        return RecipeNetworkDataSource.getsInstance(context.getApplicationContext());
    }

    public static RecipeRepository provideRepository(Context context) {

        RecipeDatabase database = RecipeDatabase.getInstance(context.getApplicationContext());

        RecipeNetworkDataSource networkDataSource =
                RecipeNetworkDataSource.getsInstance(context.getApplicationContext());

        AppExecutors executors = AppExecutors.getInstance();

        return RecipeRepository.getsInstance(database, networkDataSource, executors, context);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        RecipeRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }


}
