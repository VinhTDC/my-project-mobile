package vn.edu.tdc.doan_d2.model.category;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class CategoryDiffCallback extends DiffUtil.Callback{
    private final ArrayList<Category> oldCategories;
    private final ArrayList<Category> newCategories;

    public CategoryDiffCallback(ArrayList<Category> oldCategories, ArrayList<Category> newCategories) {
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
        Category oldCategory = oldCategories.get(oldItemPosition);
        Category newCategory = newCategories.get(newItemPosition);
        return oldCategory != null && newCategory != null && oldCategory.getId() == newCategory.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Category oldCategory = oldCategories.get(oldItemPosition);
        Category newCategory = newCategories.get(newItemPosition);
        // So sánh các thuộc tính quan trọng (ví dụ: tên, mô tả, hình ảnh)
        return oldCategory.getName().equals(newCategory.getName()) &&
                oldCategory.getImgUrl().equals(newCategory.getImgUrl());
    }
}
