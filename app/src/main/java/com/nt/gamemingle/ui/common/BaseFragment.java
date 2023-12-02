package com.nt.gamemingle.ui.common;

import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nt.gamemingle.app.AppViewModel;

public abstract class BaseFragment extends androidx.fragment.app.Fragment {

    public AppViewModel appViewModel;

    public BaseFragment() {
        // Required empty public constructor
        appViewModel = AppViewModel.instance;
    }

    protected void setToolBarVisibility(boolean isVisible) {
        if (getActivity() != null) {
            MaterialToolbar toolbar = getActivity().findViewById(com.nt.gamemingle.R.id.topAppBar);
            if (isVisible) {
                toolbar.setVisibility(android.view.View.VISIBLE);
            } else {
                toolbar.setVisibility(android.view.View.GONE);
            }
        }
    }

    protected void setBottomBarVisibility(boolean isVisible) {
        if (getActivity() == null) {
            return;
        }

        BottomNavigationView bottomNavigationView = getActivity().findViewById(com.nt.gamemingle.R.id.bottom_navigation);
        BottomNavigationView bottomNavigationViewAdmin = getActivity().findViewById(com.nt.gamemingle.R.id.bottom_navigation_admin);

        bottomNavigationView.setVisibility(View.INVISIBLE);
        bottomNavigationViewAdmin.setVisibility(View.INVISIBLE);

        try {
            String userEmail = appViewModel.mAuth.getCurrentUser().getEmail();
            if (userEmail != null) {
                if (userEmail.equals("admin@admin.com")) { // Admin
                    bottomNavigationViewAdmin.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
                } else { // User
                    bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}