package vn.edu.tdc.doan_d2.model.meal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meals {
    @SerializedName("data")
    @Expose
    private List<Meal> data;

    public List<Meal> getData() {
        return data;
    }

    public void setData(List<Meal> data) {
        this.data = data;
    }
}
