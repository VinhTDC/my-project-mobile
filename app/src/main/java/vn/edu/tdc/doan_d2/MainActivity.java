package vn.edu.tdc.doan_d2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import vn.edu.tdc.doan_d2.databinding.ActivityMainBinding;
import vn.edu.tdc.doan_d2.fragment.CategoryFragment;
import vn.edu.tdc.doan_d2.fragment.PaginationInterface;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModel;
import vn.edu.tdc.doan_d2.viewmodel.category.CategoryViewModelRetrofit;

public class MainActivity extends AppCompatActivity implements PaginationInterface {
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
        runRetrofit();
        swipeRefreshLayout = binding.swipeLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Bắt sự kiện click Button Next và Pre
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

    // Thêm menu vào thanh công cụ.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Xử lý sự kiện click trên Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.id_homeBottom) {
            // Chuyển sang trang chủ
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.id_person) {
            // Xử lý khi chọn nút Person
            return true;
        } else if (itemId == R.id.id_favorite) {
            // Chuyển sang trang yêu thích
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.id_setting) {
            // Xử lý khi chọn nút Setting
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Thiết lập thanh công cụ
    private void setupToolbar() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;

        // Navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.id_home) {
                    // Chuyển sang trang chủ
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.id_category) {
                    // Chuyển sang trang danh mục
                    viewModel.setIsCategory(true);
                    loadFragment(new CategoryFragment());
                } else if (itemId == R.id.id_cuisine) {
                    // Chuyển sang trang món ăn
                    viewModel.setIsCategory(false);
                    //loadFragment(new CuisineFragment()); // Cần tạo Fragment cho trang món ăn
                    loadFragment(new CategoryFragment());
                } else if (itemId == R.id.id_exit) {
                    // Đóng ứng dụng
                    showExitConfirmationDialog();
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

    //Tạo DiaLog thông báo thoát
    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Confirmation")
                .setMessage("Bạn có muốn thoát không?")
                .setMessage("(Về trang Home)")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Chuyển sang LoginActivity và xóa tất cả các activity trước đó
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadDataRetrofitToFirebase() {
        loadData = new CategoryViewModelRetrofit(getApplication());
        viewModel.getIsCategory().observe(this, isCategory -> {
            loadData.getAllCategoryRetrofit(isCategory).observe(this, new Observer<ArrayList<String>>() {
                @Override
                public void onChanged(ArrayList<String> strings) {
                    // Xử lý dữ liệu khi lấy xong
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "cal");
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
            fragment = existingFragment;
            fragment.setPaginationInterface(this);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment1 != null) {
            fragmentManager.beginTransaction().remove(fragment1).commitAllowingStateLoss();
        }
    }

    @Override
    public void goToPreviousPage() {
        // Xử lý chuyển về trang trước
    }

    @Override
    public void goToNextPage() {
        // Xử lý chuyển sang trang tiếp theo
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.with(this).onStop();
    }
}
