package vn.edu.tdc.doan_d2.viewmodel.mealdetail;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;

public class MealDetailViewModel extends AndroidViewModel {
    private MealDetailResponsive mealDetailResponsive;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<MealDetailData> mealData;
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();

    private MutableLiveData<String> idMeal = new MutableLiveData<>();
    public MealDetailViewModel(@NonNull Application application) {
        super(application);
        this.mealDetailResponsive = new MealDetailResponsive(application, this);
    }

    public MutableLiveData<MealDetailData> loadMealDetail(LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<MealDetailData> mealData = new MutableLiveData<>();
        idMeal.observe(lifecycleOwner, idMeal->{
            Log.d("Mot123",idMeal);
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
    public LiveData<List<Comment>> getCommentsLiveData() {
        return commentsLiveData;
    }
    @SuppressLint("SuspiciousIndentation")
    public MutableLiveData<List<Comment>> loadCommnet (LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();
        idMeal.observe(lifecycleOwner, idMeal->{
            Log.d("Mot123",idMeal);
            mealDetailResponsive.getComment(idMeal).observe(lifecycleOwner, commentMeal -> {
                List<Comment> comments = new ArrayList<>();
                if (commentMeal != null) { // Kiểm tra null
                 comments = commentMeal;
                }

                commentsLiveData.postValue(comments); // Cập nhật LiveData trên Main Thread
                isLoading.postValue(false); // Kết thúc tải
            });

        });
        return commentsLiveData;
    }

    public MutableLiveData<String> getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal.setValue(idMeal);
    }
}
