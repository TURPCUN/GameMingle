package com.nt.gamemingle.ui.eventdetails;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.User;

import java.util.ArrayList;

public class EventDetailsViewModel {

    private AppViewModel appViewModel;

    public ArrayList<User> attendeesList = new ArrayList<>();
    MutableLiveData<Boolean> isAttendeesReceived = new MutableLiveData<>(false);
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
            userEventStatus(eventId, eventOwnerId);
        }
    }

    public void userEventStatus(String eventId, String eventOwnerId) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            if (userId.equals(eventOwnerId)) {
                user_EventStatus.setValue("owner");
                return;
            }
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

    public void getEventAttendees(String eventId){
        DatabaseReference eventAttendeesReference = appViewModel.database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants");
        eventAttendeesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<User> attendeesTemp = new ArrayList<>();
                    for(DataSnapshot attendeeS : snapshot.getChildren()) {
                        String attendeeId = attendeeS.getKey();
                        DatabaseReference userReference = appViewModel.database.getReference("Users").child(attendeeId);
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                if (userSnapshot.exists()){
                                    String fullName = userSnapshot.child("userFullName").getValue(String.class);
                                    Boolean isApproved = attendeeS.child("isApproved").getValue(Boolean.class);
                                    if(isApproved== null){
                                        isApproved = false;
                                    }
                                    User attendee = new User(attendeeId, fullName, isApproved.toString());
                                    attendeesTemp.add(attendee);
                                    attendeesList = attendeesTemp;
                                    isAttendeesReceived.setValue(true);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
            }
        });
    }



    public void denyUser(String eventId, String userId) {
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        attendeesList.removeIf(user -> user.getUserId().equals(userId));
                        isAttendeesReceived.setValue(true);
                    } else {
                        Log.e("EventDetailsViewModel", "Error: " + task.getException().getMessage());
                    }
                });
    }

    public void approveUser(String eventId, String userId) {
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId).child("isApproved").setValue(true);
    }


}