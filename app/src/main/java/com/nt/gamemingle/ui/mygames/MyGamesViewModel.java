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

public class MyGamesViewModel {

    private AppViewModel appViewModel;

    public MutableLiveData<Boolean> isAnyFavouriteGame = new MutableLiveData<>(false);

    public MyGamesViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void checkFavouriteGame(Context context) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<BoardGame> favouriteGames = new ArrayList<>();
            DatabaseReference userGamesReference = appViewModel.database.getReference("USER_GAME").child(userId);
            userGamesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                        String gameId = gameSnapshot.getKey();
                        if (gameId != null) {
                            isAnyFavouriteGame.setValue(true);
                            return;
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
