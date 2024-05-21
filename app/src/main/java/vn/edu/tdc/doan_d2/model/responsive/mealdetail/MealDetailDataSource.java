package vn.edu.tdc.doan_d2.model.responsive.mealdetail;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;

public interface MealDetailDataSource {
    MutableLiveData<MealDetailData> getMealDetail(String idMeal);
    DatabaseReference getMealDetailFromFirebase();

    void loadMealDetailGeneralFromFirebase(String idMeal);


}
