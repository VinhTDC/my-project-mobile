package vn.edu.tdc.doan_d2.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.doan_d2.R;
import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.databinding.FragmentCategoryBinding;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.responsive.CategoryRecipeResponsive;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements PaginationInterface {
    private FragmentCategoryBinding binding;

    private MainActivityViewModel viewModel;
    private MyAdapter adapter;
    private PaginationInterface paginationInterface;
    private int currentPage = 0;
    private static int pageSize = 20;
    private int categoriesCount = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PaginationInterface) {
            paginationInterface = (PaginationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " phải triển khai  PaginationInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        paginationInterface = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        Log.d("onCreateView", "call");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        loadCategoriesForPage(currentPage);
        setupRecyclerView();
        viewModel.getCategoriesCountLiveData().observe(getViewLifecycleOwner(), categoriesCount -> {
            this.categoriesCount = categoriesCount; // Gán giá trị mới cho categoriesCount

        });

    }

    private void setupRecyclerView() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void goToPreviousPage() {
        int totalPage = (int) Math.ceil(categoriesCount / pageSize) + 1;
        if (currentPage > 0) { // Kiểm tra xem có phải trang cuối cùng không
            currentPage--;
            loadCategoriesForPage(currentPage);
        } else {
            currentPage = totalPage - 1;
            loadCategoriesForPage(currentPage);
        }
    }

    @Override
    public void goToNextPage() {
        int totalPage = (int) Math.ceil(categoriesCount / pageSize) + 1;
        if (currentPage < totalPage - 1) { // Kiểm tra xem có phải trang cuối cùng không
            currentPage++;
            loadCategoriesForPage(currentPage);

        } else {
            currentPage = 0;
            loadCategoriesForPage(currentPage);
        }
    }

    @Override
    public void onPageChanged(int currentPage) {

    }


    private void loadCategoriesForPage(int page) {
        viewModel.loadCategoriesForPage(page, pageSize).observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Category> categories) {
                // Cập nhật Adapter
                if (adapter == null) {
                    adapter = new MyAdapter(requireContext(), categories);
                    binding.recyclerview.setAdapter(adapter);
                } else {
                    adapter.setData(categories);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    public void setPaginationInterface(PaginationInterface paginationInterface) {
        this.paginationInterface = paginationInterface;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}