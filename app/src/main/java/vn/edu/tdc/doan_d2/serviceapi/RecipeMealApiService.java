package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.tdc.doan_d2.model.cuisine.CuisineResponse;

public interface RecipeMealApiService {
    @GET("categories/{categoryName}")
    Call<CuisineResponse> getRecipeCuisine(@Path("categoryName") String categoryName, @Query("rapidapi-key") String key);
}
