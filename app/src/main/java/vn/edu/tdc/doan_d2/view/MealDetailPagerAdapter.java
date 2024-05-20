package vn.edu.tdc.doan_d2.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.tdc.doan_d2.fragment.MealDetailGeneralFragment;
import vn.edu.tdc.doan_d2.fragment.MealDirectionsFragment;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;


public class MealDetailPagerAdapter extends FragmentStateAdapter {
    public MealDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return  new MealDetailGeneralFragment();
            case 1:
                return new MealDirectionsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
