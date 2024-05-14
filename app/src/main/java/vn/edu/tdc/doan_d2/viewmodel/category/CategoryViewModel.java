package vn.edu.tdc.doan_d2.viewmodel.category;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;


import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryFilter;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryRecipeResponsive;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryUtils;


public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRecipeResponsive recipeCategoryResponsive;
    private final CategoryFilter categoryFilter;
    private final MutableLiveData<Integer> categoriesCountLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<Category>> filteredCategoriesLiveData = new MutableLiveData<>();
    private CategoryUtils categoryUtils;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsive = new CategoryRecipeResponsive(application, this);
        this.categoryFilter = new CategoryFilter(this,recipeCategoryResponsive );
    }

    //Live Data
    public MutableLiveData<ArrayList<Category>> getAllCategory() {
        return recipeCategoryResponsive.getAllCategories();
    }

    public MutableLiveData<ArrayList<Category>> loadCategoriesForPage(int page, int pageSize,LifecycleOwner lifecycleOwner) {
        MutableLiveData<ArrayList<Category>> pageData = new MutableLiveData<>();
        categoryUtils = new CategoryUtils(this, recipeCategoryResponsive);
        recipeCategoryResponsive.getAllCategories().observe( lifecycleOwner, allCategories -> {
            if (allCategories != null) { // Kiểm tra null
                int startIndex = page * pageSize;
                int endIndex = Math.min(startIndex + pageSize, allCategories.size());
                categoriesCountLiveData.setValue(allCategories.size());
                categoryUtils.getCategoriesByRange(startIndex, endIndex, new CategoryUtils.CategoryResponse() {
                    @Override
                    public void onSuccess(ArrayList<Category> categories) {
                        pageData.setValue(categories);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getApplication(), "Error loading", Toast.LENGTH_SHORT).show();
                    }
                }, lifecycleOwner);
            } else {
                pageData.setValue(new ArrayList<>());
            }
        });
        return pageData;
    }

    public LiveData<Integer> getCategoriesCountLiveData() {
        return categoriesCountLiveData;
    }

    private MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();

//    public void updateSearchQuery(String query, int page, int pageSize, LifecycleOwner lifecycleOwner) {
//
//    }

    public LiveData<ArrayList<Category>> getFilteredCategoriesLiveData() {
        Log.d("filteredCategoriesLiveData", filteredCategoriesLiveData.getValue() + "");
        return filteredCategoriesLiveData;
    }


    public void setFilteredCategoriesLiveData(ArrayList<Category> filteredCategories) {
        filteredCategoriesLiveData.postValue(filteredCategories);
    }

    public void updateSearchQuery(String query, int page, int pageSize, LifecycleOwner lifecycleOwner) {
        if (query == null || query.isEmpty()) {
            loadCategoriesForPage(page, pageSize, lifecycleOwner).observe(lifecycleOwner, filteredCategoriesLiveData::setValue);
        } else {
            searchQueryLiveData.setValue(query);
            categoryFilter.filterCategories(query).observe(lifecycleOwner, filteredCategoriesLiveData::setValue);
        }
    }
}

