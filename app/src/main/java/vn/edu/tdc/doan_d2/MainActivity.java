package vn.edu.tdc.doan_d2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.fragment.CategoryFragment;
import vn.edu.tdc.doan_d2.fragment.MealFragment;
import vn.edu.tdc.doan_d2.fragment.PaginationInterface;
import vn.edu.tdc.doan_d2.model.meal.Meal;
import vn.edu.tdc.doan_d2.view.MyAdapter;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;


public class MainActivity extends AppCompatActivity implements PaginationInterface {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding binding;
    private CategoryViewModel viewModel;
    private CategoryViewModelRetrofit loadData;
    private static final String PREF_RETROFIT_RUN_FLAG = "retrofit_run_flag";
    private CategoryFragment fragment;
    private MealFragment mealFragment;
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
        runRetrofit();
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
                    fragment.setCurrentPage(0);
                } else if (itemId == R.id.id_cuisine) {
                    viewModel.setIsCategory(false);
                    fragment.setCurrentPage(0);
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


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","cal");
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


}
