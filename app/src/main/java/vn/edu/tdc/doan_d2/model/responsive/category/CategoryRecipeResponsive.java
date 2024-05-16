package vn.edu.tdc.doan_d2.model.responsive.category;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;


public class CategoryRecipeResponsive implements CategoryDataSource {
    private Application application;
    private final MutableLiveData<ArrayList<BaseCategory>> categoriesLiveData = new MutableLiveData<>();
    private CategoryViewModel viewModel;

    private ArrayList<BaseCategory> categories;

    private boolean isCategory ;
    private boolean isDataLoaded = false;


    public CategoryRecipeResponsive(Application application, CategoryViewModel viewModel) {
        this.application = application;
        this.viewModel = viewModel;
    }

    @Override
    public MutableLiveData<ArrayList<BaseCategory>> getAllCategories(boolean isCategory) {
        if (categories == null) {
            loadCategoriesFromFirebase(isCategory);
        } else if (categories != null && !isDataLoaded) {
            isDataLoaded = true;
            categoriesLiveData.postValue(categories);
        }
        return categoriesLiveData;
    }

    @Override
    public void loadCategoriesFromFirebase(boolean isCategory) {
        if (categories == null) {
            categories = new ArrayList<>();
        }

        getCategoriesFromFirebase(isCategory).addValueEventListener(new ValueEventListener() {
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
                        BaseCategory category;
                        if (isCategory) {
                            category = new Category(id, name, imageUrl);
                        } else {
                            category = new Cuisine(id, name, imageUrl);
                        }
                        categories.add(category);
                    }
                } else {
                    Log.d("Firebase", "Data snapshot is empty");
                }
                categoriesLiveData.postValue(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                categoriesLiveData.setValue(null);
            }
        });
    }

    @Override
    public DatabaseReference getCategoriesFromFirebase(boolean isCategory) {
        if (isCategory) {
            return FirebaseDatabase.getInstance().getReference("categories");
        } else {
            return FirebaseDatabase.getInstance().getReference("cuisines");
        }
    }
    public void resetCategories(){
        categories = null;
        isDataLoaded = false;
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

