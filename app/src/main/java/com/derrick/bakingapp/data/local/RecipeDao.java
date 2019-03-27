package com.derrick.bakingapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
    @Query("SELECT id,name,servings,image FROM recipe_table")
    LiveData<List<Recipe>> getRecipeDetailsLiveData();

}
