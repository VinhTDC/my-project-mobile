package vn.edu.tdc.doan_d2.model.category;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class CategoryDiffCallback extends DiffUtil.Callback{
    private final List<BaseCategory> oldCategories;
    private final List<BaseCategory> newCategories;

    public CategoryDiffCallback(List<BaseCategory> oldCategories, List<BaseCategory> newCategories) {
        this.oldCategories = oldCategories;
        this.newCategories = newCategories;
    }

    @Override
    public int getOldListSize() {
        return oldCategories.size(); // No need for null check, an empty list has size 0
    }

    @Override
    public int getNewListSize() {
        return newCategories.size(); // Same as above
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        BaseCategory oldItem = oldCategories.get(oldItemPosition);
        BaseCategory newItem = newCategories.get(newItemPosition);

        // Use a unique identifier for comparison (e.g., an ID or a combination of fields)
        // In your case, you were using name. Be sure it's unique. If not, use IDs for reliability
        return oldItem.getId().equals(newItem.getId()); // Assuming you have an ID field in BaseCategory
    }

    @SuppressLint("DiffUtilEquals") // Suppress warning about direct object comparison
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseCategory oldItem = oldCategories.get(oldItemPosition);
        BaseCategory newItem = newCategories.get(newItemPosition);

        // Check if all the relevant content fields have the same values
        return oldItem.equals(newItem); // This relies on your BaseCategory's equals() implementation
    }
}
