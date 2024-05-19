package vn.edu.tdc.doan_d2.viewmodel.mealdetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;


import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;

public class MealDetailViewModel extends AndroidViewModel {
    private MealDetailResponsive mealDetailResponsive;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<MealDetailData> mealData;
    private MutableLiveData<String> idMeal = new MutableLiveData<>("zxc");
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

    public MutableLiveData<String> getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal.setValue(idMeal);
    }
}
