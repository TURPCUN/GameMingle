package com.nt.gamemingle.ui.mygames;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentMyGamesBinding;
import com.nt.gamemingle.ui.common.BaseFragment;
import com.nt.gamemingle.ui.events.RecentEventsFragment;


public class MyGamesFragment extends BaseFragment {

    private MyGamesViewModel mViewModel;
    FragmentMyGamesBinding binding;
    public MyGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.checkFavouriteGame(requireContext());
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
        setBottomBarVisibility(true);
        appViewModel.upcomingEventsNotification();
        mViewModel = new MyGamesViewModel(appViewModel);
        mViewModel.checkFavouriteGame(requireContext());

        mViewModel.isAnyFavouriteGame.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.cardViewFavorites.setVisibility(View.VISIBLE);
                    binding.cardViewLibrary.setVisibility(View.VISIBLE);
                    binding.btnExploreGamesMyGames.setVisibility(View.VISIBLE);
                    binding.recentEventsFragmentContainerView.setVisibility(View.VISIBLE);
                    binding.myGamesEmptyFragmentContainerView.setVisibility(View.GONE);
                } else {
                    binding.myGamesEmptyFragmentContainerView.setVisibility(View.VISIBLE);
                }
            }
        });


        RecentEventsFragment recentEventsFragment = new RecentEventsFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.recent_events_fragment_container_view, recentEventsFragment)
                .commit();

        MyGamesFavouritesFragment myGamesFavouritesFragment = new MyGamesFavouritesFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.card_view_favorites, myGamesFavouritesFragment)
                .commit();

        MyGamesMyLibraryFragments myGamesMyLibraryFragments = new MyGamesMyLibraryFragments();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.card_view_library, myGamesMyLibraryFragments)
                .commit();

        MyGamesEmpty myGamesEmpty = new MyGamesEmpty();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.my_games_empty_fragment_container_view, myGamesEmpty)
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
           navController.navigate(R.id.action_myGamesFragment_to_searchGamesFragment);
        }
    }
}