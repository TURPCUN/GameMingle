package com.nt.gamemingle.ui.requests;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.Requests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class RequestViewModel {

    MutableLiveData<List<Requests>> requestsLiveData = new MutableLiveData<>();
    AppViewModel appViewModel;

    public RequestViewModel(AppViewModel appViewModel) {
        this.appViewModel = appViewModel;
    }

    public void getRequests(){
        ArrayList<Requests> requestsArrayList = new ArrayList<>();
        DatabaseReference requestReference = appViewModel.databaseReference.child("Requests");
        requestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestsArrayList.clear();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    String requestId = requestSnapshot.getKey();
                    String requestSenderId = requestSnapshot.child("senderId").getValue(String.class);
                    String requestedGameName = requestSnapshot.child("requestedGameName").getValue(String.class);
                    String requestedGameDetails = requestSnapshot.child("requestedGameDetails").getValue(String.class);
                    Requests request = new Requests(requestId, requestSenderId, requestedGameName, requestedGameDetails);
                    requestsArrayList.add(request);
                    requestsLiveData.setValue(requestsArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void markAsDone(String requestId, String requestSenderId) {

        // send notification to the user
        UUID uuid = UUID.randomUUID();
        String notificationUuid = uuid.toString();
        appViewModel.databaseReference.child("NOTIFICATION").child(requestSenderId).child(notificationUuid).child("eventId").setValue(null);
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
