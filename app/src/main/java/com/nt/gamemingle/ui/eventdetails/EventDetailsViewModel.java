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
    MutableLiveData<Integer> approvedEventAttendeesCount = new MutableLiveData<>(1);
    MutableLiveData<String> user_EventStatus = new MutableLiveData<>("");
    public EventDetailsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void registerForEvent(String eventId) {
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).setValue(true);
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).child("isApproved").setValue(false);
    }

    private boolean isUserApproved(String eventId, String userId) {
        DatabaseReference userEventsReference = appViewModel.database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId);
        ArrayList<Boolean> isUserApproved = new ArrayList<>();
        userEventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                    if (isApproved != null) {
                        if(isApproved){
                            isUserApproved.add(true);
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
        return isUserApproved.size() > 0;
    }

    private void cancelEventForUser(String eventId, String userId, String eventOwnerId) {
        DatabaseReference userEventsReference = appViewModel.database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId);
        userEventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                    if (isApproved != null) {
                        if(isApproved){
                            DatabaseReference eventReference = appViewModel.database.getReference("EVENT").child(eventId).child("approvedAttendeesCount");
                            eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Integer approvedAttendeesCount = snapshot.getValue(Integer.class);
                                        if (approvedAttendeesCount != null) {
                                            approvedAttendeesCount--;
                                            appViewModel.databaseReference.child("EVENT").child(eventId).child("approvedAttendeesCount").setValue(approvedAttendeesCount);
                                            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).removeValue();
                                            approvedEventAttendeesCount.setValue(approvedAttendeesCount);
                                            userEventStatus(eventId, eventOwnerId);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
                                }
                            });
                        }
                        else{
                            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).removeValue();
                            userEventStatus(eventId, eventOwnerId);
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
        });    }

    public void cancelEvent(String eventId, String eventOwnerId) {
        // owner cancels event
        if (appViewModel.mAuth.getCurrentUser().getUid().equals(eventOwnerId)) {
            appViewModel.databaseReference.child("Users").child(eventOwnerId).child("events").child(eventId).removeValue();
            appViewModel.databaseReference.child("EVENT").child(eventId).removeValue();
            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).removeValue();
        } else { // user cancels event registration
            cancelEventForUser(eventId, appViewModel.mAuth.getCurrentUser().getUid(), eventOwnerId);
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
                    ArrayList<User> approvedAttendeesTemp = new ArrayList<>();
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
                                    if (isApproved) {
                                        approvedAttendeesTemp.add(attendee);
                                    }
                                    attendeesTemp.add(attendee);
                                    attendeesList = attendeesTemp;
                                    isAttendeesReceived.setValue(true);
                                    approvedEventAttendeesCount.setValue(approvedAttendeesTemp.size() + 1);
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

        DatabaseReference eventReference = appViewModel.database.getReference("EVENT").child(eventId).child("approvedAttendeesCount");
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer approvedAttendeesCount = snapshot.getValue(Integer.class);
                    if (approvedAttendeesCount != null) {
                        approvedAttendeesCount = approvedAttendeesCount + 1;
                        appViewModel.databaseReference.child("EVENT").child(eventId).child("approvedAttendeesCount").setValue(approvedAttendeesCount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
            }
        });

    }


}