package vn.edu.tdc.doan_d2.model.responsive;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.Categories;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryResponse;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.model.cuisine.CuisineResponse;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisines;
import vn.edu.tdc.doan_d2.serviceapi.RecipeCategoryApiService;
import vn.edu.tdc.doan_d2.serviceapi.RecipeCuisineApiService;
import vn.edu.tdc.doan_d2.serviceapi.RetrofitInstance;



public class RecipeResponsiveRetrofit {
    private final MutableLiveData<ArrayList<String>> dataMutableLiveDataRetrofit = new MutableLiveData<ArrayList<String>>();
    private Categories categories;
    private Cuisines cuisines;
    private ArrayList<String> data = new ArrayList<>();
    private final Application application;
    private static final String PREF_RETROFIT_RUN_COUNT = "retrofit_run_count";


    public RecipeResponsiveRetrofit(Application application) {
        this.application = application;
    }

    public MutableLiveData<ArrayList<String>> getDataMutableLiveDataRetrofit(boolean isCategory) {
        int runCount = getRetrofitRunCount();

        if (runCount < 10) { // Kiểm tra số lần chạy
            incrementRetrofitRunCount(); // Tăng biến đếm
            if (isCategory) {
                RecipeCategoryApiService recipeCategoryApiService = RetrofitInstance.getServiceCategory();
                Call<CategoryResponse> call = recipeCategoryApiService.getRecipeCategory(application.getApplicationContext().getString(R.string.api_key));
                call.enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        CategoryResponse recipeCategory = response.body();
                        if (recipeCategory != null && recipeCategory.getCategories() != null) {
                            categories = recipeCategory.getCategories();
                            data = (ArrayList<String>) categories.getData();
                            dataMutableLiveDataRetrofit.setValue(data);
                            Log.d("API_Response", "Data: " + data.toString());
                            // Duyệt qua danh sách tên category lấy từ Retrofit
                            for (String categoryName : categories.getData()) {
                                Category category = new Category();
                                String name = categoryName;
                                if (categoryName != null) {
                                    name = name.toLowerCase().replace("!", "");
                                    category.setName(name);
                                } else {
                                    category.setName("Loading");
                                    category.setImgUrl(uploadImageToFirebaseStorage("Loading" + ".jpg"));
                                }
                                category.setImgUrl(uploadImageToFirebaseStorage(name));
                                saveCategoryToFirebase(category, isCategory);
                            }
                        } else {
                            Log.e("API_Response", "Failed to get data from API. Error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {

                    }
                });
            } else {
                RecipeCuisineApiService recipeCuisineApiService = RetrofitInstance.getServiceCuisine();
                Call<CuisineResponse> call = recipeCuisineApiService.getRecipeCuisine(application.getApplicationContext().getString(R.string.api_key));
                call.enqueue(new Callback<CuisineResponse>() {
                    @Override
                    public void onResponse(Call<CuisineResponse> call, Response<CuisineResponse> response) {
                        CuisineResponse cuisineResponse = response.body();
                        if (cuisineResponse != null && cuisineResponse.getCuisines() != null) {
                            cuisines = cuisineResponse.getCuisines();
                            data = (ArrayList<String>) cuisines.getData();
                            dataMutableLiveDataRetrofit.setValue(data);
                            Log.d("API_Response", "Data: " + data.toString());
                            // Duyệt qua danh sách tên category lấy từ Retrofit
                            for (String cuisineName : cuisines.getData()) {
                                Cuisine cuisine = new Cuisine();
                                String name = cuisineName;

                                if (cuisineName != null) {
                                    name = name.toLowerCase().replace("!", "");
                                    cuisine.setName(name);
                                } else {
                                    cuisine.setName("Loading");
                                    cuisine.setImgUrl(uploadImageToFirebaseStorage("Loading" + ".jpg"));
                                }
                                cuisine.setImgUrl(uploadImageToFirebaseStorage(name));
                                saveCategoryToFirebase(cuisine, isCategory);
                            }
                        } else {
                            Log.e("API_Response", "Failed to get data from API. Error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<CuisineResponse> call, Throwable t) {

                    }

                });
            }
        }
        return dataMutableLiveDataRetrofit;
    }


    public MutableLiveData<ArrayList<Meal>> getDataMutableLiveDataRetrofit(String nameCategory) {
        int runCount = getRetrofitRunCount();
//        if (runCount < 1) { // Kiểm tra số lần chạy
//            incrementRetrofitRunCount(); // Tăng biến đếm
            MealApiService` recipeMealApiService = RetrofitInstance.getServiceMeal();
            Call<MealResponse> call = recipeMealApiService.getRecipeMeal(nameCategory,application.getApplicationContext().getString(R.string.api_key2));
            Log.d("Hellowrold",call.request()+"");
            call.enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                    MealResponse recipeCategory = response.body();
                    if (recipeCategory != null && recipeCategory.getMeals() != null) {
                        meals = recipeCategory.getMeals();
                        dataMeal = (ArrayList<Meal>) meals.getData();
                        dataMealLiveDataRetrofit.postValue(dataMeal);
                        // Duyệt qua danh sách tên category lấy từ Retrofit
                        for (Meal meal : meals.getData()) {
                            meal.setImgUrl(uploadImageToFirebaseStorage(meal.getName()));
                            saveMealToFirebase(meal,nameCategory);
                        }
                    } else {
                        Log.e("API_Response", "Failed to get data from API. Error code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<MealResponse> call, Throwable t) {

                }
            });
//        }
        return dataMealLiveDataRetrofit;
    }

    private void saveCategoryToFirebase(BaseCategory category, boolean isCategory) {

        if (isCategory) {
            DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Category/categories");
            // Tạo key tự động cho mỗi category
            if (category.getName() != null && category.getName() != "") {
                String key = category.getName().toLowerCase().replace(" ", "_");
                categoriesRef.child(key).setValue(category);
            } else {
                String key = "Loading";
                categoriesRef.child(key).setValue(category);
            }
        }else {
            DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Category/cuisines");
            // Tạo key tự động cho mỗi category
            if (category.getName() != null && category.getName() != "") {
                String key = category.getName().toLowerCase().replace(" ", "_");
                categoriesRef.child(key).setValue(category);
            } else {
                String key = "Loading";
                categoriesRef.child(key).setValue(category);
            }
        }

    }

    private String uploadImageToFirebaseStorage(String categoryName) {
        return categoryName + ".jpg";
    }
    private int getRetrofitRunCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        return prefs.getInt(PREF_RETROFIT_RUN_COUNT, 0); // Lấy số lần chạy, mặc định là 0
    }

    private void incrementRetrofitRunCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        int runCount = prefs.getInt(PREF_RETROFIT_RUN_COUNT, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_RETROFIT_RUN_COUNT, runCount + 1);
        editor.apply();
    }

    public static void resetRetrofitRunCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_RETROFIT_RUN_COUNT, 0);
        editor.apply();
    }

}
