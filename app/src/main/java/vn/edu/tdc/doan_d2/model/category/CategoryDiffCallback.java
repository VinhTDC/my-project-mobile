package vn.edu.tdc.doan_d2.model.category;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class CategoryDiffCallback extends DiffUtil.Callback{
    private final ArrayList<BaseCategory> oldCategories;
    private final ArrayList<BaseCategory> newCategories;

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
        BaseCategory oldCategory = oldCategories.get(oldItemPosition);
        BaseCategory newCategory = newCategories.get(newItemPosition);
        return oldCategory != null && newCategory != null && oldCategory.getId() == newCategory.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseCategory oldCategory = oldCategories.get(oldItemPosition);
        BaseCategory newCategory = newCategories.get(newItemPosition);
        // So sánh các thuộc tính quan trọng (ví dụ: tên, mô tả, hình ảnh)
        return oldCategory.getName().equals(newCategory.getName()) &&
                oldCategory.getImgUrl().equals(newCategory.getImgUrl());
    }
}
