package com.nt.gamemingle.ui.common;

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
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(com.nt.gamemingle.R.id.bottom_navigation);
            if (isVisible) {
                bottomNavigationView.setVisibility(android.view.View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(android.view.View.GONE);
            }
        }
    }
}