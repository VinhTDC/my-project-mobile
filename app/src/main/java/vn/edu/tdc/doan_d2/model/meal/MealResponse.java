package vn.edu.tdc.doan_d2.model.meal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MealResponse {
    @SerializedName("categories")
    @Expose
    private Meals meals;
    public Meals getMeals() {
        return meals;
    }

    public void setMeals(Meals meals) {
        this.meals = meals;
    }
}
