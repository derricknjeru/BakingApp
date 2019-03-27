package com.derrick.bakingapp.UI.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.databinding.RecipeRowBinding;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    public void setRecipeList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }

    private List<Recipe> mRecipeList;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        RecipeRowBinding rowBinding = RecipeRowBinding.inflate(inflater);
        return new RecipeViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int pos) {

        Recipe recipe = mRecipeList.get(pos);

        if (!isEmpty(recipe.getName())) {
            holder.mRowBinding.recipeTitle.setText(recipe.getName());
        }
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
