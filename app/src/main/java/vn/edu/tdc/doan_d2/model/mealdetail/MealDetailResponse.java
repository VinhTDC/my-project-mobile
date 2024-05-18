package vn.edu.tdc.doan_d2.model.mealdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MealDetailResponse {
    @SerializedName("recipe")
    @Expose
    private Recipe recipe;
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
