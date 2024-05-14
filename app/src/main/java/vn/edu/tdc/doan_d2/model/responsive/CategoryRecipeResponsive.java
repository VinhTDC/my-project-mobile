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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;


public class CategoryRecipeResponsive implements CategoryDataSource {
    private Application application;
    private final MutableLiveData<ArrayList<Category>> categoriesLiveData = new MutableLiveData<>();
    private MainActivityViewModel viewModel;

    private ArrayList<Category> categories;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private boolean isDataLoaded = false;


    public CategoryRecipeResponsive(Application application, MainActivityViewModel viewModel) {
        this.application = application;
        this.viewModel = viewModel;
    }

    @Override
    public MutableLiveData<ArrayList<Category>> getAllCategories() {
        if (categories == null && !isLoading.getValue()) {
            loadCategoriesFromFirebase();
        } else if (categories != null && !isDataLoaded) { // Chỉ cập nhật khi dữ liệu mới được tải
            isDataLoaded = true;
            categoriesLiveData.postValue(categories);
            isLoading.setValue(false);
        }
        return categoriesLiveData;
    }

    @Override
    public void loadCategoriesFromFirebase() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        getCategoriesFromFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    categories.clear();
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
                } else {
                    Log.d("Firebase", "Data snapshot is empty");
                }
                categoriesLiveData.postValue(categories);
                isLoading.setValue(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                categoriesLiveData.setValue(null);
                isLoading.setValue(false);
            }
        });
    }

    @Override
    public DatabaseReference getCategoriesFromFirebase() {
        Log.d("getCategoriesFromFirebase", "call");
        return FirebaseDatabase.getInstance().getReference("categories"); // Giả sử 'categories' là node chính
    }

}
//    public ArrayList<Category> getCategoriesByRange(int startIndex, int endIndex) {
//        ArrayList<Category> result = new ArrayList<>();
//        if (categories != null) {
//
//            int cachedSize = categories.size();
//            for (int i = startIndex; i < endIndex && i < cachedSize; i++) {
//                result.add(categories.get(i));
//            }
//        } else {
//            isLoading.observeForever(new Observer<Boolean>() {
//                @Override
//                public void onChanged(Boolean loading) {
//                    if (!loading) { // Dữ liệu đã tải xong
//                        isLoading.removeObserver(this); // Loại bỏ observer để tránh rò rỉ bộ nhớ
//                        result.addAll(getCategoriesByRange(startIndex, endIndex)); // Gọi lại hàm để lấy dữ liệu
//                    }
//                }
//            });
//        }
//        return result;
//    }


//    public MutableLiveData<ArrayList<Category>> searchCategoriesFromFirebase(String query) {
//
//        isLoading.setValue(true); // Bắt đầu quá trình tải
//        if (categories == null) {
//            categories = new ArrayList<>();
//        }
//        Query queryRef = getCategoriesFromFirebase().orderByChild("name") // Sắp xếp theo tên
//                .startAt(query.trim())
//                .endAt(query.trim() + "\uf8ff"); //
//        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<Category> filteredCategories = new ArrayList<>();
//                if (dataSnapshot.exists()) {
//                    Log.d("Firebase", "Data snapshot exists");
//                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                        String id = "";
//                        if (categorySnapshot.child("id").exists()) {
//                            id = categorySnapshot.child("id").getValue(String.class);
//                        }
//                        String name = "";
//                        if (categorySnapshot.child("name").exists()) {
//                            name = categorySnapshot.child("name").getValue(String.class);
//                        }
//                        String imageUrl = "";
//                        if (categorySnapshot.child("imgUrl").exists()) {
//                            imageUrl = categorySnapshot.child("imgUrl").getValue(String.class);
//                        }
//                        Category category = new Category(id, name, imageUrl);
//                        filteredCategories.add(category);
//                    }
//                }
//                categoriesLiveData.postValue(filteredCategories);
//                viewModel.setFilteredCategoriesLiveData(filteredCategories);
//                Log.d("Tao la loi",+ categoriesLiveData.getValue().size() +"");
//                isLoading.setValue(false);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý lỗi
//                categoriesLiveData.postValue(null);
//                isLoading.setValue(false);
//            }
//        });
//        Log.d("query",query);
//        Log.d("query",categoriesLiveData.getValue() + "1");
//        return categoriesLiveData;
//    }

