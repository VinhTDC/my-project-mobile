package vn.edu.tdc.doan_d2.model.responsive.category;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.Category;

import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.model.meal.Meal;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;


public class CategoryFilter {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private CategoryRecipeResponsive responsive;
    private CategoryViewModel viewModel;

    public MutableLiveData<ArrayList<BaseCategory>> filterCategories(String query, boolean isCategory) {
        MutableLiveData<ArrayList<BaseCategory>> filteredLiveData = new MutableLiveData<>();
        isLoading.setValue(true); // Bắt đầu quá trình tải
        if (!query.isEmpty()) {
            DatabaseReference reference = responsive.getCategoriesFromFirebase();
            reference = isCategory ? reference.child("categories") : reference.child("cuisine");
            Query queryRef = reference.orderByChild("name")
                    .startAt(query.trim().toLowerCase())
                    .endAt(query.trim().toLowerCase() + "\uf8ff"); //
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<BaseCategory> filteredCategories = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            BaseCategory baseCategory;
                            if (isCategory) {
                                baseCategory = categorySnapshot.getValue(Category.class);

                            } else {
                                baseCategory = categorySnapshot.getValue(Cuisine.class);
                            }
                            if (baseCategory != null) {
                                filteredCategories.add(baseCategory);
                            }
                        }

                        filteredLiveData.postValue(filteredCategories);
                        isLoading.setValue(false);
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi
                    filteredLiveData.postValue(null);
                    isLoading.setValue(false);
                }
            });
        } else {

        }
        Log.d("baseCategory", filteredLiveData.getValue() + "");
        return filteredLiveData;
    }

    public MutableLiveData<ArrayList<BaseMeal>> filterMeal(String query) {
        MutableLiveData<ArrayList<BaseMeal>> filteredLiveData = new MutableLiveData<>();
        isLoading.setValue(true); // Bắt đầu quá trình tải
        if (!query.isEmpty()) {
            DatabaseReference reference = responsive.getMealsFromFirebase();
            Query queryRef = reference.orderByChild("name")
                    .startAt(query.trim().toLowerCase())
                    .endAt(query.trim().toLowerCase() + "\uf8ff"); //
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<BaseMeal> filteredCategories = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                            BaseMeal baseMeal;
                            baseMeal = mealSnapshot.getValue(Meal.class);
                            if (baseMeal != null) {
                                filteredCategories.add(baseMeal);
                            }
                        }
                        filteredLiveData.postValue(filteredCategories);
                        isLoading.setValue(false);
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi
                    filteredLiveData.postValue(null);
                    isLoading.setValue(false);
                }
            });
        } else {

        }
        Log.d("baseCategory", filteredLiveData.getValue() + "");
        return filteredLiveData;
    }

    public CategoryFilter(CategoryViewModel viewModel, CategoryRecipeResponsive responsive) {
        this.responsive = responsive;
        this.viewModel = viewModel;
    }
}
