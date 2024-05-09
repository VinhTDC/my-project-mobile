package vn.edu.tdc.doan_d2.model.responsive;

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


public class CategoryRecipeResponsive {
    private final Application application;

    public CategoryRecipeResponsive(Application application) {
        this.application = application;
    }


    public MutableLiveData<ArrayList<Category>> allCategory() {
        MutableLiveData<ArrayList<Category>> categoriesLiveData = new MutableLiveData<>();
        getCategoriesFromFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Category> categories = new ArrayList<>();
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
                categoriesLiveData.setValue(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                categoriesLiveData.setValue(null);
                Log.e("Firebase Error", "Failed to fetch data", databaseError.toException());
            }
        });
        return categoriesLiveData;
    }

    public DatabaseReference getCategoriesFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("categories"); // Giả sử 'categories' là node chính
    }
}
