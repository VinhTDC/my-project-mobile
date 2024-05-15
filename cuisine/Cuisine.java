package vn.edu.tdc.doan_d2.model.cuisine;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class Cuisine implements BaseCategory {
    private String id;
    private String name;
    private String imgUrl;

    public Cuisine(String idCuisine, String nameCuisine, String imgUrlCuisine) {
        this.id = idCuisine;
        this.name = nameCuisine;
        this.imgUrl = imgUrlCuisine;
    }
    public Cuisine() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
