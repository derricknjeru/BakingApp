package com.derrick.bakingapp.UI.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.databinding.RecipeRowBinding;
import com.derrick.bakingapp.utils.LogUtils;

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
        void onClickListener(int id, String title);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
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

        int color = getRandomMaterialColor(holder.itemView.getContext());

        LogUtils.showLog(LOG_TAG, "@Recipe color::" + color);

        Drawable myIcon = holder.itemView.getContext().getResources().getDrawable(R.drawable.rounded_drawable);
        myIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        holder.mRowBinding.titleEnd.setBackground(myIcon);

        holder.itemView.setOnClickListener(v -> mRecipeCLickListener.onClickListener(recipe.getId(), recipe.getName()));


    }

    private int getRandomMaterialColor(Context context) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier(context.getString(R.string.color_key), context.getString(R.string.array_key), context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
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
