package vn.edu.tdc.doan_d2.model.meal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class Meal implements BaseCategory {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    private String imgUrl;

    public Meal(Integer id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
