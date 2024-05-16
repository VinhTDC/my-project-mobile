package vn.edu.tdc.doan_d2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.fragment.CategoryFragment;
import vn.edu.tdc.doan_d2.fragment.PaginationInterface;
import vn.edu.tdc.doan_d2.model.category.CategoryDiffCallback;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;


public class MainActivity extends AppCompatActivity implements PaginationInterface {
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding binding;
    private CategoryViewModel viewModel;
    private CategoryViewModelRetrofit loadData;
    private static final String PREF_RETROFIT_RUN_FLAG = "retrofit_run_flag";
    private CategoryFragment fragment;
    private String tagFragment = "CATEGORY_FRAGMENT_TAG";
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        setupToolbar();
//         Khởi tạo ViewModel

        if (!isRetrofitRunToday()) {
            runRetrofit();
            updateRetrofitRunFlag();
        }
        swipeRefreshLayout = binding.swipeLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.goToNextPage();
            }
        });
        binding.buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.goToPreviousPage();
            }
        });
        loadData = new CategoryViewModelRetrofit(getApplication());
        searchView = binding.toolbar.findViewById(R.id.searchView);
        if (savedInstanceState == null) {
            addCategoryFragmentWithPaginationInterface();
            fragment.setSearchView(searchView);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void setupToolbar() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.id_category) {
                    viewModel.setIsCategory(true);
                } else if (itemId == R.id.id_cuisine){
                    viewModel.setIsCategory(false);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        // Thiết lập ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void loadDataRetrofitToFirebase() {

        loadData = new CategoryViewModelRetrofit(getApplication());
        viewModel.getIsCategory().observe(this, isCategory -> {

            loadData.getAllCategoryRetrofit(isCategory).observe(this, new Observer<ArrayList<String>>() {
                @Override
                public void onChanged(ArrayList<String> strings) {

                }
            });
        });


    }

    private boolean isRetrofitRunToday() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); // Sử dụng 'this' nếu đang ở trong Activity
        String lastRunDate = prefs.getString(PREF_RETROFIT_RUN_FLAG, ""); // Lấy ngày chạy lần cuối

        // Tính ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // So sánh ngày để xác định xem đã chạy hôm nay chưa
        return lastRunDate.equals(String.format(Locale.getDefault(), "%04d-%02d-%02d", currentYear, currentMonth, currentDay));
    }

    private void updateRetrofitRunFlag() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); // Sử dụng 'this' nếu đang ở trong Activity
        SharedPreferences.Editor editor = prefs.edit();

        // Lưu ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        editor.putString(PREF_RETROFIT_RUN_FLAG, String.format(Locale.getDefault(), "%04d-%02d-%02d", currentYear, currentMonth, currentDay));

        editor.apply();
    }

    private void runRetrofit() {
        loadDataRetrofitToFirebase();
    }

    private void addCategoryFragmentWithPaginationInterface() {
        FragmentManager fragmentManager = getSupportFragmentManager();


        CategoryFragment existingFragment = (CategoryFragment) fragmentManager.findFragmentByTag(tagFragment);
        if (existingFragment == null) {
            fragment = new CategoryFragment();
            fragment.setPaginationInterface(this);
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment, tagFragment).commit();
        } else {
            fragment = new CategoryFragment();
            fragment.setPaginationInterface(this);
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment, tagFragment).commit();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment1 != null) {
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void goToPreviousPage() {

    }

    @Override
    public void goToNextPage() {

    }

    @Override
    public void onPageChanged(int currentPage) {

    }

}
