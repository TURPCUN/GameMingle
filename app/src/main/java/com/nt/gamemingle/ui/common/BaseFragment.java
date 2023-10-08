package com.nt.gamemingle.ui.common;

import com.google.android.material.appbar.MaterialToolbar;
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
}