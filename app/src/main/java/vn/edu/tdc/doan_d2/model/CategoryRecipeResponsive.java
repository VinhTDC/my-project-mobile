package vn.edu.tdc.doan_d2.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.model.category.Categories;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.RecipeCategory;
import vn.edu.tdc.doan_d2.serviceapi.RecipeCategoryApiService;
import vn.edu.tdc.doan_d2.serviceapi.RetrofitInstance;

public class CategoryRecipeResponsive {
    private Categories categories = new Categories();

    private ArrayList<String> data = new ArrayList<>();

    private MutableLiveData<ArrayList<String>> dataMutableLiveData = new MutableLiveData<ArrayList<String>>();

    private Application application;

    public CategoryRecipeResponsive(Application application) {
        this.application = application;
    }

    public MutableLiveData<ArrayList<String>> getDataMutableLiveData() {
        RecipeCategoryApiService recipeApiService = RetrofitInstance.getService();
        Call<RecipeCategory> call =  recipeApiService.getRecipeCategory(application.getApplicationContext().getString(R.string.api_key));
        call.enqueue(new Callback<RecipeCategory>() {
            @Override
            public void onResponse(Call<RecipeCategory> call, Response<RecipeCategory> response) {
                RecipeCategory recipeCategory = response.body();
                if(recipeCategory != null && recipeCategory.getCategories() != null){
                    categories = recipeCategory.getCategories();
                    data = (ArrayList<String>) categories.getData();
                    dataMutableLiveData.setValue(data);
                    Log.d("API_Response", "Data: " + data.toString());
                    saveCategoriesToFirebase(categories.getData());
                }else {
                    Log.e("API_Response", "Failed to get data from API. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeCategory> call, Throwable t) {

            }

        });
        return dataMutableLiveData;
    }
    private void saveCategoriesToFirebase(List<String> categoryNames) {
        if (categoryNames != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("categories");
            for (String categoryName : categoryNames) {
                DatabaseReference categoryRef = databaseRef.push();
                String categoryId = categoryRef.getKey(); // Lấy id tự động từ Firebase

                // Tải lên ảnh và lấy đường dẫn ảnh từ Firebase Storage
                String imageUrl = uploadImageToFirebaseStorage(categoryName);

                // Tạo một đối tượng Category mới với id, tên và imageUrl
                Category newCategory = new Category(categoryId, categoryName, imageUrl);

                // Lưu đối tượng Category vào Firebase Realtime Database
                categoryRef.setValue(newCategory);
            }
        }
    }
    private String uploadImageToFirebaseStorage(String categoryName) {
        // Triển khai logic tải lên ảnh và lấy đường dẫn ảnh từ Firebase Storage tại đây
        // Ví dụ, bạn có thể tải lên ảnh với tên là "category_" + categoryName + ".jpg"
        // và trả về đường dẫn ảnh tương ứng

        // Đoạn mã này chỉ để minh họa, bạn cần tự triển khai logic tải lên ảnh và lấy đường dẫn
        return "https://example.com/images/category_" + categoryName + ".jpg";
    }

}
