package com.nt.gamemingle.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.nt.gamemingle.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // hide status bar
        getWindow().setFlags(0xFFFFFFFF,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // set timer for splash screen
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                } finally {
                    // go to main activity
                    Intent intent = new Intent(SplashActivity.this, com.nt.gamemingle.MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.start();


    }
}