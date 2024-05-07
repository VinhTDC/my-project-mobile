package vn.edu.tdc.doan_d2.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.model.category.Categories;
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
}
