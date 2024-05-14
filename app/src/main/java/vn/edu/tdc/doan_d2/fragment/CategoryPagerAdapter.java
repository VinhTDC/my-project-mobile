package vn.edu.tdc.doan_d2.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;

public class CategoryPagerAdapter extends FragmentStateAdapter {
    private final MainActivityViewModel viewModel; // Truyền viewModel từ Activity
    private final int pageCount; // Số lượng trang

    public CategoryPagerAdapter(@NonNull FragmentActivity fragmentActivity, int pageCount, MainActivityViewModel viewModel) {
        super(fragmentActivity);
        this.pageCount = pageCount;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Tạo một CategoryFragment mới cho mỗi trang
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setViewModel(viewModel);

        // Truyền vị trí trang vào CategoryFragment
        Bundle args = new Bundle();
        args.putInt("page", position);
        categoryFragment.setArguments(args);

        return categoryFragment;
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }
}
