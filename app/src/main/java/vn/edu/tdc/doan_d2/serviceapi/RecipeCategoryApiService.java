package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import vn.edu.tdc.doan_d2.model.category.RecipeCategory;

public interface RecipeCategoryApiService {
    @GET("categories")
    Call<RecipeCategory> getRecipeCategory(@Query("rapidapi-key") String key);
}
//public  interface RecipeApiService{
//
//}
