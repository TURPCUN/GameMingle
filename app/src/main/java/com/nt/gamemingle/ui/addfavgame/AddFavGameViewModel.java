package com.nt.gamemingle.ui.addfavgame;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.BoardGame;

public class AddFavGameViewModel {

    private AppViewModel appViewModel;
    public MutableLiveData<BoardGame> boardGameLiveData = new MutableLiveData<>();

    public AddFavGameViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void addFavGame(String gameId, String userId, boolean inLibrary, Context context) {
        appViewModel.databaseReference.child("USER_GAME").child(userId).child(gameId).child("inLibrary").setValue(inLibrary);
        Toast.makeText(context, "Game added to your library", Toast.LENGTH_SHORT).show();
    }

    public void getGameDetails(String gameId) {
        DatabaseReference gameReference = appViewModel.database.getReference("Games").child(gameId);
        gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gameName = snapshot.child("gameName").getValue(String.class);
                String gameDescription = snapshot.child("gameDescription").getValue(String.class);
                String gameMinPlayers = snapshot.child("minPlayer").getValue(String.class);
                String gameMaxPlayers = snapshot.child("maxPlayer").getValue(String.class);
                String gameCategory = snapshot.child("gameCategory").getValue(String.class);
                String gameImageUrl = snapshot.child("gameImageUrl").getValue(String.class);
                BoardGame game = new BoardGame(gameId, gameName, gameDescription, gameImageUrl, gameMinPlayers, gameMaxPlayers, gameCategory);
                boardGameLiveData.setValue(game);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddFavGameViewModel", "onCancelled: " + error.getMessage());
            }
        });
    }
}
