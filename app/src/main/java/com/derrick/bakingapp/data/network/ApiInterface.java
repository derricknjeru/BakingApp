package com.derrick.bakingapp.data.network;

import com.derrick.bakingapp.data.local.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();

}
