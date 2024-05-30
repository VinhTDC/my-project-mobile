package vn.edu.tdc.doan_d2.model.responsive.mealdetail;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.comment.Rating;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.viewmodel.mealdetail.MealDetailViewModel;

public class MealDetailResponsive implements MealDetailDataSource{
    private MutableLiveData<MealDetailData> mealLiveDetailData = new MutableLiveData<>();
    private MutableLiveData<Rating> ratingLiveDetailData = new MutableLiveData<>();
    private MealDetailData mealDetailData;
    private Rating rating;
    private Comment commentMeal;
    private ArrayList<Comment> commentMeals;
    private MutableLiveData<ArrayList<Comment>> commentMutableLiveData = new MutableLiveData<>();
    private boolean isDataLoaded = false;
    private Application application;
    private MealDetailViewModel mealDetailViewModel;


    public MealDetailResponsive(Application application, MealDetailViewModel mealDetailViewModel) {
        this.application = application;
        this.mealDetailViewModel = mealDetailViewModel;
    }

    @Override
    public MutableLiveData<MealDetailData> getMealDetail(String idMeal) {
        if (mealDetailData == null) {
            loadMealDetailGeneralFromFirebase(idMeal);

        } else if (mealDetailData != null && !isDataLoaded) {
            isDataLoaded = true;
            mealLiveDetailData.postValue(mealDetailData);
        }
        return mealLiveDetailData;
    }
    public MutableLiveData<Rating> getRating(String idMeal) {
        if (rating == null) {
            loadRatingFromFirebase(idMeal);

        } else if (rating != null && !isDataLoaded) {
            isDataLoaded = true;
            ratingLiveDetailData.postValue(rating);
        }
        return ratingLiveDetailData;
    }


