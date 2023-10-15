package com.nt.gamemingle.ui.favoritegames;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.FavouriteGamesDetailsAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;

public class AllFavouriteGamesFragment extends BaseFragment {

    private AllFavouriteGamesViewModel mViewModel;
    ArrayList<BoardGame> favBoardGameList2;
    private FavouriteGamesDetailsAdapter favouriteGamesDetailsAdapter;
    RecyclerView recyclerFavGames2;
    NavController navController;

    @Override
    public void onResume() {
        super.onResume();
        favBoardGameList2 = getArguments().getParcelableArrayList("favBoardGameList");
        favouriteGamesDetailsAdapter = new FavouriteGamesDetailsAdapter(requireContext(), favBoardGameList2);
        recyclerFavGames2.setAdapter(favouriteGamesDetailsAdapter);
    }

    public AllFavouriteGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favBoardGameList2 = getArguments().getParcelableArrayList("favBoardGameList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_favorite_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        navController = appViewModel.getNavController().getValue();
        mViewModel = new AllFavouriteGamesViewModel(appViewModel);

        recyclerFavGames2 = getActivity().findViewById(R.id.recycler_all_fav_games);
        recyclerFavGames2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false  ));
        favouriteGamesDetailsAdapter = new FavouriteGamesDetailsAdapter(requireContext(), favBoardGameList2);
        recyclerFavGames2.setAdapter(favouriteGamesDetailsAdapter);
        favouriteGamesDetailsAdapter.notifyDataSetChanged();
        }
}