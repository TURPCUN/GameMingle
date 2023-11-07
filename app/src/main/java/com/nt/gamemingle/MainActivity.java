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
        setUpBottomNavigationBarClickListener();

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
            }
        });
    }

    private void onClickNavigateUp() {
        navController.navigateUp();
        // navController.popBackStack();
    }

    private void setUpBottomNavigationBarClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener( item -> {
            switch (item.getItemId()) {
                case R.id.menuGames:
                    navController.navigate(R.id.myGamesFragment);
                    break;
                case R.id.menuEvents:
                    navController.navigate(R.id.eventsFragment);
                    break;
                case R.id.menuCreateEvent:
                    navController.navigate(R.id.createEventFragment);
                    break;
                case R.id.menuProfile:
                    navController.navigate(R.id.testFragment);
                    break;
                case R.id.menuNotification:
                    navController.navigate(R.id.eventsFragment);
                    break;

            }
            return true;
        });
    }
}