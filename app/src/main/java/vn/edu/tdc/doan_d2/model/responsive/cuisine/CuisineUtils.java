package vn.edu.tdc.doan_d2.model.responsive.cuisine;


import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import java.util.ArrayList;

import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.cuisine.Cuisine;
import vn.edu.tdc.doan_d2.viewmodel.cuisine.CuisineViewModel;

public class CuisineUtils {
    private CuisineRecipeResponsive responsive;
    private CuisineViewModel viewModel;

    public CuisineUtils(CuisineRecipeResponsive responsive, CuisineViewModel viewModel) {
        this.responsive = responsive;
        this.viewModel = viewModel;
    }
    public void getCuisineByRange(int startIndex, int endIndex, CuisineUtils.CuisineResponse callback, LifecycleOwner lifecycleOwner) {

        responsive.getAllCuisine().observe(lifecycleOwner, new Observer<ArrayList<Cuisine>>() {
            @Override
            public void onChanged(ArrayList<Cuisine> cuisines) {
                if (cuisines != null) {
                    ArrayList<Cuisine> result = new ArrayList<>();
                    for (int i = startIndex; i < endIndex; i++) {
                        result.add(cuisines.get(i));
                    }
                    callback.onSuccess(result);
                } else {

                }
                responsive.getAllCuisine().removeObserver(this);
            }
        });

    }

    public interface CuisineResponse {
        void onSuccess(ArrayList<Cuisine> cuisines);

        void onError(String errorMessage);
    }
}
