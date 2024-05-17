package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import vn.edu.tdc.doan_d2.model.meal.MealResponse;

public interface MealApiService {
    @GET("categories/{nameCategory}")
    Call<MealResponse> getRecipeMeal(@Path("nameCategory") String nameCategory, @Query("rapidapi-key") String key);
}
