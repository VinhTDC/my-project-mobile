//package vn.edu.tdc.doan_d2.model.responsive.cuisine;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.MutableLiveData;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
//
//
//public class CuisineFilter {
//    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
//
//    private CuisineRecipeResponsive responsive;
//
//
//    public MutableLiveData<ArrayList<Cuisine>> filterCategories(String query) {
//        MutableLiveData<ArrayList<Cuisine>> filteredLiveData = new MutableLiveData<ArrayList<Cuisine>>();
//        isLoading.setValue(true); // Bắt đầu quá trình tải
//        if (!query.isEmpty()) {
//            Query queryRef = responsive.getCuisinesFromFirebase().orderByChild("name")
//                    .startAt(query.trim().toLowerCase())
//                    .endAt(query.trim().toLowerCase() + "\uf8ff"); //
//            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    ArrayList<Cuisine> filteredCuisines = new ArrayList<>();
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot cuisineSnapshot : dataSnapshot.getChildren()) {
//                            String id = "";
//                            if (cuisineSnapshot.child("id").exists()) {
//                                id = cuisineSnapshot.child("id").getValue(String.class);
//                            }
//                            String name = "";
//                            if (cuisineSnapshot.child("name").exists()) {
//                                name = cuisineSnapshot.child("name").getValue(String.class);
//                            }
//                            String imageUrl = "";
//                            if (cuisineSnapshot.child("imgUrl").exists()) {
//                                imageUrl = cuisineSnapshot.child("imgUrl").getValue(String.class);
//                            }
//                            Cuisine cuisine = new Cuisine(id, name, imageUrl);
//                            filteredCuisines.add(cuisine);
//                        }
//                        filteredLiveData.postValue(filteredCuisines);
//                        isLoading.setValue(false);
//                    } else {
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Xử lý lỗi
//                    filteredLiveData.postValue(null);
//                    isLoading.setValue(false);
//                }
//            });
//        }else {
//
//        }
//        return filteredLiveData;
//    }
//    public CuisineFilter(CuisineViewModel viewModel, CuisineRecipeResponsive responsive) {
//        this.responsive = responsive;
//        this.viewModel = viewModel;
//    }
//}
