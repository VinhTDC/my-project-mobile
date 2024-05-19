package vn.edu.tdc.doan_d2.model.mealdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("data")
    @Expose
    private MealDetailData data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MealDetailData getData() {
        return data;
    }

    public void setData(MealDetailData data) {
        this.data = data;
    }
}
