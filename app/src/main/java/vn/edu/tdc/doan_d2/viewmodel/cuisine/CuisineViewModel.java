package vn.edu.tdc.doan_d2.viewmodel.cuisine;

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
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.model.responsive.cuisine.CuisineFilter;
import vn.edu.tdc.doan_d2.model.responsive.cuisine.CuisineRecipeResponsive;
import vn.edu.tdc.doan_d2.model.responsive.cuisine.CuisineUtils;

public class CuisineViewModel extends AndroidViewModel {
    private final CuisineRecipeResponsive cuisineRecipeResponsive;
    private final MutableLiveData<Integer> cuisinesCountLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<ArrayList<Cuisine>> filteredCuisinesLiveData = new MutableLiveData<>();
    private CuisineUtils cuisineUtils;
    private CuisineFilter cuisineFilter;
    public CuisineViewModel(@NonNull Application application) {
        super(application);
        this.cuisineRecipeResponsive = new CuisineRecipeResponsive(application, this);
        this.cuisineFilter = new CuisineFilter(this,cuisineRecipeResponsive );
    }
    public MutableLiveData<ArrayList<Cuisine>> getAllCuisine() {
        return cuisineRecipeResponsive.getAllCuisine();
    }
    public MutableLiveData<ArrayList<Cuisine>> loadCuisineForPage(int page, int pageSize, LifecycleOwner lifecycleOwner) {
        MutableLiveData<ArrayList<Cuisine>> pageData = new MutableLiveData<>();
        cuisineFilter = new CuisineFilter(this, cuisineRecipeResponsive);
        cuisineRecipeResponsive.getAllCuisine().observe( lifecycleOwner, allCuisines -> {
            if (allCuisines != null) { // Kiểm tra null
                int startIndex = page * pageSize;
                int endIndex = Math.min(startIndex + pageSize, allCuisines.size());
                cuisinesCountLiveData.setValue(allCuisines.size());
                cuisineUtils.getCuisineByRange(startIndex, endIndex, new CuisineUtils.CuisineResponse() {
                    @Override
                    public void onSuccess(ArrayList<Cuisine> cuisines) {
                        pageData.setValue(cuisines);
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
        return cuisinesCountLiveData;
    }

    private MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();
    public LiveData<ArrayList<Cuisine>> getFilteredCategoriesLiveData() {

        return filteredCuisinesLiveData;
    }


    public void setFilteredCategoriesLiveData(ArrayList<Cuisine> filteredCuisine) {
        filteredCuisinesLiveData.postValue(filteredCuisine);
    }
    public void updateSearchQuery(String query, int page, int pageSize, LifecycleOwner lifecycleOwner) {
        if (query == null || query.isEmpty()) {
            loadCuisineForPage(page, pageSize, lifecycleOwner).observe(lifecycleOwner, filteredCuisinesLiveData::setValue);
        } else {
            searchQueryLiveData.setValue(query);
            cuisineFilter.filterCategories(query).observe(lifecycleOwner, filteredCuisinesLiveData::setValue);
        }
    }
}
