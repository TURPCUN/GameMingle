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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchGamesViewModel {
    private AppViewModel appViewModel;
    MutableLiveData<List<BoardGame>> boardGamesLiveData = new MutableLiveData<>();

    public SearchGamesViewModel(AppViewModel appViewModel) {
        this.appViewModel = appViewModel;
    }

    public void getBoardGames(Context context) {
        ArrayList<BoardGame> tempBoardGameList = new ArrayList<>();
        DatabaseReference gamesReference = appViewModel.database.getReference("Games");
        gamesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
                    tempBoardGameList.add(boardGame);
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
}
