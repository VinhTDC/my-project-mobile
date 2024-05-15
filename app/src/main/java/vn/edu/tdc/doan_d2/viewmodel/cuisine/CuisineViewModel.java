//package vn.edu.tdc.doan_d2.viewmodel.cuisine;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//import vn.edu.tdc.doan_d2.model.BaseCategory;
//
//import vn.edu.tdc.doan_d2.model.responsive.cuisine.CuisineFilter;
//import vn.edu.tdc.doan_d2.model.responsive.cuisine.CuisineRecipeResponsive;
//import vn.edu.tdc.doan_d2.model.responsive.cuisine.CuisineUtils;
//
//public class CuisineViewModel extends AndroidViewModel {
//    private final CuisineRecipeResponsive cuisineRecipeResponsive;
//    private final MutableLiveData<Integer> cuisinesCountLiveData = new MutableLiveData<>(0);
//    private final MutableLiveData<ArrayList<Cuisine>> filteredCuisinesLiveData = new MutableLiveData<>();
//    private CuisineUtils cuisineUtils;
//    private CuisineFilter cuisineFilter;
//    public CuisineViewModel(@NonNull Application application) {
//        super(application);
//        this.cuisineRecipeResponsive = new CuisineRecipeResponsive(application, this);
//        this.cuisineFilter = new CuisineFilter(this,cuisineRecipeResponsive );
//    }
//    public MutableLiveData<ArrayList<Cuisine>> getAllCuisine() {
//        return cuisineRecipeResponsive.getAllCuisine();
//    }
//    public MutableLiveData<ArrayList<BaseCategory>> loadCuisineForPage(int page, int pageSize, LifecycleOwner lifecycleOwner) {
//        MutableLiveData<ArrayList<BaseCategory>> pageData = new MutableLiveData<>();
//        cuisineRecipeResponsive.getAllCuisine().observe( lifecycleOwner, allCategories -> {
//            if (allCategories != null) { // Kiểm tra null
//                int startIndex = page * pageSize;
//                int endIndex = Math.min(startIndex + pageSize, allCategories.size());
//                cuisinesCountLiveData.setValue(allCategories.size());
//                List<Cuisine> categoriesForPage = allCategories.subList(startIndex,endIndex);
//                List<BaseCategory> baseCategoriesForPage = new ArrayList<>(categoriesForPage);
//                pageData.setValue(new ArrayList<>(baseCategoriesForPage));
//            } else {
//                pageData.setValue(new ArrayList<>());
//            }
//        });
//        return pageData;
//    }
//    public LiveData<Integer> getCategoriesCountLiveData() {
//        return cuisinesCountLiveData;
//    }
//
//    private MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();
//    public LiveData<ArrayList<Cuisine>> getFilteredCategoriesLiveData() {
//
//        return filteredCuisinesLiveData;
//    }
//
//
//    public void setFilteredCategoriesLiveData(ArrayList<Cuisine> filteredCuisine) {
//        filteredCuisinesLiveData.postValue(filteredCuisine);
//    }
////    public void updateSearchQuery(String query, int page, int pageSize, LifecycleOwner lifecycleOwner) {
////        if (query == null || query.isEmpty()) {
////            loadCuisineForPage(page, pageSize, lifecycleOwner).observe(lifecycleOwner, filteredCuisinesLiveData::setValue);
////        } else {
////            searchQueryLiveData.setValue(query);
////            cuisineFilter.filterCategories(query).observe(lifecycleOwner, filteredCuisinesLiveData::setValue);
////        }
////    }
//}
