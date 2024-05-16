package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.tdc.doan_d2.model.category.CategoryResponse;
import vn.edu.tdc.doan_d2.model.cuisine.CuisineResponse;

public interface RecipeCategoryApiService {
    @GET("categories")
    Call<CategoryResponse> getRecipeCategory(@Query("rapidapi-key") String key);

}