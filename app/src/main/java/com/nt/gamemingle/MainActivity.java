package com.nt.gamemingle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;

import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.databinding.ActivityMainBinding;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;


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

        // Picasso cache refactoring
        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(24000))
                .build();
        Picasso.setSingletonInstance(picasso);

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

    @Override
    public void onBackPressed() {
        onClickNavigateUp();
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
                    navController.navigate(R.id.profileFragment);
                    break;
                case R.id.menuNotification:
                    navController.navigate(R.id.notificationFragment);
                    break;

            }
            return true;
        });

        binding.bottomNavigation.setOnItemReselectedListener( item -> {
            switch (item.getItemId()) {
                case R.id.menuGames:
                    break;
                case R.id.menuEvents:
                    break;
                case R.id.menuCreateEvent:
                    break;
                case R.id.menuProfile:
                    break;
                case R.id.menuNotification:
                    break;

            }
        });
    }


}