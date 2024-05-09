package vn.edu.tdc.doan_d2.model.responsive;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.model.category.Categories;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryResponse;
import vn.edu.tdc.doan_d2.serviceapi.RecipeCategoryApiService;
import vn.edu.tdc.doan_d2.serviceapi.RetrofitInstance;

public class  CategoryRecipeResponsiveRetrofit {
    private final MutableLiveData<ArrayList<String>> dataMutableLiveDataRetrofit = new MutableLiveData<ArrayList<String>>();
    private Categories categories = new Categories();

    private ArrayList<String> data = new ArrayList<>();

    private final Application application;

    public CategoryRecipeResponsiveRetrofit(Application application) {
        this.application = application;
    }

    public MutableLiveData<ArrayList<String>> getDataMutableLiveDataRetrofit() {
        RecipeCategoryApiService recipeApiService = RetrofitInstance.getService();
        Call<CategoryResponse> call = recipeApiService.getRecipeCategory(application.getApplicationContext().getString(R.string.api_key));
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
                        category.setName(categoryName);
                        // Giả sử tạm thời chưa có đường dẫn ảnh
                        category.setImgUrl(uploadImageToFirebaseStorage(categoryName));
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
        return dataMutableLiveDataRetrofit;
    }
    private void saveCategoryToFirebase(Category category) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("categories");

        // Tạo key tự động cho mỗi category
        String key = category.getName().toLowerCase().replace(" ", "_");
        categoriesRef.child(key).setValue(category);
    }
    private String uploadImageToFirebaseStorage(String categoryName) {
        return  categoryName + ".jpg";
    }

}
