package vn.edu.tdc.doan_d2.model.mealdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("data")
    @Expose
    private MealDetailDataT data;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MealDetailDataT getData() {
        return data;
    }

    public void setData(MealDetailDataT data) {
        this.data = data;
    }
}
