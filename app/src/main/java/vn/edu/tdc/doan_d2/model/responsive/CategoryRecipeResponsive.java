package vn.edu.tdc.doan_d2.model.responsive;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import vn.edu.tdc.doan_d2.model.category.Category;


public class CategoryRecipeResponsive {
    private final Application application;
    private final MutableLiveData<ArrayList<Category>> categoriesLiveData = new MutableLiveData<>();

    private  ArrayList<Category>  categories;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public CategoryRecipeResponsive(Application application) {
        this.application = application;

    }

    public MutableLiveData<ArrayList<Category>> getAllCategory() {

        if (categories == null && !isLoading.getValue()) {
            loadCategoriesFromFirebase();

            Log.d("getAllCategory", "if");
        } else { // Chỉ fetch nếu chưa fetch trước đó
            isLoading.setValue(true);
            categoriesLiveData.setValue(categories);

            Log.d("getAllCategory", "else");
        }

        Log.d("categoriesLiveDataRes", categoriesLiveData.getValue() + "");
        return categoriesLiveData;
    }

    public MutableLiveData<ArrayList<Category>> loadCategoriesFromFirebase() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        getCategoriesFromFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        categories.add(category);
                    }
                }
                else {
                    Log.d("Firebase", "Data snapshot is empty");
                }
                categoriesLiveData.postValue(categories);
                Log.d("categoriesLiveData", "Data from Firebase: " + categoriesLiveData.getValue());
                isLoading.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                categoriesLiveData.setValue(null);
                Log.e("Firebase Error", "Failed to fetch data", databaseError.toException());
                isLoading.setValue(false);
            }

        });
        Log.d("categoriesLiveData", "Data from Firebase: " + categoriesLiveData.getValue());
        return categoriesLiveData;
    }

    public ArrayList<Category> getCategoriesByRange(int startIndex, int endIndex) {
        ArrayList<Category> result = new ArrayList<>();
        if (categories != null) {

            int cachedSize = categories.size();
            for (int i = startIndex; i < endIndex && i < cachedSize; i++) {
                result.add(categories.get(i));
            }
        } else {
            isLoading.observeForever(new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean loading) {
                    if (!loading) { // Dữ liệu đã tải xong
                        isLoading.removeObserver(this); // Loại bỏ observer để tránh rò rỉ bộ nhớ
                        result.addAll(getCategoriesByRange(startIndex, endIndex)); // Gọi lại hàm để lấy dữ liệu
                    }
                }
            });
        }
        return result;
    }

    public DatabaseReference getCategoriesFromFirebase() {
        Log.d("getCategoriesFromFirebase", "call");
        return FirebaseDatabase.getInstance().getReference("categories"); // Giả sử 'categories' là node chính
    }
    public int getCategoriesCount() {
        Log.d("allCategoriesCache",categories.size()+"");
        return categories != null ? categories.size() : 0;
    }
}