    @Override
    public DatabaseReference getMealDetailFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("RecipeMeal");
    }
    public DatabaseReference getRatingFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("Rating");
    }
    public void loadRatingFromFirebase(String idMeal){
        if(rating == null){
            rating = new Rating();
        }
        DatabaseReference ratingRef = getRatingFromFirebase();
        DatabaseReference dataRef = ratingRef.child(idMeal);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot dataSnapshot = snapshot;
                if(dataSnapshot.exists()){
                    float ratting = 0;
                    if(dataSnapshot.child("rating").exists()){
                        ratting = dataSnapshot.child("rating").getValue(Float.class);
                    }
                    rating = new Rating(ratting);
                }
                ratingLiveDetailData.postValue(rating);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ratingLiveDetailData.postValue(null);
            }
        });
    }
    @Override
    public void loadMealDetailGeneralFromFirebase(String idMeal) {
        if(mealDetailData == null){
            mealDetailData = new MealDetailData();
        }
        DatabaseReference mealDetailRef = getMealDetailFromFirebase();
        DatabaseReference dataRef = mealDetailRef.child(idMeal);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot dataSnapshot = snapshot.child(idMeal);
                if(dataSnapshot.exists()){
                    String name  = "";
                    if(dataSnapshot.child("name").exists()){
                       name = dataSnapshot.child("name").getValue(String.class);
                    }
                    String totalTime = "";
                    if (dataSnapshot.child("time").exists()) { // Sửa thành Time (viết hoa) do trong json là Time
                        GenericTypeIndicator<List<String>> timeListType = new GenericTypeIndicator<List<String>>() {};
                        List<String> timeList = dataSnapshot.child("time").getValue(timeListType);

                        for (int i = 0 ; i <timeList.size() ; i++){
                            String[] line = timeList.get(i).split(" ");

                            for (int j = 0 ; j < line.length ; j++ ){
                                if(line[0].equals("Total:") ||line[0].equals("Total") ) {
                                    totalTime = timeList.get(i);
                                    break;
                                }
                            }
                        }
                    }
                    float rating  = 0;
                    if(dataSnapshot.child("rating").exists()){
                      rating =  dataSnapshot.child("rating").getValue(Float.class);

                    }
                    String description  = "";
                    if(dataSnapshot.child("description").exists()){
                      description =  dataSnapshot.child("description").getValue(String.class);
                    }
                    String category  = "";
                    if(dataSnapshot.child("category").exists()){
                       category = dataSnapshot.child("category").getValue(String.class);
                    }
                    String cuisine  = "";
                    if(dataSnapshot.child("cuisine").exists()){
                      cuisine =  dataSnapshot.child("cuisine").getValue(String.class);
                    }
                    List<String> directions = new ArrayList<>();
                    if (dataSnapshot.child("directions").exists()) {
                        GenericTypeIndicator<List<String>> timeListType = new GenericTypeIndicator<List<String>>() {
                        };
                        directions = dataSnapshot.child("directions").getValue(timeListType);
                        Log.d("zxczcz", directions.toString() );

                    }
                    List<String> time = new ArrayList<>();
                    if (dataSnapshot.child("time").exists()) {
                        GenericTypeIndicator<List<String>> timeListType = new GenericTypeIndicator<List<String>>() {
                        };
                        time = dataSnapshot.child("time").getValue(timeListType);
                    }
                    List<String> nutritions = new ArrayList<>();
                    if (dataSnapshot.child("nutritions").exists()) {
                        GenericTypeIndicator<List<String>> timeListType = new GenericTypeIndicator<List<String>>() {
                        };
                        nutritions = dataSnapshot.child("nutritions").getValue(timeListType);
                    }
                    List<String> ingredients = new ArrayList<>();
                    if (dataSnapshot.child("ingredients").exists()) {
                        GenericTypeIndicator<List<String>> timeListType = new GenericTypeIndicator<List<String>>() {
                        };
                        ingredients = dataSnapshot.child("ingredients").getValue(timeListType);
                    }
                    String imgUrl  = "";
                    if(dataSnapshot.child("imgUrl").exists()){
                      imgUrl =  dataSnapshot.child("imgUrl").getValue(String.class);
                    }
                    mealDetailData = new MealDetailData(name,description,time,ingredients,directions,nutritions,rating,category,cuisine,imgUrl,totalTime);
                }
                mealLiveDetailData.postValue(mealDetailData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mealLiveDetailData.postValue(null);
            }
        });
    }


    public MutableLiveData<ArrayList<Comment>> getComment(String idMeal) {
        if (commentMeals == null) {

            loadCommentFromFirebase(idMeal);
        } else if (commentMeals != null && !isDataLoaded) {
            isDataLoaded = true;
            commentMutableLiveData.postValue(commentMeals);
        }
        return commentMutableLiveData;
    }

    public void loadCommentFromFirebase(String idMeal) {
        if (commentMeals == null) {
            commentMeals = new ArrayList<>();
        }
        DatabaseReference mealDetailRef = getCommentFromFirebase();

        DatabaseReference dataRef = mealDetailRef.child(idMeal);
        String key = dataRef.getKey();
        dataRef = dataRef.child(key);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    commentMeals.clear();
                    for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                        String user = "";
                        if (commentSnapshot.child("userName").exists()) {
                            user = commentSnapshot.child("userName").getValue(String.class);
                        }
                        String comment = "";
                        if (commentSnapshot.child("comment").exists()) {
                            comment = commentSnapshot.child("comment").getValue(String.class);
                        }
                        float rating = 0;
                        if (commentSnapshot.child("rating").exists()) {
                            rating = commentSnapshot.child("rating").getValue(Float.class);
                        }
                        commentMeal = new Comment(comment, user, rating);
                        commentMeals.add(commentMeal);
                    }
                }
                commentMutableLiveData.postValue(commentMeals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                commentMutableLiveData.postValue(null);
            }
        });
    }


    public DatabaseReference getCommentFromFirebase() {
        return FirebaseDatabase.getInstance().getReference("Comments");
    }

    public void sendCommentToFirebase(Comment comment, String idMeal) {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Comments/" + idMeal);
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (idMeal != null) {
            commentsRef.child(idMeal).push().setValue(comment).addOnSuccessListener(aVoid -> {
                Toast.makeText(application.getApplicationContext(), "Comment added successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Xử lý lỗi khi gửi bình luận
                Toast.makeText(application.getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
            });
        }
    }

}
