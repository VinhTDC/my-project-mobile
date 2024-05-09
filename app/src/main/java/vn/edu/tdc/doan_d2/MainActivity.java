package vn.edu.tdc.doan_d2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.model.category.Category;
import vn.edu.tdc.doan_d2.model.responsive.CategoryRecipeResponsive;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModelRetrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private MainActivityViewModelRetrofit loadData;
    private static final String PREF_RETROFIT_RUN_FLAG = "pref_retrofit_run_flag";

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
        loadData = new ViewModelProvider(this).get(MainActivityViewModelRetrofit.class);

        // Kiểm tra xem Retrofit đã được chạy trong ngày hôm nay hay không
//        if (!isRetrofitRunToday()) {
//            // Chạy Retrofit
//            runRetrofit();
//            // Cập nhật cờ trong SharedPreferences để đánh dấu rằng Retrofit đã được chạy trong ngày hôm nay
//            updateRetrofitRunFlag();
//        }


        getCategories();


        swipeRefreshLayout = binding.swipeLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getCategories();
            }
        });


    }

    private void getCategories() {
        viewModel.getAllCategory().observe(this, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> newCategories) {
                displayCategoryInRecyclerView(newCategories);
            }

        });

    }
    private void loadDataRetrofitToFriebase(){
        loadData.getAllCategoryRetrofit().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
            }
        });

    }

    private void displayCategoryInRecyclerView(ArrayList<Category> categories) {
        recyclerView = binding.recyclerview;
        // Tạo một danh sách mới để lưu các chuỗi đã được chuyển đổi
        ArrayList<Category> capitalizedCategories = categories;

        myAdapter = new MyAdapter(this, capitalizedCategories);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }
    private boolean isRetrofitRunToday() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lastRunDate = prefs.getString(PREF_RETROFIT_RUN_FLAG, "");
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return lastRunDate.equals(todayDate);
    }
    private void updateRetrofitRunFlag() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_RETROFIT_RUN_FLAG, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        editor.apply();
    }
    private void runRetrofit() {
        loadDataRetrofitToFriebase();
    }
}