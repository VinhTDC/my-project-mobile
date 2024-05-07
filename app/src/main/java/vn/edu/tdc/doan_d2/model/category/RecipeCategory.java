package vn.edu.tdc.doan_d2.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeCategory {

    @SerializedName("categories")
    @Expose
    private Categories categories;

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }
}
