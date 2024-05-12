package vn.edu.tdc.doan_d2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.fragment.CategoryFragment;
import vn.edu.tdc.doan_d2.fragment.PaginationInterface;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModel;
import vn.edu.tdc.doan_d2.viewmodel.MainActivityViewModelRetrofit;

public class MainActivity extends AppCompatActivity implements PaginationInterface {
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private MainActivityViewModelRetrofit loadData;
    private static final String PREF_RETROFIT_RUN_FLAG = "pref_retrofit_run_flag";
    private CategoryFragment fragment;
    private String tagFragment = "CATEGORY_FRAGMENT_TAG";
    private androidx.appcompat.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
         // Khởi tạo ViewModel
//        if (!isRetrofitRunToday()) {
//            runRetrofit();
//            updateRetrofitRunFlag();
//        }
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

        searchView = binding.topAppBar.findViewById(R.id.searchView);
        if (savedInstanceState == null) {
            addCategoryFragmentWithPaginationInterface();
            fragment.setSearchView(searchView);
        }

    }

    private void loadDataRetrofitToFriebase() {
        loadData.getAllCategoryRetrofit().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
            }
        });

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

    private void addCategoryFragmentWithPaginationInterface() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        CategoryFragment existingFragment = (CategoryFragment) fragmentManager.
                findFragmentByTag(tagFragment);
        if (existingFragment == null) {
            fragment = new CategoryFragment();
            fragment.setPaginationInterface(this);
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment, tagFragment)
                    .commit();
        } else {
            fragment = new CategoryFragment();
            fragment.setPaginationInterface(this);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, tagFragment)
                    .commit();
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
