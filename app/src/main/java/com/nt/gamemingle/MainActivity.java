package com.nt.gamemingle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.appbar.MaterialToolbar;
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
}