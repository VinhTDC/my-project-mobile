package vn.edu.tdc.doan_d2.model.comment;

import java.util.Objects;

public class Comment {
    private String idMeal;
    private String comment;
    private String userName;
    private  float rating;



    public Comment(String comment, String userName, float rating) {
        this.comment = comment;
        this.userName = userName;
        this.rating = rating;
    }

    public Comment(String idMeal, String comment, String userName, float rating) {
        this.idMeal = idMeal;
        this.comment = comment;
        this.userName = userName;
        this.rating = rating;
    }
    public Comment() {

    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment1 = (Comment) o;
        return Float.compare(getRating(), comment1.getRating()) == 0 && Objects.equals(getIdMeal(), comment1.getIdMeal()) && Objects.equals(getComment(), comment1.getComment()) && Objects.equals(getUserName(), comment1.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdMeal(), getComment(), getUserName(), getRating());
    }
}
