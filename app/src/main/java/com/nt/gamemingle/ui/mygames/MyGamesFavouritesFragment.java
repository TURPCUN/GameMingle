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
import com.nt.gamemingle.adapters.FavouriteGamesAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyGamesFavouritesFragment extends BaseFragment implements FavouriteGamesAdapter.ItemClickListener{

    private MyGamesFavouritesFragmentViewModel mViewModel;

    ArrayList<BoardGame> favBoardGameList;
    private FavouriteGamesAdapter favouriteGamesAdapter;
    ArrayList<BoardGame> previewFavBoardGames = new ArrayList<>();
    RecyclerView recyclerFavGames;
    NavController navController;
    Button btnSeeAllFavGames;

    @Override
    public void onResume() {
        super.onResume();
        favouriteGamesAdapter = new FavouriteGamesAdapter(requireContext(), new ArrayList<BoardGame>(), this);
        recyclerFavGames.setAdapter(favouriteGamesAdapter);
        mViewModel.getPreviewFavBoardGames(requireContext());
    }

    public MyGamesFavouritesFragment() {
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

        mViewModel = new MyGamesFavouritesFragmentViewModel(appViewModel);

        mViewModel.getPreviewFavBoardGames(requireContext());
        recyclerFavGames = getActivity().findViewById(R.id.recycler_fav_games);
        recyclerFavGames.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false ));
        favouriteGamesAdapter = new FavouriteGamesAdapter(requireContext(), favBoardGameList, this);
        recyclerFavGames.setAdapter(favouriteGamesAdapter);

        final Observer<List<BoardGame>> isFavBoardGameLoaded = new Observer<List<BoardGame>>() {
            @Override
            public void onChanged(List<BoardGame> boardGames) {
                if(boardGames != null){
                    favBoardGameList = mViewModel.previewFavBoardGames.getValue();
                    if (favBoardGameList.size() == 4) {
                        previewFavBoardGames.clear();
                        previewFavBoardGames.addAll(favBoardGameList.subList(0, 4));
                        favouriteGamesAdapter.setFavouriteGamesList(previewFavBoardGames);
                    }
                    if(favBoardGameList.size() < 4){
                        previewFavBoardGames.clear();
                        previewFavBoardGames.addAll(favBoardGameList.subList(0, favBoardGameList.size()));
                        favouriteGamesAdapter.setFavouriteGamesList(previewFavBoardGames);
                    }
                }
            }
        };

        mViewModel.previewFavBoardGames.observe(getViewLifecycleOwner(), isFavBoardGameLoaded);

        btnSeeAllFavGames = getActivity().findViewById(R.id.btnSeeAll);
        btnSeeAllFavGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ArrayList<BoardGame> myList = (ArrayList<BoardGame>) favBoardGameList.clone();
                bundle.putParcelableArrayList("favBoardGameList", myList);
                navController.navigate(R.id.action_myGamesFragment_to_allFavouriteGamesFragment, bundle);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("game", favBoardGameList.get(position));
        bundle.putBoolean("hideSomeFields", true);
        navController.navigate(R.id.action_myGamesFragment_to_addFavGameFragment, bundle);
    }
}