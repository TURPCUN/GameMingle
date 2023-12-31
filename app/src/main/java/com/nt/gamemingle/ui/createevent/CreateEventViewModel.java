package com.nt.gamemingle.ui.createevent;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.BoardGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateEventViewModel {

    private AppViewModel appViewModel;
    MutableLiveData<List<BoardGame>> boardGamesLiveDataCreateEvent = new MutableLiveData<>();

    public CreateEventViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void createEvent(String eventName, String eventLocation, String eventDescription, String selectedGame,
                            String formattedDate, String formattedTime, Context context, Uri mImageUri) {

        // EVENT
        UUID uuid = UUID.randomUUID();
        String eventUuid = uuid.toString();
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("eventName").setValue(eventName);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("location").setValue(eventLocation);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("description").setValue(eventDescription);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("gameId").setValue(selectedGame);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("date").setValue(formattedDate);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("time").setValue(formattedTime);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("ownerId").setValue(userId);
        appViewModel.databaseReference.child("EVENT").child(eventUuid).child("approvedAttendeesCount").setValue(1);

        appViewModel.databaseReference.child("Users").child(userId).child("events").child(eventUuid).setValue(true);

        if (mImageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("eventImages/" + eventUuid);
            storageReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            appViewModel.databaseReference.child("EVENT").child(eventUuid).child("imageUrl").setValue(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        Toast.makeText(context, "Event created successfully", Toast.LENGTH_SHORT).show();
    }

    public void getBoardGames(Context context) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if(userId != null){
            ArrayList<BoardGame> myGamesLibrary = new ArrayList<>();
            DatabaseReference userGamesReference = appViewModel.database.getReference("USER_GAME").child(userId);
            userGamesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                        String gameId = gameSnapshot.getKey();
                        Boolean inLibrary = gameSnapshot.child("inLibrary").getValue(Boolean.class);
                        if (gameId != null && inLibrary == true) {
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
                                        boardGamesLiveDataCreateEvent.setValue(myGamesLibrary);
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
