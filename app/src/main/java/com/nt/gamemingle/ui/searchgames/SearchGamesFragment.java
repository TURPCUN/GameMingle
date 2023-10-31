package com.nt.gamemingle.ui.searchgames;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.GameSearchAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchGamesFragment extends BaseFragment implements GameSearchAdapter.ItemClickListener {

    private SearchGamesViewModel mViewModel;
    private List<BoardGame> boardGameList = new ArrayList<>();
    private GameSearchAdapter gameSearchAdapter;
    private SearchView searchView;
    RecyclerView recyclerSearchGames;
    List<BoardGame> filteredList = new ArrayList<>();

    NavController navController;


    @Override
    public void onResume() {
        super.onResume();
        gameSearchAdapter = new GameSearchAdapter(requireContext(), new ArrayList<BoardGame>(), this);
        recyclerSearchGames.setAdapter(gameSearchAdapter);
        searchView.setQuery("", false);
        searchView.clearFocus();
        boardGameList.clear();
        mViewModel.getBoardGames(requireContext());
    }

    public SearchGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        navController = appViewModel.getNavController().getValue();

        mViewModel = new SearchGamesViewModel(appViewModel);

        mViewModel.getBoardGames(requireContext());
        recyclerSearchGames = getActivity().findViewById(R.id.recyclerSearchGames);
        recyclerSearchGames.setLayoutManager(new LinearLayoutManager(getActivity()));
        gameSearchAdapter = new GameSearchAdapter(getActivity(), boardGameList, this);
        recyclerSearchGames.setAdapter(gameSearchAdapter);

        searchView = getActivity().findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        final Observer<List<BoardGame>> isBoardGameLoaded = new Observer<List<BoardGame>>() {

            @Override
            public void onChanged(List<BoardGame> boardGamesLiveData) {
                if (boardGamesLiveData != null) {
                    boardGameList = mViewModel.boardGamesLiveData.getValue();
                    filteredList = boardGameList;
                    gameSearchAdapter.setFilteredList(boardGameList);
                    gameSearchAdapter.notifyDataSetChanged();
                }
            }
        };
        mViewModel.boardGamesLiveData.observe(getViewLifecycleOwner(), isBoardGameLoaded);
    }

    private void filterList(String text) {
        filteredList.clear();
        if(text.equals("")){
            gameSearchAdapter.setFilteredList(boardGameList);
        } else {
            for(BoardGame game : boardGameList){
                if(game.getGameName().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(game);
                }
            }
            if(!filteredList.isEmpty()){
                gameSearchAdapter.setFilteredList(filteredList);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_games, container, false);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(filteredList.size() >= position && filteredList.size() != 0){
            Bundle bundle = new Bundle();
            bundle.putParcelable("game", filteredList.get(position));
            navController.navigate(R.id.action_searchGamesFragment_to_addFavGameFragment, bundle);
        }
    }
}