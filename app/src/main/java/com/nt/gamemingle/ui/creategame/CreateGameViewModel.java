package com.nt.gamemingle.ui.creategame;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.Requests;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreateGameViewModel {

    private AppViewModel appViewModel;

    public CreateGameViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public String addGame(String gameName, String gameCategory, String gameDescription, String gameMinPlayers, String gameMaxPlayers, Uri gameImageUrl, Context context) {
        UUID uuid = UUID.randomUUID();
        String gameId = uuid.toString();
        appViewModel.databaseReference.child("Games").child(gameId).child("gameName").setValue(gameName);
        appViewModel.databaseReference.child("Games").child(gameId).child("gameDescription").setValue(gameDescription);
        appViewModel.databaseReference.child("Games").child(gameId).child("minPlayer").setValue(gameMinPlayers);
        appViewModel.databaseReference.child("Games").child(gameId).child("maxPlayer").setValue(gameMaxPlayers);
        appViewModel.databaseReference.child("Games").child(gameId).child("gameCategory").setValue(gameCategory);

        if (gameImageUrl != null) {
            //appViewModel.databaseReference.child("GAME").child(gameId).child("gameImageUrl").setValue(gameImageUrl.toString());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("gameImages/" + gameId);
            storageReference.putFile(gameImageUrl)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            appViewModel.databaseReference.child("Games").child(gameId).child("gameImageUrl").setValue(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }
        Toast.makeText(context, "Game added successfully", Toast.LENGTH_SHORT).show();
        return gameId;
    }

    public void deleteRequest(Requests requestedGame, String addedGameId) {
        markRequestAsDone(addedGameId, requestedGame.getRequestId(), requestedGame.getRequestSenderId());
    }

    public void markRequestAsDone(String addedGameId, String requestId, String requestSenderId) {

        // send notification to the user
        UUID uuid = UUID.randomUUID();
        String notificationUuid = uuid.toString();
        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("eventId").setValue(null);
        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("gameId").setValue(addedGameId);
        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("message").setValue("The game you requested has been added to our application.");
        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("isRead").setValue(false);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("time").setValue(formattedTime);
        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("date").setValue(formattedDate);


        DatabaseReference requestReference = appViewModel.databaseReference.child("Requests").child(requestId);
        requestReference.removeValue();

    }
}
