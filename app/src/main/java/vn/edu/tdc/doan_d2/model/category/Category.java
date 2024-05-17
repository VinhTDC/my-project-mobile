package vn.edu.tdc.doan_d2.model.category;

import java.util.Objects;

import vn.edu.tdc.doan_d2.model.BaseCategory;

public class Category implements BaseCategory {
    private Integer id;
    private String name;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public Category(Integer id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }
    public Category( String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }


    public Category() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId()) && Objects.equals(getName(), category.getName()) && Objects.equals(getImgUrl(), category.getImgUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getImgUrl());
    }
}
