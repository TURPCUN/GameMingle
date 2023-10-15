package com.nt.gamemingle.ui.addfavgame;

import android.content.Context;
import android.widget.Toast;

import com.nt.gamemingle.app.AppViewModel;

public class AddFavGameViewModel {

    private AppViewModel appViewModel;

    public AddFavGameViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void addFavGame(String gameId, String userId, boolean inLibrary, Context context) {
        appViewModel.databaseReference.child("USER_GAME").child(userId).child(gameId).child("inLibrary").setValue(inLibrary);
        Toast.makeText(context, "Game added to your library", Toast.LENGTH_SHORT).show();
    }
}
