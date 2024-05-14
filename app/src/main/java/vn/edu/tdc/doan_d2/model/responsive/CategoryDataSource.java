package vn.edu.tdc.doan_d2.model.responsive;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.category.Category;

public interface CategoryDataSource {
    MutableLiveData<ArrayList<Category>> getAllCategories();

   void loadCategoriesFromFirebase();

    DatabaseReference getCategoriesFromFirebase();
}
