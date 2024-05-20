package vn.edu.tdc.doan_d2.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.MealActivity;

import vn.edu.tdc.doan_d2.MealDetailActivity;
import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.databinding.FragmentCategoryBinding;
import vn.edu.tdc.doan_d2.databinding.FragmentMealListBinding;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryFilter;
import vn.edu.tdc.doan_d2.view.MealAdapter;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;


public class MealFragment extends Fragment implements PaginationInterface,OnMealClickListener {
    private FragmentMealListBinding binding;

    private SearchView searchView;
    private ActivityMainBinding bindingMain;
    CategoryViewModelRetrofit categoryViewModelRetrofit;

    private CategoryViewModel viewModelCategory;

    private MealAdapter adapter;
    private PaginationInterface paginationInterface;
    private int currentPage = 0;
    private int categoriesCount = 0;
    private String tagFragment = "MEAL_FRAGMENT_TAG";
    private boolean isLoading = true;
    private static final int PAGE_SIZE = 50;
    private boolean isUpdatingAdapter = false;

    private MutableLiveData<Integer> currentpageLive =  new MutableLiveData<>(currentPage);

    private CategoryFilter categoryFilter;
    private MutableLiveData<String> currentQueryLiveData = new MutableLiveData<>();




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PaginationInterface) {
            paginationInterface = (PaginationInterface) context;
        } else {
            throw new RuntimeException(context + "");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        paginationInterface = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPage = getArguments().getInt("page");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMealListBinding.inflate(inflater, container, false);
        binding.getRoot().setTag(tagFragment);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryViewModelRetrofit = new ViewModelProvider(requireActivity()).get(CategoryViewModelRetrofit.class);
        viewModelCategory = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        setupRecyclerView();
        // Khởi tạo categoryUtils
        viewModelCategory.getCategoriesCountLiveData().observe(getViewLifecycleOwner(), total -> {
            this.categoriesCount = total;
        });
        loadMealsForSearch();
        currentpageLive.observe(getViewLifecycleOwner(),page ->{
            loadMealsForPage(page);
        });


    }

    private void updateCategoriesInAdapter(ArrayList<BaseCategory> newCategories) {
        if (!binding.recyclerViewMeal.isComputingLayout()) {
            isUpdatingAdapter = true;
            if (newCategories != null) {// Bắt đầu cập nhật Adapter
                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        if (adapter == null) {
                            adapter = new MealAdapter(requireContext(), newCategories,MealFragment.this);
                            binding.recyclerViewMeal.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(adapter.getData(), newCategories));
                            adapter.setData(newCategories);
                            diffResult.dispatchUpdatesTo(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
            isUpdatingAdapter = false; // Kết thúc cập nhật Adapter
        } else {
            // RecyclerView đang trong quá trình tính toán layout, không thực hiện cập nhật ngay lúc này

        }
    }

    private void setupRecyclerView() {
        binding.recyclerViewMeal.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerViewMeal.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewMeal.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void goToPreviousPage() {
        if (adapter != null) {
            if (viewModelCategory != null) {
                int totalPage = (int) Math.ceil((double) categoriesCount / PAGE_SIZE);
                Log.d("total", totalPage + "");
                if (currentPage > 0) {
                    currentPage--;
                    loadMealsForPage(currentPage);
                    adapter.notifyDataSetChanged();

                } else {
                    currentPage = totalPage - 1;
                    loadMealsForPage(currentPage);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void goToNextPage() {
        Log.d("Gotonext","call");
        int totalPage = (int) Math.ceil((double) categoriesCount / PAGE_SIZE);
        if (adapter != null) {
            if (viewModelCategory != null) { // Kiểm tra null
                if (currentPage < totalPage - 1) {
                    currentPage++;
                    loadMealsForPage(currentPage);
                    adapter.notifyDataSetChanged();
                } else {
                    currentPage = 0;
                    loadMealsForPage(currentPage);
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }

    private void loadMealsForSearch() {
        viewModelCategory.getFilteredCategoriesLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<BaseCategory>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<BaseCategory> categories) {
                // Cập nhật Adapter
                if (categories != null) {
                    updateCategoriesInAdapter(categories);
                } else {

                }
                isLoading = false; // Finish loading
            }
        });
    }

    public void setPaginationInterface(PaginationInterface paginationInterface) {
        this.paginationInterface = paginationInterface;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupSearchView() {
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    // Gọi phương thức tìm kiếm trong fragment
                    currentQueryLiveData.setValue(query);
                    currentPage = 0;
                    viewModelCategory.updateSearchQuery(query, currentPage, PAGE_SIZE, getViewLifecycleOwner());
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    currentQueryLiveData.setValue(newText);
                    currentPage = 0;
                    viewModelCategory.updateSearchQuery(newText, currentPage, PAGE_SIZE, getViewLifecycleOwner()); // Cập nhật ViewModel với truy vấn tìm kiếm mới
                    return true;
                }
            });
        }
    }

    public void loadMealsForPage(int page) {
        Log.d("CategoryFragment1232", "Failed to load categories for page " + page);
        viewModelCategory.loadMealsForPage(page, PAGE_SIZE, getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<ArrayList<BaseCategory>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<BaseCategory> meals) {
                // Cập nhật Adapter]

                if (meals != null) {
                    if (adapter == null) {
                        adapter = new MealAdapter(requireContext(), meals,MealFragment.this);
                        binding.recyclerViewMeal.setAdapter(adapter);
                        currentpageLive.setValue(page);
                        adapter.notifyDataSetChanged();
                    } else {
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(adapter.getData(), meals));
                        adapter.setData(meals);
                        diffResult.dispatchUpdatesTo(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    // Handle loading error (e.g., show error message)
                    Log.e("CategoryFragment", "Failed to load categories for page " + page);

                }
                binding.recyclerViewMeal.scrollToPosition(0);
                isLoading = false; // Finish loading
            }
        });

    }
    //reset Currentpage


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        Log.d("123213131", "call");

    }
    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
        setupSearchView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.with(this).pauseRequests();
    }

    @Override
    public void onMealClick(BaseCategory meal) {
        categoryViewModelRetrofit.getAllMealDetailRetrofit(meal.getId());
        Intent intent = new Intent(getActivity(), MealDetailActivity.class); // Activity chứa MealDetailFragment
        intent.putExtra("mealId", meal.getId()+"");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public MutableLiveData<Integer> getCurrentpageLive() {
        return currentpageLive;
    }

    public void setCurrentpageLive(int currentpageLive) {
        this.currentpageLive.setValue(currentpageLive);
    }
}
