package com.nt.gamemingle.ui.mygames;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nt.gamemingle.R;
import com.nt.gamemingle.ui.common.BaseFragment;

public class MyGamesEmpty extends BaseFragment {

    public MyGamesEmpty() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_games_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        Button button = view.findViewById(R.id.btn_explore_games);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearchGames();
            }
        });
    }

    private void navigateToSearchGames() {
        appViewModel.getNavController().getValue().navigate(R.id.action_myGamesEmpty_to_searchGamesFragment);
    }
}