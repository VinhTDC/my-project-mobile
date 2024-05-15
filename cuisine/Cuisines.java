package vn.edu.tdc.doan_d2.model.cuisine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cuisines {
    @SerializedName("data")
    @Expose
    private List<String> data;

    public Cuisines(List<String> data) {
      this.data = data;
    }
    public Cuisines() {

    }

    // Getters and setters

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
