package com.nt.gamemingle.ui.searchgames;

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
import com.nt.gamemingle.model.Requests;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SearchGamesViewModel {
    private AppViewModel appViewModel;
    MutableLiveData<List<BoardGame>> boardGamesLiveData = new MutableLiveData<>();

    public SearchGamesViewModel(AppViewModel appViewModel) {
        this.appViewModel = appViewModel;
    }

    public void getUserFavoriteGames(Context context){
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<String> userFavoriteGamesIds = new ArrayList<>();
            HashMap<String,Boolean> userFavoriteGames = new HashMap<>(); //gameId, inLibrary
            DatabaseReference userGamesReference = appViewModel.database.getReference("USER_GAME").child(userId);
            userGamesReference.addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                        String gameId = gameSnapshot.getKey();
                        if (gameId != null) {
                            Boolean inLibrary = (Boolean) gameSnapshot.child("inLibrary").getValue();
                           // userFavoriteGamesIds.add(gameId);
                            userFavoriteGames.put(gameId, inLibrary);
                        }
                    }
                    getBoardGames(context, userFavoriteGames);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void getBoardGames(Context context, HashMap<String, Boolean> userFavoriteGames) {
        ArrayList<BoardGame> tempBoardGameList = new ArrayList<>();
        DatabaseReference gamesReference = appViewModel.database.getReference("Games");
        gamesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempBoardGameList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        HashMap<String, String> game = (HashMap<String, String>) dataSnapshot.getValue();
                        String gameId = dataSnapshot.getKey();
                        String gameName = game.get("gameName");
                        String gameDescription = game.get("gameDescription");
                        String gameImageUrl = game.get("gameImageUrl");
                        String gameMinPlayers = game.get("minPlayer").toString();
                        String gameMaxPlayers = game.get("maxPlayer").toString();
                        String gameCategory = game.get("gameCategory");
                        BoardGame boardGame = new BoardGame(gameId, gameName, gameDescription,
                                gameImageUrl, gameMinPlayers, gameMaxPlayers, gameCategory);
                        if (userFavoriteGames != null && userFavoriteGames.containsKey(gameId)) {
                            boardGame.setUserFavorite(true);
                            if(userFavoriteGames.get(gameId)){ // inLibrary true or false
                                boardGame.setInLibrary(true);
                            }
                        }
                        tempBoardGameList.add(boardGame);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                tempBoardGameList.sort((o1, o2) -> o1.getGameName().compareTo(o2.getGameName()));
                boardGamesLiveData.setValue(tempBoardGameList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestGame(String gameName, String gameDescription) {
        UUID uuid = UUID.randomUUID();
        String requestId = uuid.toString();
        appViewModel.databaseReference.child("Requests").child(requestId).setValue(requestId);
        appViewModel.databaseReference.child("Requests").child(requestId).child("senderId").setValue(appViewModel.mAuth.getCurrentUser().getUid());
        appViewModel.databaseReference.child("Requests").child(requestId).child("requestedGameName").setValue(gameName);
        appViewModel.databaseReference.child("Requests").child(requestId).child("requestedGameDetails").setValue(gameDescription);

    }
}
