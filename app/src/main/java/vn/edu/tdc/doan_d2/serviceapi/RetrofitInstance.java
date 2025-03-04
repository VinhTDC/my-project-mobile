package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitInstance {

    private static Retrofit retrofit = null;
    public static String BASE_URL = "https://all-in-one-recipe-api.p.rapidapi.com/";

    public static RecipeCategoryApiService getServiceCategory() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RecipeCategoryApiService.class);
    }
    public static RecipeCuisineApiService getServiceCuisine() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RecipeCuisineApiService.class);
    }
    public static MealApiService getServiceMeal() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MealApiService.class);
    }
    public static MealDetailService getServiceMealDetail(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MealDetailService.class);
    }

}
