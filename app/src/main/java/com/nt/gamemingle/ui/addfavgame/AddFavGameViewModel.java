package com.nt.gamemingle.ui.addfavgame;

import android.content.Context;
import android.widget.Toast;

import com.nt.gamemingle.app.AppViewModel;

import java.util.UUID;

public class AddFavGameViewModel {

    private AppViewModel appViewModel;

    public AddFavGameViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void addFavGame(String gameId, String userId, boolean inLibrary, Context context) {
        //appViewModel.databaseReference.child("Users").child(appViewModel.mAuth.getCurrentUser().getUid()).child("favGames").child(gameName).setValue(gameName);
        UUID uuid = UUID.randomUUID();
        String favGameId = uuid.toString();
        appViewModel.databaseReference.child("USER_GAME").child(favGameId).child("gameId").setValue(gameId);
        appViewModel.databaseReference.child("USER_GAME").child(favGameId).child("userId").setValue(userId);
        appViewModel.databaseReference.child("USER_GAME").child(favGameId).child("inLibrary").setValue(inLibrary);
        Toast.makeText(context, "Game added to your library", Toast.LENGTH_SHORT).show();
    }
}
