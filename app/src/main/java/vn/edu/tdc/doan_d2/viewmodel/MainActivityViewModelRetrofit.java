package vn.edu.tdc.doan_d2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.responsive.CategoryRecipeResponsiveRetrofit;


public class MainActivityViewModelRetrofit extends AndroidViewModel {
    private CategoryRecipeResponsiveRetrofit recipeCategoryResponsiveRetrofit;

    public MainActivityViewModelRetrofit(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsiveRetrofit = new CategoryRecipeResponsiveRetrofit(application);
    }
    //Live Data

    public MutableLiveData<ArrayList<String>> getAllCategoryRetrofit(){
        return recipeCategoryResponsiveRetrofit.getDataMutableLiveDataRetrofit();
    }
}
