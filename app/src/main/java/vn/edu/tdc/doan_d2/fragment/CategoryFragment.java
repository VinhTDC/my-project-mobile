package vn.edu.tdc.doan_d2.fragment;

import android.annotation.SuppressLint;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


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


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.MealActivity;
import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.databinding.FragmentCategoryBinding;
import vn.edu.tdc.doan_d2.model.BaseCategory;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.meal.Meal;
import vn.edu.tdc.doan_d2.model.responsive.category.CategoryFilter;
import vn.edu.tdc.doan_d2.view.MealAdapter;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;


public class CategoryFragment extends Fragment implements PaginationInterface, OnCategoryClickListener {
    private FragmentCategoryBinding binding;
    private SearchView searchView;

    CategoryViewModelRetrofit categoryViewModelRetrofit;

    private CategoryViewModel viewModelCategory;

    private MyAdapter adapter;
    private MealAdapter mealAdapter;
    private PaginationInterface paginationInterface;
    private int currentPage = 0;
    private int categoriesCount = 0;
    private String tagFragment = "CATEGORY_FRAGMENT_TAG";
    private String tagFragmentMeal = "MEAL_FRAGMENT_TAG";
    private boolean isLoading = true;
    private static final int PAGE_SIZE = 50;
    private boolean isUpdatingAdapter = false;
    private MutableLiveData<String> currentQueryLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPageLiveData = new MutableLiveData<>(currentPage);

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
        setupSearchView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PaginationInterface) {
            paginationInterface = (PaginationInterface) context;
        } else {
            throw new RuntimeException(context + " phải triển khai  PaginationInterface");
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
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
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
        // Khởi tạo categoryUtils</layout>
        viewModelCategory.getCategoriesCountLiveData().observe(getViewLifecycleOwner(), total -> {
            this.categoriesCount = total;
        });
        currentPageLiveData.observe(getViewLifecycleOwner(), page -> {
            this.currentPage = page; // Cập nhật currentPage từ LiveData
            loadCategoriesForPage(page);
        });
        loadCategoriesForSearch();

    }

    private void updateCategoriesInAdapter(ArrayList<BaseCategory> newCategories) {
        if (!binding.recyclerview.isComputingLayout()) {
            isUpdatingAdapter = true;
            if (newCategories != null) {// Bắt đầu cập nhật Adapter
                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        if (adapter == null) {
                            adapter = new MyAdapter(requireContext(), newCategories, CategoryFragment.this);
                            binding.recyclerview.setAdapter(adapter);
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
            Log.d("RecyclerView111111111", "Đang trong quá trình tính toán layout, không thực hiện cập nhật ngay lúc này");
        }
    }

    private void setupRecyclerView() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void goToPreviousPage() {
        if (adapter != null) {
            if (viewModelCategory != null) {
                int totalPage = (int) Math.ceil((double) categoriesCount / PAGE_SIZE);
                Log.d("total", totalPage + "");
                if (currentPage > 0) {
                    currentPageLiveData.setValue(currentPage - 1); // Sử dụng LiveData để cập nhật
                } else {
                    currentPageLiveData.setValue(totalPage - 1); // Sử dụng LiveData để cập nhật
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void goToNextPage() {
        int totalPage = (int) Math.ceil((double) categoriesCount / PAGE_SIZE);
        if (adapter != null) {
            if (viewModelCategory != null) { // Kiểm tra null
                if (currentPage < totalPage - 1) {
                    currentPageLiveData.setValue(currentPage + 1); // Sử dụng LiveData để cập nhật
                } else {
                    currentPageLiveData.setValue(0); // Sử dụng LiveData để cập nhật
                }
            }
        }

    }

    private void loadCategoriesForSearch() {
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

    public void loadCategoriesForPage(int page) {
        getViewLifecycleOwnerLiveData().observe(getViewLifecycleOwner(), viewLifecycleOwner -> {
            if (viewLifecycleOwner != null) {
                viewModelCategory.loadCategoriesForPage(page, PAGE_SIZE, getViewLifecycleOwner()).observe(getViewLifecycleOwner(), new Observer<ArrayList<BaseCategory>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChanged(ArrayList<BaseCategory> categories) {
                        // Cập nhật Adapter]
                        if (categories != null) {
                            if (adapter == null) {
                                adapter = new MyAdapter(requireContext(), categories, CategoryFragment.this);
                                binding.recyclerview.setAdapter(adapter);
                                currentPageLiveData.setValue(page);
                                adapter.notifyDataSetChanged();
                            } else {
                                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(adapter.getData(), categories));
                                adapter.setData(categories);
                                diffResult.dispatchUpdatesTo(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            Log.e("CategoryFragment123131", "Failed to load categories for page " + page);
                        } else {
                            // Handle loading error (e.g., show error message)
                            Log.e("CategoryFragment", "Failed to load categories for page " + page);

                        }
                        binding.recyclerview.scrollToPosition(0);
                        isLoading = false; // Finish loading
                    }
                });
            }

        });


    }
    //reset Currentpage


    public void setCurrentPage(int currentPage) {
        this.currentPageLiveData.setValue(currentPage);
    }

    @Override
    public void onCategoryClick(BaseCategory meal) {
        String nameCategory = meal.getName().toLowerCase();
        categoryViewModelRetrofit.getAllMealRetrofit(meal.getName());
        Intent intent = new Intent(getActivity(), MealActivity.class); // Activity chứa MealFragment
        intent.putExtra("categoryName", nameCategory);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Glide.with(this).pauseRequests();
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.with(this).onStop();
    }
}