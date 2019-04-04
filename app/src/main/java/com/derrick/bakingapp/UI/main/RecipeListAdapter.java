package com.derrick.bakingapp.UI.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.data.local.Step;
import com.derrick.bakingapp.databinding.RecipeRowBinding;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private static final String LOG_TAG = RecipeListAdapter.class.getSimpleName();

    public void setRecipeList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }

    private List<Recipe> mRecipeList;

    private RecipeCLickListener mRecipeCLickListener;

    public void setRecipeCLickListener(RecipeCLickListener mRecipeCLickListener) {
        this.mRecipeCLickListener = mRecipeCLickListener;
    }

    public interface RecipeCLickListener {
        void onClickListener(int id, String title, int totalSteps);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        RecipeRowBinding rowBinding = RecipeRowBinding.inflate(inflater, viewGroup, false);

        return new RecipeViewHolder(rowBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int pos) {

        Recipe recipe = mRecipeList.get(pos);

        if (!isEmpty(recipe.getName())) {
            holder.mRowBinding.recipeTitle.setText(recipe.getName());
        }
        holder.itemView.setOnClickListener(v -> mRecipeCLickListener.onClickListener(recipe.getId(), recipe.getName(), recipe.getSteps().size()));


    }

    @Override
    public int getItemCount() {
        return mRecipeList != null && mRecipeList.size() > 0 ? mRecipeList.size() : 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        RecipeRowBinding mRowBinding;

        public RecipeViewHolder(@NonNull RecipeRowBinding v) {
            super(v.getRoot());

            mRowBinding = v;

        }
    }
}
