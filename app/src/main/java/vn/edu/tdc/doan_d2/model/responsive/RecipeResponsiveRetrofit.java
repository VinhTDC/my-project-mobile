package vn.edu.tdc.doan_d2.model.responsive;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.tdc.doan_d2.R;
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

    public RecipeResponsiveRetrofit(Application application) {
        this.application = application;
    }

    public MutableLiveData<ArrayList<String>> getDataMutableLiveDataRetrofit(boolean isCategory) {

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
                            String name  = categoryName.toLowerCase().replace("!","");
                            if ( categoryName != null) {
                                category.setName(name);
                            } else {
                                category.setName("Loading");
                                category.setImgUrl(uploadImageToFirebaseStorage("Loading" + ".jpg"));
                            }
                            // Giả sử tạm thời chưa có đường dẫn ảnh
                            category.setImgUrl(uploadImageToFirebaseStorage(name));
                            // Lưu category vào Firebase
                            saveCategoryToFirebase(category);
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
                            String name = cuisineName.toLowerCase().replace("!","");
                            if ( cuisineName != null && cuisineName != "") {
                                cuisine.setName(name);
                                cuisine.setImgUrl(uploadImageToFirebaseStorage(name));
                            } else {
                                cuisine.setName("Loading");
                                cuisine.setImgUrl(uploadImageToFirebaseStorage("Loading" + ".jpg"));
                            }

                            // Giả sử tạm thời chưa có đường dẫn ảnh

                            // Lưu category vào Firebase
                            saveCategoryToFirebase(cuisine);
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

        return dataMutableLiveDataRetrofit;
    }

    private void saveCategoryToFirebase(Category category) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("categories");

        // Tạo key tự động cho mỗi category
        if ( category.getName() != null && category.getName() != "") {
            String key = category.getName().toLowerCase().replace(" ", "_");
            categoriesRef.child(key).setValue(category);
        } else {
            String key = "Loading";
            categoriesRef.child(key).setValue(category);
        }
    }

    private void saveCategoryToFirebase(Cuisine cuisine) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("cuisines");

        // Tạo key tự động cho mỗi category
        if ( cuisine.getName() != null && cuisine.getName() != "") {
            String key = cuisine.getName().toLowerCase().replace(" ", "_").trim();
            categoriesRef.child(key).setValue(cuisine);
        } else {
            String key = "Loading";
            categoriesRef.child(key).setValue(cuisine);
        }

    }

    private String uploadImageToFirebaseStorage(String categoryName) {

        return categoryName + ".jpg";
    }
}
