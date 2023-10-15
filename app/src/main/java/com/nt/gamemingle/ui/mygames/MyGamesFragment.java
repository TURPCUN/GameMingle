package com.nt.gamemingle.ui.mygames;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nt.gamemingle.R;
import com.nt.gamemingle.ui.common.BaseFragment;


public class MyGamesFragment extends BaseFragment {

    public MyGamesFragment() {
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
        return inflater.inflate(R.layout.fragment_my_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyGamesFavoritesFragment myGamesFavoritesFragment = new MyGamesFavoritesFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.card_view_favorites, myGamesFavoritesFragment)
                .commit();
    }
}