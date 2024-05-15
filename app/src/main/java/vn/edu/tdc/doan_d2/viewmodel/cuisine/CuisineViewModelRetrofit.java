//package vn.edu.tdc.doan_d2.viewmodel.cuisine;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.MutableLiveData;
//
//import java.util.ArrayList;
//
//import vn.edu.tdc.doan_d2.model.responsive.RecipeResponsiveRetrofit;
//
//public class CuisineViewModelRetrofit extends AndroidViewModel {
//    private RecipeResponsiveRetrofit recipeCuisineResponsiveRetrofit;
//
//    public CuisineViewModelRetrofit(@NonNull Application application) {
//        super(application);
//        this.recipeCuisineResponsiveRetrofit = new RecipeResponsiveRetrofit(application);
//    }
//    //Live Data
//
//    public MutableLiveData<ArrayList<String>> getAllCategoryRetrofit(boolean flag){
//        return recipeCuisineResponsiveRetrofit.getDataMutableLiveDataRetrofit(flag);
//    }
//}
