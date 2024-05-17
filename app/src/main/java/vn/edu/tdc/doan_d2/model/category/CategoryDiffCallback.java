package vn.edu.tdc.doan_d2.model.category;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class CategoryDiffCallback extends DiffUtil.Callback{
    private  ArrayList<BaseCategory> oldCategories;
    private  ArrayList<BaseCategory> newCategories;

    public CategoryDiffCallback(ArrayList<BaseCategory> oldCategories, ArrayList<BaseCategory> newCategories) {
        this.oldCategories = oldCategories;
        this.newCategories = newCategories;
    }
    @Override
    public int getOldListSize() {
        return oldCategories.size();
    }

    @Override
    public int getNewListSize() {
        return newCategories.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        BaseCategory oldItem = oldCategories.get(oldItemPosition);
        BaseCategory newItem = newCategories.get(newItemPosition);

        // Kiểm tra xem 2 item có cùng ID không
        return oldItem.getName().equals(newItem.getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseCategory oldItem = oldCategories.get(oldItemPosition);
        BaseCategory newItem = newCategories.get(newItemPosition);
        // So sánh các thuộc tính của BaseCategory
        // Chú ý: Bạn cần đảm bảo rằng các lớp Category và Cuisine đều override phương thức equals()
        return oldItem.equals(newItem);
    }
}
