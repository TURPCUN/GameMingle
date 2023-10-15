package com.nt.gamemingle.ui.mygames;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.FavoriteGamesAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyGamesFavoritesFragment extends BaseFragment {

    private MyGamesFavoritesFragmentViewModel mViewModel;

    ArrayList<BoardGame> favBoardGameList;
    private FavoriteGamesAdapter favoriteGamesAdapter;
    RecyclerView recyclerFavGames;
    NavController navController;
    Button btnSeeAllFavGames;

    @Override
    public void onResume() {
        super.onResume();
        favoriteGamesAdapter = new FavoriteGamesAdapter(requireContext(), new ArrayList<BoardGame>());
        recyclerFavGames.setAdapter(favoriteGamesAdapter);
        mViewModel.getPreviewFavBoardGames(requireContext());
    }

    public MyGamesFavoritesFragment() {
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
        return inflater.inflate(R.layout.fragment_my_games_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = appViewModel.getNavController().getValue();

        mViewModel = new MyGamesFavoritesFragmentViewModel(appViewModel);

        mViewModel.getPreviewFavBoardGames(requireContext());
        recyclerFavGames = getActivity().findViewById(R.id.recycler_fav_games);
        recyclerFavGames.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false  ));
        favoriteGamesAdapter = new FavoriteGamesAdapter(requireContext(), favBoardGameList);
        recyclerFavGames.setAdapter(favoriteGamesAdapter);

        final Observer<List<BoardGame>> isFavBoardGameLoaded = new Observer<List<BoardGame>>() {
            @Override
            public void onChanged(List<BoardGame> boardGames) {
                if(boardGames != null){
                    favBoardGameList = mViewModel.previewFavBoardGames.getValue();
                    favoriteGamesAdapter.setFavouriteGamesList(favBoardGameList);
                }
            }
        };

        mViewModel.previewFavBoardGames.observe(getViewLifecycleOwner(), isFavBoardGameLoaded);

        btnSeeAllFavGames = getActivity().findViewById(R.id.btnSeeAll);
        btnSeeAllFavGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("favBoardGameList", favBoardGameList);
                navController.navigate(R.id.action_myGamesFragment_to_allFavoriteGamesFragment, bundle);
            }
        });
    }
}