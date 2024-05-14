package vn.edu.tdc.doan_d2.model.responsive.cuisine;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;

public interface CuisineDataSource {
    MutableLiveData<ArrayList<Cuisine>> getAllCuisine();

    void loadCuisinesFromFirebase();

    DatabaseReference getCuisinesFromFirebase();
}
