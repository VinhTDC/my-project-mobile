package vn.edu.tdc.doan_d2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.tdc.doan_d2.databinding.ActivityMealDetailBinding;
import vn.edu.tdc.doan_d2.fragment.MealDetailGeneralFragment;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.view.MealDetailPagerAdapter;
import vn.edu.tdc.doan_d2.viewmodel.mealdetail.MealDetailViewModel;

public class MealDetailActivity extends AppCompatActivity {
    private ActivityMealDetailBinding binding;
    private MealDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_meal_detail);
        viewModel = new ViewModelProvider(this).get(MealDetailViewModel.class);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("mealId")) {
            String idMeal = intent.getStringExtra("mealId");
            viewModel.setIdMeal(idMeal);


            Log.d("idMeal",viewModel.getIdMeal().getValue()+"");
        }

        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager2 = binding.viewPager;
        MealDetailPagerAdapter adapter = new MealDetailPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();

    }

    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return "General";
            case 1:
                return "Guide";
            default:
                return null;
        }
    }

}