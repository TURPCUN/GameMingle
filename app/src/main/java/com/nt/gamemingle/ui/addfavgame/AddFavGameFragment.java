package com.nt.gamemingle.ui.addfavgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.nt.gamemingle.R;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;


public class AddFavGameFragment extends BaseFragment {

    private AddFavGameViewModel mViewModel;
    private TextView titleGame;
    private Button btnAddFavGame;

    private ImageView imageView;
    NavController navController;
    BoardGame boardGame;

    String requestedGameId;
    Boolean isComingFromFavOrLibrary = false; // bundle : hideSomeFields
    public AddFavGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        boardGame = getArguments().getParcelable("game");
        if (getArguments().getBoolean("hideSomeFields")){
            isComingFromFavOrLibrary = getArguments().getBoolean("hideSomeFields");
        }
        requestedGameId = getArguments().getString("gameId");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardGame = getArguments().getParcelable("game");
        if (getArguments().getBoolean("hideSomeFields")){
            isComingFromFavOrLibrary = getArguments().getBoolean("hideSomeFields");
        }
        requestedGameId = getArguments().getString("gameId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_fav_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        mViewModel = new AddFavGameViewModel(appViewModel);
        navController = appViewModel.getNavController().getValue();

        if (requestedGameId != null){
            mViewModel.getGameDetails(requestedGameId);
            mViewModel.boardGameLiveData.observe(getViewLifecycleOwner(), boardGame -> {
                this.boardGame = boardGame;
                setGameDetails();
            });
        } else {
            setGameDetails();
        }

    }

    private void setGameDetails() {
        if(boardGame != null){
            titleGame = getActivity().findViewById(R.id.titleGame);
            titleGame.setText(boardGame.getGameName());

            imageView = getActivity().findViewById(R.id.ImgGame);
            Glide.with(imageView.getContext())
                    .load(boardGame.getGameImageUrl())
                    .placeholder(R.drawable.loading_gif)
                    .fitCenter()
                    .centerCrop()
                    .error(R.drawable.icon)
                    .into(imageView);

            if (boardGame.getUserFavorite() != null && boardGame.getUserFavorite()) {
                ImageView imgFav = getActivity().findViewById(R.id.imgStarFav);
                imgFav.setVisibility(View.VISIBLE);
            }

            if (boardGame.getInLibrary() != null && boardGame.getInLibrary()) {
                MaterialCheckBox checkBox = getActivity().findViewById(R.id.checkBoxAddFavGame);
                checkBox.setVisibility(View.INVISIBLE);
            }

            if(boardGame.getUserFavorite() != null && boardGame.getUserFavorite() && boardGame.getInLibrary() != null && boardGame.getInLibrary()){
                Button btnAddFavGame = getActivity().findViewById(R.id.btn_add_fav_game);
                btnAddFavGame.setVisibility(View.INVISIBLE);
            }

            if (isComingFromFavOrLibrary){
                MaterialCheckBox checkBox = getActivity().findViewById(R.id.checkBoxAddFavGame);
                checkBox.setVisibility(View.INVISIBLE);
                ImageView imgFav = getActivity().findViewById(R.id.imgStarFav);
                imgFav.setVisibility(View.INVISIBLE);
                Button btnAddFavGame = getActivity().findViewById(R.id.btn_add_fav_game);
                btnAddFavGame.setVisibility(View.INVISIBLE);
            }

            TextView gamePlayers = getActivity().findViewById(R.id.gamePlayers);
            String gamePlayersText = boardGame.getGameMinPlayers() + "-" + boardGame.getGameMaxPlayers();
            gamePlayers.setText(gamePlayersText);

            TextView gameCategory = getActivity().findViewById(R.id.gameCategory);
            gameCategory.setText(boardGame.getGameCategory());

            TextView gameDescription = getActivity().findViewById(R.id.gameDescription);
            gameDescription.setText(boardGame.getGameDescription());

            btnAddFavGame = getActivity().findViewById(R.id.btn_add_fav_game);
            btnAddFavGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialCheckBox checkBox = getActivity().findViewById(R.id.checkBoxAddFavGame);
                    addFavGameForUser(boardGame.getBoardGameId(), appViewModel.mAuth.getCurrentUser().getUid(), checkBox.isChecked());
                }
            });
        }
    }

    private void addFavGameForUser(String gameID, String userID, boolean inLibrary){
        mViewModel.addFavGame(gameID, userID, inLibrary, getContext());

    }
}