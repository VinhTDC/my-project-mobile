package vn.edu.tdc.doan_d2.model.responsive.category;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public interface CategoryDataSource {
    MutableLiveData<ArrayList<BaseCategory>> getAllCategories(boolean isCategory);

   void loadCategoriesFromFirebase(boolean isCategory);

    DatabaseReference getCategoriesFromFirebase();
    DatabaseReference getMealsFromFirebase();

    void loadMealFromFirebase(String nameCategoryString);
}
