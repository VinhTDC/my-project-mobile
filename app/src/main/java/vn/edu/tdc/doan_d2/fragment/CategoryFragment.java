package vn.edu.tdc.doan_d2.fragment;

import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.databinding.FragmentCategoryBinding;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.model.responsive.CategoryRecipeResponsive;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;

public class CategoryFragment extends Fragment implements PaginationInterface {
    private FragmentCategoryBinding binding;

    private SearchView searchView;
    private ActivityMainBinding bindingMain;

    private MainActivityViewModel viewModel;
    private MyAdapter adapter;
    private PaginationInterface paginationInterface;
    private int currentPage = 0;
    private int categoriesCount = 0;
    private String tagFragment = "CATEGORY_FRAGMENT_TAG";
    private boolean isLoading = true;
    private static final int PAGE_SIZE = 50;
    private boolean isUpdatingAdapter = false;


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
            throw new RuntimeException(context.toString() + " phải triển khai  PaginationInterface");
        }
    }

    public void setViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
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
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setupRecyclerView();
//        viewModel.loadCategoriesForPage(currentPage, PAGE_SIZE).observe(getViewLifecycleOwner(), upCategories -> {
//            if (upCategories != null) {
//                adapter.setData(upCategories);
//                adapter.notifyDataSetChanged();
//            }
//        });


        viewModel.getFilteredCategoriesLiveData().observe(getViewLifecycleOwner(), filteredCategories -> {
            if (filteredCategories != null) {
                updateCategoriesInAdapter(filteredCategories); // Cập nhật Adapter với kết quả tìm kiếm
                // Sau khi cập nhật dữ liệu, reset lại currentPage về 0 và tải lại trang đầu tiên

            } else {


            }
        });

        viewModel.getCategoriesCountLiveData().observe(getViewLifecycleOwner(), total -> {
            this.categoriesCount = total;
        });
        loadCategoriesForPage(currentPage);
//
    }

    private void updateCategoriesInAdapter(ArrayList<Category> newCategories) {
        isUpdatingAdapter = true;
        if (newCategories != null) {// Bắt đầu cập nhật Adapter
            if (adapter == null) {
                // Nếu chưa có Adapter, tạo mới và thiết lập cho RecyclerView
                adapter = new MyAdapter(requireContext(), newCategories);
                binding.recyclerview.setAdapter(adapter);
                currentPage = 0;
            } else {
                currentPage = 0;
                // Nếu đã có Adapter, cập nhật dữ liệu và sử dụng DiffUtil để tối ưu hóa hiệu suất cập nhật giao diện
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(adapter.getData(), newCategories));
                adapter.setData(newCategories);
                diffResult.dispatchUpdatesTo(adapter);
            }
        }

        isUpdatingAdapter = false; // Kết thúc cập nhật Adapter

    }

    private void setupRecyclerView() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.setAdapter(adapter);
    }

    @Override
    public void goToPreviousPage() {
        if (viewModel != null) {
            int totalPage = (int) Math.ceil((double) categoriesCount / PAGE_SIZE);
            Log.d("total", totalPage + "");
            if (currentPage > 0) {
                currentPage--;
                loadCategoriesForPage(currentPage);

            } else {
                currentPage = totalPage - 1;
                loadCategoriesForPage(currentPage);
            }
        }

    }

    @Override
    public void goToNextPage() {
        int totalPage = (int) Math.ceil((double) categoriesCount / PAGE_SIZE);
        if (viewModel != null) { // Kiểm tra null
            if (currentPage < totalPage - 1) {
                currentPage++;
                loadCategoriesForPage(currentPage);
            } else {
                currentPage = 0;
                loadCategoriesForPage(currentPage);
            }
        }

    }

    @Override
    public void onPageChanged(int currentPage) {

    }

    public void loadCategoriesForPage(int page) {
        if (searchView.getQuery().toString().isEmpty()) {
            viewModel.loadCategoriesForPage(page, PAGE_SIZE).observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onChanged(ArrayList<Category> categories) {
                    // Cập nhật Adapter
                    if (categories != null) {
                        if (adapter == null) {
                            adapter = new MyAdapter(requireContext(), categories);
                            binding.recyclerview.setAdapter(adapter);
                        } else {
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(adapter.getData(), categories));
                            adapter.setData(categories);
                            diffResult.dispatchUpdatesTo(adapter);
                        }
                    } else {
                        // Handle loading error (e.g., show error message)
                        Log.e("CategoryFragment", "Failed to load categories for page " + page);
                    }
                    binding.recyclerview.scrollToPosition(0);
                    isLoading = false; // Finish loading


                }
            });
        }
    }

    private void loadCategoriesForSearch() {
        viewModel.getFilteredCategoriesLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Category> categories) {
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
                    query = " "+query;
                    // Gọi phương thức tìm kiếm trong fragment
                    currentPage = 0;
                    viewModel.updateSearchQuery(query,currentPage,PAGE_SIZE);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    newText = " " + newText + " ";
                    currentPage = 0;
                    viewModel.updateSearchQuery(newText,currentPage,PAGE_SIZE); // Cập nhật ViewModel với truy vấn tìm kiếm mới
                    return true;
                }
            });
        }
    }
}