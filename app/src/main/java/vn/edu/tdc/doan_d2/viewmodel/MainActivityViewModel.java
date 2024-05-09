package vn.edu.tdc.doan_d2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;


import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.responsive.CategoryRecipeResponsive;


public class MainActivityViewModel extends AndroidViewModel {
    private CategoryRecipeResponsive recipeCategoryResponsive;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsive = new CategoryRecipeResponsive(application);
    }
    //Live Data
    public MutableLiveData<ArrayList<Category>> getAllCategory(){
        return recipeCategoryResponsive.allCategory();
    }
}
