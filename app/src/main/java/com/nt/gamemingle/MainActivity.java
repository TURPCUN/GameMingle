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


public class MainActivity extends AppCompatActivity {
    private AppViewModel appViewModel;
    EditText emailText, passwordText;
    Button signUpButton, signInButton;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appViewModel = AppViewModel.instance;
        // temporary sign out
        appViewModel.signOut();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_container);
        navController = navHostFragment.getNavController();

        appViewModel.setNavController(navController);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
                // navController.popBackStack();
            }
        });


/*
        emailText = findViewById(R.id.user_email_edit_text);
        passwordText = findViewById(R.id.user_password_edit_text);

        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.btn_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        signInButton = findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            System.out.println("User is logged in");
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            System.out.println("User is not logged in");
        }
*/

    }
}