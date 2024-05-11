package vn.edu.tdc.doan_d2.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Categories {
    @SerializedName("data")
    @Expose
    private List<String> data;

    // Getters and setters
    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}


