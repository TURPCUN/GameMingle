package com.nt.gamemingle.ui.mylibrary;

import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.BoardGame;

public class AllMyLibraryViewModel {
    private AppViewModel appViewModel;

    public AllMyLibraryViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void removeMyLibraryGame(BoardGame boardGame) {
        appViewModel.databaseReference.child("USER_GAME").child(appViewModel.mAuth.getCurrentUser().getUid()).child(boardGame.getBoardGameId()).child("inLibrary").setValue(false);
    }
}
