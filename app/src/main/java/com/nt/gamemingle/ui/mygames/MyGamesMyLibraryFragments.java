package com.nt.gamemingle.ui.mygames;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.MyGamesLibraryAdapter;
import com.nt.gamemingle.databinding.FragmentMyGamesMyLibraryFragmentsBinding;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyGamesMyLibraryFragments extends BaseFragment {

    private MyGamesMyLibraryFragmentsViewModel mViewModel;
    ArrayList<BoardGame> myBoardGamesList;
    private MyGamesLibraryAdapter gamesAdapter;
    RecyclerView recyclerMyGames;
    NavController navController;
    Button btnSeeAllMyGames;
    FragmentMyGamesMyLibraryFragmentsBinding binding;
    ArrayList<BoardGame> emptyList = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();
        gamesAdapter = new MyGamesLibraryAdapter(requireContext(), new ArrayList<BoardGame>());
        recyclerMyGames.setAdapter(gamesAdapter);
        mViewModel.getPreviewMyLibraryBoardGames(requireContext());
    }

    public MyGamesMyLibraryFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding = FragmentMyGamesMyLibraryFragmentsBinding.inflate(inflater, container, false);
        //return binding.getRoot();
        return inflater.inflate(R.layout.fragment_my_games_my_library_fragments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = appViewModel.getNavController().getValue();
        mViewModel = new MyGamesMyLibraryFragmentsViewModel(appViewModel);

        mViewModel.getPreviewMyLibraryBoardGames(requireContext());
        recyclerMyGames = getActivity().findViewById(R.id.recycler_my_library);
        recyclerMyGames.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        gamesAdapter = new MyGamesLibraryAdapter(requireContext(), myBoardGamesList);
        recyclerMyGames.setAdapter(gamesAdapter);

        mViewModel.isAnyLibraryGame.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    btnSeeAllMyGames.setVisibility(View.GONE);
                    getActivity().findViewById(R.id.txtMyLibrary).setVisibility(View.GONE);
                }
            }
        });
        final Observer<List<BoardGame>> isLibraryBoardGamesLoaded = new Observer<List<BoardGame>>() {
            @Override
            public void onChanged(List<BoardGame> boardGames) {
                if (boardGames != null) {
                    btnSeeAllMyGames.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.txtMyLibrary).setVisibility(View.VISIBLE);
                    myBoardGamesList = mViewModel.previewMyLibraryBoardGames.getValue();
                    gamesAdapter.setLibraryGamesList(myBoardGamesList);
                }
            }
        };

        mViewModel.previewMyLibraryBoardGames.observe(getViewLifecycleOwner(), isLibraryBoardGamesLoaded);

        btnSeeAllMyGames = getActivity().findViewById(R.id.btnSeeAllLib);
        btnSeeAllMyGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("myLibraryBoardGameList", myBoardGamesList);
                navController.navigate(R.id.action_myGamesFragment_to_allMyLibraryFragment, bundle);
            }
        });
    }
}