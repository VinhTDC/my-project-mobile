package vn.edu.tdc.doan_d2.viewmodel.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.responsive.RecipeResponsiveRetrofit;


public class CategoryViewModelRetrofit extends AndroidViewModel {
    private RecipeResponsiveRetrofit recipeCategoryResponsiveRetrofit;

    public CategoryViewModelRetrofit(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsiveRetrofit = new RecipeResponsiveRetrofit(application);
    }
    //Live Data

    public MutableLiveData<ArrayList<String>> getAllCategoryRetrofit(String key){
        return recipeCategoryResponsiveRetrofit.getDataMutableLiveDataRetrofit(key);
    }
}
