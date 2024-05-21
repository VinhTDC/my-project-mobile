package vn.edu.tdc.doan_d2.viewmodel.mealdetail;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.model.comment.Comment;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;

public class MealDetailViewModel extends AndroidViewModel {
    private MealDetailResponsive mealDetailResponsive;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<MealDetailData> mealData;
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();
    private  float rating = 0;
    private MutableLiveData<Float> ratting  = new MutableLiveData<>(rating);
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


    @SuppressLint("SuspiciousIndentation")
    public MutableLiveData<List<Comment>> loadCommnet (LifecycleOwner lifecycleOwner) {
        isLoading.postValue(true);
        MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();
        idMeal.observe(lifecycleOwner, idMeal->{
            mealDetailResponsive.getComment(idMeal).observe(lifecycleOwner, commentMeal -> {
                List<Comment> comments = new ArrayList<>();
                if (commentMeal != null) { // Kiểm tra null
                    comments = commentMeal;
                }
                float totalRating = 0;
                for (Comment cmt : comments){
                    totalRating  += cmt.getRating();
                }
                totalRating = totalRating/comments.size();
                ratting.setValue(totalRating);
                Log.d("Gettoalt", ratting.getValue()+"");
                DatabaseReference mealsRef = FirebaseDatabase.getInstance().getReference("Rating/"+idMeal);
                mealsRef.child(idMeal).child("rating").setValue(totalRating); // Lưu dưới dạng số thực (float)
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

    public MutableLiveData<Float> getRatting() {
        return ratting;
    }

    public void setRatting(float  ratting) {
        this.ratting.setValue(ratting);
    }
}
