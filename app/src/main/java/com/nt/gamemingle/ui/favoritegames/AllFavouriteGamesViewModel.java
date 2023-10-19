package com.nt.gamemingle.ui.favoritegames;

import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.BoardGame;

public class AllFavouriteGamesViewModel {

    private AppViewModel appViewModel;

    public AllFavouriteGamesViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void removeFavouriteGame(BoardGame boardGame) {
        appViewModel.databaseReference.child("USER_GAME").child(appViewModel.mAuth.getCurrentUser().getUid()).child(boardGame.getBoardGameId()).removeValue();
    }
}
