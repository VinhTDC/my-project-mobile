package vn.edu.tdc.doan_d2.model.responsive.cuisine;

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

import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.cuisine.CuisineViewModel;

public class CuisineRecipeResponsive implements CuisineDataSource{
    private Application application;
    private final MutableLiveData<ArrayList<Cuisine>> cuisinesLiveData = new MutableLiveData<>();
    private CuisineViewModel viewModel;

    private ArrayList<Cuisine> cuisines;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private boolean isDataLoaded = false;

    public CuisineRecipeResponsive(Application application, CuisineViewModel viewModel) {
        this.application = application;
        this.viewModel = viewModel;
    }
    @Override
    public MutableLiveData<ArrayList<Cuisine>> getAllCuisine() {
        if (cuisines == null && !isLoading.getValue()) {
            loadCuisinesFromFirebase();
        } else if (cuisines != null && !isDataLoaded) { // Chỉ cập nhật khi dữ liệu mới được tải
            isDataLoaded = true;
            cuisinesLiveData.postValue(cuisines);
            isLoading.setValue(false);
        }
        return cuisinesLiveData;
    }


    @Override
    public void loadCuisinesFromFirebase() {
        if (cuisines == null) {
            cuisines = new ArrayList<>();
        }
        getCuisinesFromFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cuisines.clear();
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
                        Cuisine cuisine = new Cuisine(id, name, imageUrl);
                        cuisines.add(cuisine);

                    }
                } else {
                    Log.d("Firebase", "Data snapshot is empty");
                }
                cuisinesLiveData.postValue(cuisines);
                isLoading.setValue(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cuisinesLiveData.setValue(null);
                isLoading.setValue(false);
            }
        });
    }

    @Override
    public DatabaseReference getCuisinesFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("cuisines");
    }
}
