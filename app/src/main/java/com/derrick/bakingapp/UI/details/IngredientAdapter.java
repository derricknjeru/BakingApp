package com.derrick.bakingapp.UI.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Ingredient;
import com.derrick.bakingapp.databinding.IngredientRowBinding;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.VHIngredient> {
    List<Ingredient> mIngredients;

    public IngredientAdapter(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    @NonNull
    @Override
    public VHIngredient onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View v = inflater.inflate(R.layout.ingredient_row, viewGroup, false);
        IngredientRowBinding rowBinding = IngredientRowBinding.inflate(inflater);

        return new VHIngredient(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VHIngredient holder, int pos) {
        Ingredient mIngredient = mIngredients.get(pos);
        holder.rowBinding.ingredient.setText("\u2022 " + mIngredient.getIngredient() +" "+
                holder.itemView.getContext().getString(R.string.open_blacket) + mIngredient.getQuantity() + " "
                + mIngredient.getMeasure() + holder.itemView.getContext().getString(R.string.closing_blacket));

    }

    @Override
    public int getItemCount() {
        return mIngredients != null && mIngredients.size() > 0 ? mIngredients.size() : 0;
    }

    public class VHIngredient extends RecyclerView.ViewHolder {
        IngredientRowBinding rowBinding;

        public VHIngredient(@NonNull IngredientRowBinding rowBinding) {
            super(rowBinding.getRoot());

            this.rowBinding = rowBinding;
        }
    }
}
