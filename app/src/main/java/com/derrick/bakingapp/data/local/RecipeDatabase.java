package com.derrick.bakingapp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.derrick.bakingapp.utils.LogUtils;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({IngredientConverter.class, StepConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String LOG_TAG = RecipeDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "recipe_db";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RecipeDatabase sInstance;

    public static RecipeDatabase getInstance(Context context) {
        LogUtils.showLog(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, RecipeDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration().build();
        }

        return sInstance;
    }

    // The associated DAOs for the database
    public abstract RecipeDao recipeDao();

}
