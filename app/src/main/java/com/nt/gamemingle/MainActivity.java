package com.nt.gamemingle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private AppViewModel appViewModel;
    private NavController navController;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initializeViewModel();
        initializeNavController();
        setUpTopAppBarClickListener();
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Tıklanan öğenin ID'sini alabilir ve işleyebilirsiniz
                int id = item.getItemId();
                switch (id) {
                    case R.id.inbox_item:
                        // İlk öğe tıklandığında yapılacak işlemler
                        break;
                    case R.id.outbox_item:
                        // İkinci öğe tıklandığında yapılacak işlemler
                        break;
                    // Diğer öğeleri burada işleyin
                }
                // True döndürmek öğenin seçili olduğunu gösterir
                return true;
            }
        });
    }

    private void initializeViewModel() {
        appViewModel = AppViewModel.instance;
    }

    private void initializeNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_container);
        navController = navHostFragment.getNavController();
        appViewModel.setNavController(navController);
    }

    private void setUpTopAppBarClickListener() {
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNavigateUp();
                /*
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                 if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                 */
            }
        });
    }

    private void onClickNavigateUp() {
        navController.navigateUp();
        // navController.popBackStack();
    }
}