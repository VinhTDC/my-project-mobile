package vn.edu.tdc.doan_d2.serviceapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {

    private static Retrofit retrofit = null;
    public static String BASE_URL = "https://all-in-one-recipe-api.p.rapidapi.com/";

    public static RecipeCategoryApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RecipeCategoryApiService.class);
    }
}
