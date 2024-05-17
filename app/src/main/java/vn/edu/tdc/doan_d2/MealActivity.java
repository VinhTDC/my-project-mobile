package vn.edu.tdc.doan_d2;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.databinding.ActivityMealBinding;
import vn.edu.tdc.doan_d2.fragment.MealFragment;
import vn.edu.tdc.doan_d2.fragment.PaginationInterface;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;

public class MealActivity extends AppCompatActivity implements PaginationInterface {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMealBinding binding;
    private CategoryViewModel viewModel;
    private CategoryViewModelRetrofit loadData;
    private static final String PREF_RETROFIT_RUN_FLAG = "retrofit_run_flag";
    private MealFragment fragment;
    private String tagFragment = "MEAL_FRAGMENT_TAG";
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_meal);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("categoryName")) {
            String categoryName = intent.getStringExtra("categoryName");
            categoryName = categoryName.toLowerCase();
            Log.d("zxc",categoryName.toLowerCase());
            viewModel.setNameCategory(categoryName);

        }
        setupToolbar();
        swipeRefreshLayout = binding.swipeLayoutMeal;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.buttonNextMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              fragment.goToNextPage();

            }
        });
        binding.buttonPreMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.goToPreviousPage();
            }
        });
        loadData = new CategoryViewModelRetrofit(getApplication());
        searchView = binding.mealToolbar.findViewById(R.id.searchMealView);
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
        toolbar = binding.mealToolbar;
        setSupportActionBar(toolbar);
        drawerLayout = binding.drawerLayoutMeal;
        navigationView = binding.navViewMeal;

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




    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","cal");
    }

    private void addCategoryFragmentWithPaginationInterface() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MealFragment existingFragment = (MealFragment) fragmentManager.findFragmentByTag(tagFragment);
        if (existingFragment == null) {
            fragment = new MealFragment();
            fragment.setPaginationInterface(this);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tagFragment).commit();
        } else {
            fragment = new MealFragment();
            fragment.setPaginationInterface(this);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tagFragment).commit();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragment_container);
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