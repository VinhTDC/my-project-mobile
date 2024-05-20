package vn.edu.tdc.doan_d2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.tdc.doan_d2.databinding.GuideRecipeMealLayoutBinding;
import vn.edu.tdc.doan_d2.model.mealdetail.MealDetailData;
import vn.edu.tdc.doan_d2.model.responsive.mealdetail.MealDetailResponsive;
import vn.edu.tdc.doan_d2.viewmodel.mealdetail.MealDetailViewModel;

public class MealDirectionsFragment extends Fragment {
    private MealDetailResponsive mealDetailResponsive;
    private MealDetailViewModel viewModel;
    private GuideRecipeMealLayoutBinding binding;
    private final String tagFragment = "GUIDE_FRAGMENT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GuideRecipeMealLayoutBinding.inflate(inflater, container, false);
        binding.getRoot().setTag(tagFragment);
        viewModel = new ViewModelProvider(requireActivity()).get(MealDetailViewModel.class);
        mealDetailResponsive = new MealDetailResponsive(getActivity().getApplication(), viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
    }

    private void setupData() {
        viewModel.loadMealDetail(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<MealDetailData>() {
            @Override
            public void onChanged(MealDetailData mealDetailData) {

                StringBuilder stringDirection = new StringBuilder();
                for (int i = 0; i < mealDetailData.getDirections().size(); i++) {
                    stringDirection.append(i + 1).append(". ").append(mealDetailData.getDirections().get(i)).append("\n");
                }
                binding.directText.setText(stringDirection.toString());

                StringBuilder stringIngre = new StringBuilder();
                for (int i = 0; i < mealDetailData.getIngredients().size(); i++) {
                    stringIngre.append(i + 1).append(". ").append(mealDetailData.getIngredients().get(i)).append("\n");
                }
                binding.ingreText.setText(stringIngre.toString());

                StringBuilder stringTime = new StringBuilder();
                for (int i = 0; i < mealDetailData.getTime().size(); i++) {
                    stringTime.append(i + 1).append(". ").append(mealDetailData.getTime().get(i)).append("\n");
                }
                binding.timeText.setText(stringTime.toString());

                StringBuilder stringNutrition = new StringBuilder();
                for (int i = 0; i < mealDetailData.getNutritions().size(); i++) {
                    stringNutrition.append(i + 1).append(". ").append(mealDetailData.getNutritions().get(i)).append("\n");
                }
                binding.nutriText.setText(stringNutrition.toString());
            }
        });
    }
}
