package com.nt.gamemingle.ui.eventdetails;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;

public class EventDetailsViewModel {

    private AppViewModel appViewModel;
    MutableLiveData<Boolean> isUserApprovedForEvent = new MutableLiveData<>(false);

    MutableLiveData<String> user_EventStatus = new MutableLiveData<>("");
    public EventDetailsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void registerForEvent(String eventId) {
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).setValue(true);
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).child("isApproved").setValue(false);
    }

    public void cancelEvent(String eventId, String eventOwnerId) {
        if (appViewModel.mAuth.getCurrentUser().getUid().equals(eventOwnerId)) {
            appViewModel.databaseReference.child("Users").child(eventOwnerId).child("events").child(eventId).removeValue();
            appViewModel.databaseReference.child("EVENT").child(eventId).removeValue();
            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).removeValue();
        } else {
            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).removeValue();
            userEventStatus(eventId);
        }
    }

    public void isUserSendRequestForEvent(String eventId){
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference userEventsReference = appViewModel.database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId);
            userEventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                        if (isApproved != null) {
                            isUserApprovedForEvent.setValue(isApproved);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("EventDetailsViewModel : userEventStatus ", "Error: " + error.getMessage());
                }
            });
        }
    }
    public void userEventStatus(String eventId) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference userEventsReference = appViewModel.database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId);
            userEventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                        if (isApproved != null) {
                            if(isApproved){
                                user_EventStatus.setValue("approved");
                            } else {
                                user_EventStatus.setValue("pending");
                            }
                        }
                    } else {
                        user_EventStatus.setValue("not registered");
                        Log.d("EventDetailsViewModel : userEventStatus ", "Error: user is not registered");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("EventDetailsViewModel : userEventStatus ", "Error: " + error.getMessage());
                }
            });
        }
    }
}