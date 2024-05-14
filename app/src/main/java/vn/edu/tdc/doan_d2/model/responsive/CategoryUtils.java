package vn.edu.tdc.doan_d2.model.responsive;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryResponse;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;

public class CategoryUtils {
    private CategoryRecipeResponsive responsive;
    private MainActivityViewModel viewModel;


    public CategoryUtils(MainActivityViewModel viewModel, CategoryRecipeResponsive responsive) {
        this.viewModel = viewModel;
        this.responsive = responsive;
    }

    public void getCategoriesByRange(int startIndex, int endIndex, CategoryResponse callback, LifecycleOwner lifecycleOwner) {

        responsive.getAllCategories().observe(lifecycleOwner, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                if (categories != null) {
                    ArrayList<Category> result = new ArrayList<>();
                    for (int i = startIndex; i < endIndex; i++) {
                        result.add(categories.get(i));
                    }
                    Log.d("startIndex", startIndex + "");
                    Log.d("startIndex", endIndex + " endIndex");
                    callback.onSuccess(result);
                } else {

                }
                responsive.getAllCategories().removeObserver(this);
            }
        });

    }

    public interface CategoryResponse {
        void onSuccess(ArrayList<Category> categories);

        void onError(String errorMessage);
    }

}
