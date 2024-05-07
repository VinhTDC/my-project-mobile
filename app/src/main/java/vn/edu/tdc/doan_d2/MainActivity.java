package vn.edu.tdc.doan_d2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.model.category.Categories;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> categories;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        );

        viewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);

        getCategories();



        swipeRefreshLayout = binding.swipeLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategories();
            }
        });


    }

    private void getCategories() {

        viewModel.getAllCategory().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> newCategories) {

                displayCategoryInRecyclerView(newCategories);
            }

        });

    }

    private void displayCategoryInRecyclerView(ArrayList<String> categories) {
        recyclerView = binding.recyclerview;

        // Tạo một danh sách mới để lưu các chuỗi đã được chuyển đổi
        ArrayList<String> capitalizedCategories = new ArrayList<>();

        // Duyệt qua từng chuỗi trong danh sách categories và chuyển đổi chữ cái đầu tiên sang chữ hoa
        for (String category : categories) {
            if (category != null && !category.isEmpty()) {
                // Chuyển đổi chữ cái đầu tiên sang chữ hoa và thêm vào danh sách mới
                String capitalizedCategory = category.substring(0, 1).toUpperCase() + category.substring(1);
                capitalizedCategories.add(capitalizedCategory);
            }
        }

        myAdapter = new MyAdapter(this, capitalizedCategories);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }
}