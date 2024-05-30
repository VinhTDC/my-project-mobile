package vn.edu.tdc.doan_d2.viewmodel.category;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.meal.Meal;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailDataT;
import vn.edu.tdc.doan_d2.model.responsive.RecipeResponsiveRetrofit;


public class CategoryViewModelRetrofit extends AndroidViewModel {
    private RecipeResponsiveRetrofit recipeCategoryResponsiveRetrofit;
    private String nameCategory;

    public CategoryViewModelRetrofit(@NonNull Application application) {
        super(application);
        this.recipeCategoryResponsiveRetrofit = new RecipeResponsiveRetrofit(application);
    }
    //Live Data

    public MutableLiveData<ArrayList<String>> getAllCategoryRetrofit(boolean isCategory){
        return recipeCategoryResponsiveRetrofit.getDataMutableLiveDataRetrofit(isCategory);
    }
    public MutableLiveData<ArrayList<Meal>> getAllMealRetrofit(String nameCategory){
        return recipeCategoryResponsiveRetrofit.getDataMutableLiveDataRetrofit(nameCategory);
    }
    public MutableLiveData<MealDetailDataT> getAllMealDetailRetrofit(String idMeal){

        return recipeCategoryResponsiveRetrofit.getDataMealDetailMutableLiveDataRetrofit(idMeal);
    }
}
