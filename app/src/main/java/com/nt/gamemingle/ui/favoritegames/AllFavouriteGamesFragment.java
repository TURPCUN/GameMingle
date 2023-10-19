package com.nt.gamemingle.ui.favoritegames;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.FavouriteGamesSeeAllAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;

public class AllFavouriteGamesFragment extends BaseFragment {

    private AllFavouriteGamesViewModel mViewModel;
    ArrayList<BoardGame> favBoardGameList2;
    private FavouriteGamesSeeAllAdapter favouriteGamesSeeAllAdapter;
    RecyclerView recyclerFavGames2;
    NavController navController;


    @Override
    public void onResume() {
        super.onResume();
        favBoardGameList2 = getArguments().getParcelableArrayList("favBoardGameList");
        favouriteGamesSeeAllAdapter = new FavouriteGamesSeeAllAdapter(requireContext(), favBoardGameList2);
        recyclerFavGames2.setAdapter(favouriteGamesSeeAllAdapter);
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
        favouriteGamesSeeAllAdapter = new FavouriteGamesSeeAllAdapter(requireContext(), favBoardGameList2);
        recyclerFavGames2.setAdapter(favouriteGamesSeeAllAdapter);
        favouriteGamesSeeAllAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

                    @Override
                    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return 0.1f;
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        if ( direction == ItemTouchHelper.LEFT ) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Remove from Favourites");
                            builder.setMessage("Are you sure you want to remove this game from your favourites?");
                            builder.setPositiveButton("Yes", (dialog, which) -> {
                                mViewModel.removeFavouriteGame(favBoardGameList2.get(position));
                                favBoardGameList2.remove(position);
                                favouriteGamesSeeAllAdapter.setFavouriteGamesList(favBoardGameList2);
                            });
                            builder.setNegativeButton("No", (dialog, which) -> {
                                favouriteGamesSeeAllAdapter.setFavouriteGamesList(favBoardGameList2);
                                dialog.dismiss();
                            });
                            builder.show();


                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerFavGames2);

    }
}