package vn.edu.tdc.doan_d2.model.responsive;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;

public class CategoryFilter {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private CategoryRecipeResponsive responsive;
    private MainActivityViewModel viewModel;

    public MutableLiveData<ArrayList<Category>> filterCategories(String query) {
        MutableLiveData<ArrayList<Category>> filteredLiveData = new MutableLiveData<ArrayList<Category>>();
        isLoading.setValue(true); // Bắt đầu quá trình tải
        if (!query.isEmpty()) {
            Query queryRef = responsive.getCategoriesFromFirebase().orderByChild("name")
                    .startAt(query.trim())
                    .endAt(query.trim() + "\uf8ff"); //
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Category> filteredCategories = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        Log.d("Firebase", "Data snapshot exists");
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            String id = "";
                            if (categorySnapshot.child("id").exists()) {
                                id = categorySnapshot.child("id").getValue(String.class);
                            }
                            String name = "";
                            if (categorySnapshot.child("name").exists()) {
                                name = categorySnapshot.child("name").getValue(String.class);
                            }
                            String imageUrl = "";
                            if (categorySnapshot.child("imgUrl").exists()) {
                                imageUrl = categorySnapshot.child("imgUrl").getValue(String.class);
                            }
                            Category category = new Category(id, name, imageUrl);
                            filteredCategories.add(category);
                            Log.d("Con cặc", dataSnapshot.exists() + "" + filteredCategories.size() + "");
                        }
                        filteredLiveData.postValue(filteredCategories);

                        isLoading.setValue(false);
                        Log.d("query", filteredCategories.size() + "");
                    } else {
                        Log.d("Con cặc", dataSnapshot.exists() + "" + filteredCategories.size() + "");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi
                    filteredLiveData.postValue(null);
                    isLoading.setValue(false);
                }
            });
        }else {

        }

        return filteredLiveData;
    }

    public CategoryFilter(MainActivityViewModel viewModel, CategoryRecipeResponsive responsive) {
        this.responsive = responsive;
        this.viewModel = viewModel;
    }
}
