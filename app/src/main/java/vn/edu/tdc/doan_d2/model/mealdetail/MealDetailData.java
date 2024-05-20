package vn.edu.tdc.doan_d2.model.mealdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealDetailData {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Time")
    @Expose
    private List<String> time;
    @SerializedName("Ingredients")
    @Expose
    private List<String> ingredients;
    @SerializedName("Directions")
    @Expose
    private List<String> directions;
    @SerializedName("Nutritions")
    @Expose
    private List<String> nutritions;
    @SerializedName("Rating")
    @Expose
    private float rating;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Cuisine")
    @Expose
    private String cuisine;
    private String imgUrl;
    private String timeTotal;

    public MealDetailData(String name, String description, List<String> time, List<String> ingredients, List<String> directions, List<String> nutritions, float rating, String category, String cuisine, String imgUrl, String timeTotal) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.ingredients = ingredients;
        this.directions = directions;
        this.nutritions = nutritions;
        this.rating = rating;
        this.category = category;
        this.cuisine = cuisine;
        this.imgUrl = imgUrl;
        this.timeTotal = timeTotal;
    }



    public MealDetailData(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public List<String> getNutritions() {
        return nutritions;
    }

    public void setNutritions(List<String> nutritions) {
        this.nutritions = nutritions;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(String timeTotal) {
        this.timeTotal = timeTotal;
    }
}
