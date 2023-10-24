package com.nt.gamemingle.ui.mygames;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentMyGamesBinding;
import com.nt.gamemingle.ui.common.BaseFragment;


public class MyGamesFragment extends BaseFragment {

    FragmentMyGamesBinding binding;
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
        binding = FragmentMyGamesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        MyGamesFavouritesFragment myGamesFavouritesFragment = new MyGamesFavouritesFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.card_view_favorites, myGamesFavouritesFragment)
                .commit();

        MyGamesMyLibraryFragments myGamesMyLibraryFragments = new MyGamesMyLibraryFragments();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.card_view_library, myGamesMyLibraryFragments)
                .commit();

        binding.btnExploreGamesMyGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearchGames();
            }
        });
    }

    private void navigateToSearchGames() {
        NavController navController = appViewModel.getNavController().getValue();
        if (navController != null) {
           // navController.navigate(R.id.action_myGamesFragment_to_searchGamesFragment);
        }
    }
}