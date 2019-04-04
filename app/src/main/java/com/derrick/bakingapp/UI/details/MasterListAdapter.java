package com.derrick.bakingapp.UI.details;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derrick.bakingapp.R;
import com.derrick.bakingapp.data.local.Ingredient;
import com.derrick.bakingapp.data.local.Recipe;
import com.derrick.bakingapp.data.local.Step;
import com.derrick.bakingapp.databinding.MasterListHeaderRowBinding;
import com.derrick.bakingapp.databinding.MasterListStepsRowBinding;
import com.derrick.bakingapp.utils.AppExecutors;
import com.derrick.bakingapp.utils.BakingPreference;
import com.derrick.bakingapp.utils.InjectorUtils;
import com.derrick.bakingapp.utils.LogUtils;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class MasterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final String LOG_TAG = MasterListAdapter.class.getSimpleName();
    private int mRecipeId;
    AppExecutors mAppExecutors;
    FragmentActivity mActivity;
    int selectedPosition = -1;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void setOnStepClickListener(OnStepClickListener mOnStepClickListener) {
        this.mOnStepClickListener = mOnStepClickListener;
    }

    OnStepClickListener mOnStepClickListener;

    public interface OnStepClickListener {
        void OnStepClicked(int pos, long step_id);
    }

    public MasterListAdapter(FragmentActivity activity) {
        mAppExecutors = AppExecutors.getInstance();
        mActivity = activity;
    }

    public void setStepList(List<Step> mStepList, int recipeId) {
        this.mStepList = mStepList;
        notifyDataSetChanged();
        this.mRecipeId = recipeId;
    }

    private List<Step> mStepList;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            MasterListStepsRowBinding rowBinding = MasterListStepsRowBinding.inflate(inflater);
            return new VHItem(rowBinding);

        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            MasterListHeaderRowBinding rowBinding = MasterListHeaderRowBinding.inflate(inflater);
            return new VHHeader(rowBinding);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        if (holder instanceof VHItem) {
            //cast holder to VHItem and set data
            displaySteps(((VHItem) holder).mRowBinding, pos);
        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            displayIngredients(holder, mRecipeId);
        }
    }

    private void displaySteps(MasterListStepsRowBinding mRowBinding, int pos) {
        Step step = mStepList.get(pos - 1);

        LogUtils.showLog(LOG_TAG, "@Fetched step::" + step);
        if (!isEmpty(step.getShortDescription())) {
            mRowBinding.stepTitle.setText(step.getShortDescription());
        }

        setRowBackgroundColor(mRowBinding, pos);

        mRowBinding.getRoot().setOnClickListener(v -> {
            mOnStepClickListener.OnStepClicked(pos, step.getId());
            BakingPreference.setSetterTitle(mActivity, step.getShortDescription());

            selectedPosition = pos;
            notifyDataSetChanged();

        });

    }


    private void setRowBackgroundColor(MasterListStepsRowBinding mRowBinding, int pos) {
        if (selectedPosition == pos) {
            mRowBinding.getRoot().setBackgroundColor(ContextCompat.getColor(mRowBinding.getRoot().getContext(), R.color.row_activated));
        } else if (selectedPosition == -1 && pos == 1) {
            mRowBinding.getRoot().setBackgroundColor(ContextCompat.getColor(mRowBinding.getRoot().getContext(), R.color.row_activated));
        } else {
            mRowBinding.getRoot().setBackgroundColor(ContextCompat.getColor(mRowBinding.getRoot().getContext(), R.color.white));
        }
    }

    private void displayIngredients(RecyclerView.ViewHolder holder, int recipe_id) {
        mAppExecutors.diskIO().execute(() -> {
            List<Recipe> mRecipeList = InjectorUtils.provideRepository(mActivity)
                    .fetIngredients(recipe_id);
            //The list size will always be one
            if (mRecipeList != null && mRecipeList.size() > 0) {
                List<Ingredient> mIngredients = mRecipeList.get(0).getIngredients();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showIngredients(mIngredients, holder);
                    }
                });

            }

        });
    }

    private void showIngredients(List<Ingredient> mIngredients, RecyclerView.ViewHolder holder) {
        LogUtils.showLog(LOG_TAG, "@Ingredients mIngredients" + mIngredients);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ((VHHeader) holder).mRowBinding.headerList.setLayoutManager(layoutManager);
        ((VHHeader) holder).mRowBinding.headerList.setHasFixedSize(true);
        ((VHHeader) holder).mRowBinding.headerList.setAdapter(new IngredientAdapter(mIngredients));
    }

    @Override
    public int getItemCount() {
        return mStepList != null && mStepList.size() > 0 ? mStepList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    class VHItem extends RecyclerView.ViewHolder {
        MasterListStepsRowBinding mRowBinding;

        public VHItem(MasterListStepsRowBinding rowBinding) {
            super(rowBinding.getRoot());
            mRowBinding = rowBinding;
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        MasterListHeaderRowBinding mRowBinding;

        public VHHeader(MasterListHeaderRowBinding rowBinding) {
            super(rowBinding.getRoot());

            mRowBinding = rowBinding;
        }
    }

    public void toggleSelection(int pos) {
        selectedPosition = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

}
