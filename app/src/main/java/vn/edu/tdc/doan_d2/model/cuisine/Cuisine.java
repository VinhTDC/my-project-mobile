package vn.edu.tdc.doan_d2.model.cuisine;

import java.util.Objects;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class Cuisine implements BaseCategory {
    private Integer id;
    private String name;
    private String imgUrl;

    public Cuisine(Integer idCuisine, String nameCuisine, String imgUrlCuisine) {
        this.id = idCuisine;
        this.name = nameCuisine;
        this.imgUrl = imgUrlCuisine;
    }
    public Cuisine( String nameCuisine, String imgUrlCuisine) {
        this.name = nameCuisine;
        this.imgUrl = imgUrlCuisine;
    }
    public Cuisine() {
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }

    @Override
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cuisine)) return false;
        Cuisine cuisine = (Cuisine) o;
        return Objects.equals(getId(), cuisine.getId()) && Objects.equals(getName(), cuisine.getName()) && Objects.equals(getImgUrl(), cuisine.getImgUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getImgUrl());
    }
}
