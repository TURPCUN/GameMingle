package com.nt.gamemingle.ui.mygames;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.BoardGame;

import java.util.ArrayList;

public class MyGamesMyLibraryFragmentsViewModel {

    private AppViewModel appViewModel;

    public MutableLiveData<ArrayList<BoardGame>> previewMyLibraryBoardGames = new MutableLiveData<>();

    public MyGamesMyLibraryFragmentsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void getPreviewMyLibraryBoardGames(Context context) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if(userId != null){
            ArrayList<BoardGame> myGamesLibrary = new ArrayList<>();
            DatabaseReference userGamesReference = appViewModel.database.getReference("USER_GAME").child(userId);
            userGamesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                        String gameId = gameSnapshot.getKey();
                        //String inLibrary = gameSnapshot.child("inLibrary").getValue(String.class);
                        if (gameId != null){
                            DatabaseReference gamesReference = appViewModel.database.getReference("Games").child(gameId);
                            gamesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot gameSnapshot) {
                                    if (gameSnapshot.exists()) {
                                        String gameName = gameSnapshot.child("gameName").getValue(String.class);
                                        String gameDescription = gameSnapshot.child("gameDescription").getValue(String.class);
                                        String gameImageUrl = gameSnapshot.child("gameImageUrl").getValue(String.class);
                                        String gameMinPlayers = gameSnapshot.child("minPlayer").getValue(String.class);
                                        String gameMaxPlayers = gameSnapshot.child("maxPlayer").getValue(String.class);
                                        String gameCategory = gameSnapshot.child("gameCategory").getValue(String.class);

                                        BoardGame boardGame = new BoardGame(gameId, gameName, gameDescription, gameImageUrl, gameMinPlayers, gameMaxPlayers, gameCategory);
                                        myGamesLibrary.add(boardGame);
                                        previewMyLibraryBoardGames.setValue(myGamesLibrary);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
