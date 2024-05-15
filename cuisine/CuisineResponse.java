package vn.edu.tdc.doan_d2.model.cuisine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.tdc.doan_d2.model.category.Categories;

public class CuisineResponse {
    @SerializedName("cuisines")
    @Expose
    private Cuisines cuisines;
    public Cuisines getCuisines() {
        return cuisines;
    }
    public void setCuisines(Cuisines cuisines) {
        this.cuisines = cuisines;
    }
}
