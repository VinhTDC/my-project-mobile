package vn.edu.tdc.doan_d2.viewmodel.mealdetail;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.comment.Rating;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;

public class MealDetailViewModel extends AndroidViewModel {
    private MealDetailResponsive mealDetailResponsive;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<MealDetailData> mealData;
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> rating = new MutableLiveData<>();
    private MutableLiveData<String> idMeal = new MutableLiveData<>();
    public MealDetailViewModel(@NonNull Application application) {
        super(application);
        this.mealDetailResponsive = new MealDetailResponsive(application, this);
    }

    public MutableLiveData<MealDetailData> loadMealDetail(LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<MealDetailData> mealData = new MutableLiveData<>();
        idMeal.observe(lifecycleOwner, idMeal->{

            mealDetailResponsive.getMealDetail(idMeal).observe(lifecycleOwner, mealDetail -> {
                MealDetailData mealDetailData = new MealDetailData();
                if (mealDetail != null) { // Kiểm tra null
                   mealDetailData = mealDetail;
                }
                mealData.postValue(mealDetailData); // Cập nhật LiveData trên Main Thread
                isLoading.postValue(false); // Kết thúc tải
            });

        });
        return mealData;
    }

    public MutableLiveData<Rating> loadRating(LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<Rating> ratingData = new MutableLiveData<>();
        idMeal.observe(lifecycleOwner, idMeal -> {

            mealDetailResponsive.getRating(idMeal).observe(lifecycleOwner, getRating -> {
                Rating ratting = new Rating();
                if (getRating != null) { // Kiểm tra null
                    ratting = getRating;
                }
                ratingData.postValue(ratting); // Cập nhật LiveData trên Main Thread
                isLoading.postValue(false); // Kết thúc tải
            });

        });
        return ratingData;
    }

    public MutableLiveData<List<Comment>> loadComment(LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();
        idMeal.observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String idMeal) {
                if (idMeal != null) {
                    mealDetailResponsive.getComment(idMeal).observe(lifecycleOwner, new Observer<List<Comment>>() {
                        @Override
                        public void onChanged(List<Comment> commentMeal) {
                            List<Comment> comments = commentMeal != null ? commentMeal : new ArrayList<>();

                            float totalRating = 0;
                            for (Comment cmt : comments) {
                                totalRating += cmt.getRating();
                            }

                            if (!comments.isEmpty()) {
                                totalRating = totalRating / comments.size();
                            }

                            rating.setValue(totalRating);

                            DatabaseReference mealsRef = FirebaseDatabase.getInstance().getReference("Rating").child(idMeal);
                            float finalTotalRating = totalRating;
                            mealsRef.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    mealsRef.child("rating").setValue(snapshot.exists() ? finalTotalRating : 0);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle any error if needed
                                }
                            });

                            commentsLiveData.postValue(comments); // Update LiveData on Main Thread
                            isLoading.postValue(false); // Finish loading
                        }
                    });
                } else {
                    isLoading.postValue(false);
                }
            }
        });

        return commentsLiveData;
    }

    public MutableLiveData<String> getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal.setValue(idMeal);
    }

    public MutableLiveData<Float> getRating() {
        return rating;
    }

    public void setRating(MutableLiveData<Float> rating) {
        this.rating = rating;
    }
}
