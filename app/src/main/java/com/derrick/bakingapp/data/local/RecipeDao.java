package com.derrick.bakingapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

@Dao
public interface RecipeDao {
    /**
     * Inserts a list of {@link Recipe} into the recipe_table
     *
     * @param result an object of recipe to insert
     */

    @Insert
    long[] bulkInsertRecipes(List<Recipe> result);


    //This method gets all recipes from the database
    @Query("SELECT * FROM recipe_table")
    List<Recipe> getAllRecipes();

    /**
     * getting specific column from the table
     *
     * @return
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id,name,servings,image,steps FROM recipe_table")
    LiveData<List<Recipe>> getRecipeDetailsLiveData();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id,steps FROM recipe_table where id=:id")
    LiveData<List<Recipe>> getStepsLiveData(int id);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id,ingredients FROM recipe_table where id=:recipe_id")
    List<Recipe> getIngredients(int recipe_id);
}
