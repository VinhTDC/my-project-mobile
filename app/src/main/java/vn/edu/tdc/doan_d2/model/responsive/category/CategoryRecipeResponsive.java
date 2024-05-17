package vn.edu.tdc.doan_d2.model.responsive.category;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import vn.edu.tdc.doan_d2.model.meal.BaseMeal;
import vn.edu.tdc.doan_d2.model.meal.Meal;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;


public class CategoryRecipeResponsive implements CategoryDataSource {
    private Application application;

    private CategoryViewModel viewModel;

    private MutableLiveData<ArrayList<BaseCategory>> categoriesLiveData = new MutableLiveData<>();
    private ArrayList<BaseCategory> categoriesData;

    private MutableLiveData<ArrayList<BaseCategory>> mealsLiveData = new MutableLiveData<>();
    private ArrayList<BaseCategory> mealsData;

    private boolean isCategory;
    private boolean isDataLoaded = false;

    private static final String PREF_RETROFIT_RUN_COUNT = "retrofit_run_count";

    public CategoryRecipeResponsive(Application application, CategoryViewModel viewModel) {
        this.application = application;
        this.viewModel = viewModel;
    }

    @Override
    public MutableLiveData<ArrayList<BaseCategory>> getAllCategories(boolean isCategory) {
        if (categoriesData == null) {
            loadCategoriesFromFirebase(isCategory);
        } else if (categoriesData != null && !isDataLoaded) {
            Log.d("Firebase", "Data snapshot is empty");

            isDataLoaded = true;
            categoriesLiveData.postValue(categoriesData);
        }
        return categoriesLiveData;
    }

    public MutableLiveData<ArrayList<BaseCategory>> getAllMeals(String nameCategory) {
        if (mealsData == null) {
            loadMealFromFirebase(nameCategory);
        } else if (mealsData != null && !isDataLoaded) {
            isDataLoaded = true;
            mealsLiveData.postValue(mealsData);
        }
        return mealsLiveData;
    }

    @Override
    public void loadCategoriesFromFirebase(boolean isCategory) {
        if (categoriesData == null) {
            categoriesData = new ArrayList<>();
        }
        DatabaseReference categoryRef = getCategoriesFromFirebase();

        // Reference đến node con cụ thể dựa trên isCategory
        DatabaseReference dataRef = isCategory ? categoryRef.child("categories") : categoryRef.child("cuisines");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoriesData.clear();
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
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
                            category = new Category(name, imageUrl);
                        } else {
                            category = new Cuisine(name, imageUrl);
                        }
                        categoriesData.add(category);
                    }
                } else {
                    Log.d("Firebase", "Data snapshot is empty");
                }
                categoriesLiveData.postValue(categoriesData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                categoriesLiveData.setValue(null);
            }
        });
    }

    @Override
    public void loadMealFromFirebase(String nameCategory) {
        if (mealsData == null) {
            mealsData = new ArrayList<>();
        }
        DatabaseReference mealRef = getMealsFromFirebase();
        // Reference đến node con cụ thể dựa trên isCategory
        DatabaseReference dataRef = mealRef.child(nameCategory);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("LogSnapShot", nameCategory + "");
                if (dataSnapshot.exists()) {
                    mealsData.clear();
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        Integer id = 0;
                        if (categorySnapshot.child("id").exists()) {
                            id = categorySnapshot.child("id").getValue(Integer.class);
                        }
                        String name = "";
                        if (categorySnapshot.child("name").exists()) {
                            name = categorySnapshot.child("name").getValue(String.class);
                        }
                        String imageUrl = "";
                        if (categorySnapshot.child("imgUrl").exists()) {
                            imageUrl = categorySnapshot.child("imgUrl").getValue(String.class);
                        }

                        Meal meal = new Meal(id, name, imageUrl);


                        mealsData.add(meal);
                    }
                } else {
                    Log.d("Firebase", "Data snapshot is empty");
                }
                mealsLiveData.postValue(mealsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mealsLiveData.setValue(null);
            }
        });
    }

    @Override
    public DatabaseReference getCategoriesFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("Category");
    }

    @Override
    public DatabaseReference getMealsFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("Categories");
    }

    public void resetCategories() {
        categoriesData = null;
        isDataLoaded = false;
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

    private void resetRetrofitRunCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_RETROFIT_RUN_COUNT, 0);
        editor.apply();
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

