package vn.edu.tdc.doan_d2.viewmodel.category;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.fragment.CategoryFragment;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryFilter;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryRecipeResponsive;


public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRecipeResponsive recipeCategoryResponsive;
    private final CategoryFilter categoryFilter;
    private final MutableLiveData<Integer> categoriesCountLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<BaseCategory>> filteredCategoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isCategory = new MutableLiveData<>(true);
    private MutableLiveData<String> nameCategory = new MutableLiveData<>();

    private CategoryFragment fragment;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsive = new CategoryRecipeResponsive(application, this);
        this.categoryFilter = new CategoryFilter(this, recipeCategoryResponsive);
    }

    //Live Data

    public MutableLiveData<ArrayList<BaseCategory>> loadCategoriesForPage(int page, int pageSize, LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<ArrayList<BaseCategory>> pageData = new MutableLiveData<>();
        isCategory.observe(lifecycleOwner, isCategory -> {
            recipeCategoryResponsive.getAllCategories(isCategory).observe(lifecycleOwner, allCategories -> {
                List<BaseCategory> categoriesForPage = new ArrayList<>();
                if (allCategories != null) { // Kiểm tra null
                    int startIndex = page * pageSize;
                    int endIndex = Math.min(startIndex + pageSize, allCategories.size());
                    if (startIndex >= endIndex) {
                        pageData.postValue(new ArrayList<>()); // Trả về danh sách rỗng cho pageData
                    } else {
                        categoriesCountLiveData.setValue(allCategories.size());
                         categoriesForPage = allCategories.subList(startIndex, endIndex);
                        pageData.postValue(new ArrayList<>(categoriesForPage));
                    }
                }
                pageData.postValue(new ArrayList<>(categoriesForPage)); // Cập nhật LiveData trên Main Thread
                isLoading.postValue(false); // Kết thúc tải
            });
        });
        return pageData;
    }
    public MutableLiveData<ArrayList<BaseCategory>> loadMealsForPage(int page, int pageSize, LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<ArrayList<BaseCategory>> pageData = new MutableLiveData<>();
        nameCategory.observe(lifecycleOwner, nameCategory ->{
            recipeCategoryResponsive.getAllMeals(nameCategory).observe(lifecycleOwner, allMeals -> {
                List<BaseCategory> mealsForPage = new ArrayList<>();
                if (allMeals != null) { // Kiểm tra null
                    Log.d("addtobac","called");
                    int startIndex = page * pageSize;
                    int endIndex = Math.min(startIndex + pageSize, allMeals.size());
                    if (startIndex >= endIndex) {
                        pageData.postValue(new ArrayList<>()); // Trả về danh sách rỗng cho pageData
                    } else {
                        categoriesCountLiveData.setValue(allMeals.size());
                        mealsForPage = allMeals.subList(startIndex, endIndex);
                        pageData.postValue(new ArrayList<>(mealsForPage));
                    }
                }
                pageData.postValue(new ArrayList<>(mealsForPage)); // Cập nhật LiveData trên Main Thread
                isLoading.postValue(false); // Kết thúc tải
            });

        });
        return pageData;
    }
    public LiveData<Integer> getCategoriesCountLiveData() {
        return categoriesCountLiveData;
    }


    public LiveData<ArrayList<BaseCategory>> getFilteredCategoriesLiveData() {
        return filteredCategoriesLiveData;
    }

    public void updateSearchQuery(String query, int page, int pageSize, LifecycleOwner lifecycleOwner) {
        if (query == null || query.isEmpty()) {
            loadCategoriesForPage(page, pageSize, lifecycleOwner).observe(lifecycleOwner, filteredCategoriesLiveData::setValue);
        } else {
            isCategory.observe(lifecycleOwner, isCategory ->
                    categoryFilter.filterCategories(query, isCategory) // Lọc theo isCategory
                            .observe(lifecycleOwner, filteredCategoriesLiveData::setValue)
            );

        }
    }

    public void setIsCategory(boolean key) {
        this.isCategory.postValue(key);
        recipeCategoryResponsive.resetCategories();
    }

    public MutableLiveData<String> getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory.setValue(nameCategory);
    }

    public LiveData<Boolean> getIsCategory() {
        return isCategory;
    }
}

