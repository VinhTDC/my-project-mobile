package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.edu.tdc.doan_d2.model.cuisine.CuisineResponse;

public interface RecipeCuisineApiService {
    @GET("cuisines")
    Call<CuisineResponse> getRecipeCuisine(@Query("rapidapi-key") String key);

}
