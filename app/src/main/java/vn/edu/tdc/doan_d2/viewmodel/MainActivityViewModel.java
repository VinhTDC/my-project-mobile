package vn.edu.tdc.doan_d2.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Objects;


import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.responsive.CategoryRecipeResponsive;


public class MainActivityViewModel extends AndroidViewModel {
    private final CategoryRecipeResponsive recipeCategoryResponsive;
    private MutableLiveData<Integer> categoriesCountLiveData = new MutableLiveData<>(0);

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsive = new CategoryRecipeResponsive(application);
    }

    //Live Data
    public MutableLiveData<ArrayList<Category>> getAllCategory() {
        Log.d("testgetAllCategory", recipeCategoryResponsive.getAllCategory() + "");
        return recipeCategoryResponsive.getAllCategory();

    }

    public MutableLiveData<ArrayList<Category>> loadCategoriesForPage(int page, int pageSize) {
        MutableLiveData<ArrayList<Category>> pageData = new MutableLiveData<>();
        recipeCategoryResponsive.getAllCategory().observeForever(allCategories -> {


            if (allCategories != null) { // Kiểm tra null
                int startIndex = page * pageSize;
                int endIndex = Math.min(startIndex + pageSize, allCategories.size());
                categoriesCountLiveData.setValue(allCategories.size());
                ArrayList<Category> pageCategories = recipeCategoryResponsive.getCategoriesByRange(startIndex, endIndex);
                pageData.setValue(pageCategories);
            } else {

                pageData.setValue(new ArrayList<>());
            }
        });
        return pageData;
    }
    public LiveData<Integer> getCategoriesCountLiveData() {
        return categoriesCountLiveData;
    }


}
