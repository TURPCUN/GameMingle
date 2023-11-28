package com.nt.gamemingle.ui.eventdetails;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class EventDetailsViewModel {

    private AppViewModel appViewModel;
    MutableLiveData<Event> mutableEvent = new MutableLiveData<>();
    public ArrayList<User> attendeesList = new ArrayList<>();
    MutableLiveData<Boolean> isAttendeesReceived = new MutableLiveData<>(false);
    MutableLiveData<Integer> approvedEventAttendeesCount = new MutableLiveData<>(1);
    MutableLiveData<String> user_EventStatus = new MutableLiveData<>("");
    public EventDetailsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void registerForEvent(String eventId, String eventName) {
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).setValue(true);
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).child("isApproved").setValue(false);

        // Prepare a notification for the user
        UUID uuid = UUID.randomUUID();
        String notificationUuid = uuid.toString();
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("eventId").setValue(eventId);
        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("message").setValue("You registered for the " + eventName + " event!");
        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("isRead").setValue(false);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("time").setValue(formattedTime);
        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("date").setValue(formattedDate);

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

    private void cancelEventForUser(String eventId, String userId, String eventOwnerId, String eventName) {
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

                                            // Prepare a notification for the user
                                            UUID uuid = UUID.randomUUID();
                                            String notificationUuid = uuid.toString();
                                            appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("eventId").setValue(eventId);
                                            appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("message").setValue("You cancelled your registration of the  " + eventName + " event.");
                                            appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("isRead").setValue(false);

                                            Calendar calendar = Calendar.getInstance();
                                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                            int minute = calendar.get(Calendar.MINUTE);
                                            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                                            int month = calendar.get(Calendar.MONTH) + 1;
                                            int year = calendar.get(Calendar.YEAR);
                                            String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

                                            appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("time").setValue(formattedTime);
                                            appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("date").setValue(formattedDate);


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

    public void cancelEvent(String eventId, String eventOwnerId, String eventName) {
        // owner cancels event
        if (appViewModel.mAuth.getCurrentUser().getUid().equals(eventOwnerId)) {
            appViewModel.databaseReference.child("Users").child(eventOwnerId).child("events").child(eventId).removeValue();
            appViewModel.databaseReference.child("EVENT").child(eventId).removeValue();
            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).removeValue();

            // Prepare a notification for the user
            UUID uuid = UUID.randomUUID();
            String notificationUuid = uuid.toString();
            appViewModel.databaseReference.child("NOTIFICATION").child(eventOwnerId).child(notificationUuid).child("eventId").setValue(eventId);
            appViewModel.databaseReference.child("NOTIFICATION").child(eventOwnerId).child(notificationUuid).child("message").setValue("You cancelled the " + eventName + " event.");
            appViewModel.databaseReference.child("NOTIFICATION").child(eventOwnerId).child(notificationUuid).child("isRead").setValue(false);

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

            appViewModel.databaseReference.child("NOTIFICATION").child(eventOwnerId).child(notificationUuid).child("time").setValue(formattedTime);
            appViewModel.databaseReference.child("NOTIFICATION").child(eventOwnerId).child(notificationUuid).child("date").setValue(formattedDate);


        } else { // user cancels event registration
            cancelEventForUser(eventId, appViewModel.mAuth.getCurrentUser().getUid(), eventOwnerId, eventName);
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
                                    String userProfileImageUrl = userSnapshot.child("profileImageUrl").getValue(String.class);
                                    User attendee = new User(attendeeId, fullName, isApproved.toString());
                                    attendee.setUserProfileImageUrl(userProfileImageUrl);
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

    public void approveUser(String eventId, String userId, String eventName) {
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
                        approvedEventAttendeesCount.setValue(approvedAttendeesCount);

                        // Prepare a notification for the user
                        UUID uuid = UUID.randomUUID();
                        String notificationUuid = uuid.toString();
                        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("eventId").setValue(eventId);
                        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("message").setValue("You have been approved for the " + eventName + " event! Let's check the details.");
                        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("isRead").setValue(false);

                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int year = calendar.get(Calendar.YEAR);
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

                        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("time").setValue(formattedTime);
                        appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationUuid).child("date").setValue(formattedDate);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
            }
        });

    }


    public void getEvent(String eventIdFromNotification) {
        DatabaseReference eventReference = appViewModel.database.getReference("EVENT").child(eventIdFromNotification);
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String eventName = snapshot.child("eventName").getValue(String.class);
                    String eventDescription = snapshot.child("description").getValue(String.class);
                    String eventDate = snapshot.child("date").getValue(String.class);
                    String eventTime = snapshot.child("time").getValue(String.class);
                    String eventLocation = snapshot.child("location").getValue(String.class);
                    String eventOwnerId = snapshot.child("ownerId").getValue(String.class);
                    String eventGameId = snapshot.child("gameId").getValue(String.class);
                    String eventImageUrl = snapshot.child("imageUrl").getValue(String.class);
                    int eventAttendeesCount = snapshot.child("approvedAttendeesCount").getValue(Integer.class);
                    Event event = new Event(eventIdFromNotification, eventName, eventDescription, eventDate, eventTime, eventLocation, eventOwnerId, eventGameId);
                    event.setEventAttendees(eventAttendeesCount);
                    event.setEventImageUrl(eventImageUrl);
                    DatabaseReference gameReference = appViewModel.database.getReference("Games").child(eventGameId);
                    gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot gameSnapshot) {
                            if (gameSnapshot.exists()){
                                String gameName = gameSnapshot.child("gameName").getValue(String.class);
                                String maxPlayers = gameSnapshot.child("maxPlayer").getValue(String.class);
                                String minPlayers = gameSnapshot.child("minPlayer").getValue(String.class);
                                event.setEventMaxPlayers(maxPlayers);
                                event.setEventMinPlayers(minPlayers);
                                event.setEventGameName(gameName);
                                mutableEvent.setValue(event);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EventDetailsViewModel", "Error: " + error.getMessage());
            }
        });

    }

    public void uploadImage(Uri data, String eventId) {
        if (data != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("eventImages/" + eventId);
            storageReference.putFile(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            appViewModel.databaseReference.child("EVENT").child(eventId).child("imageUrl").setValue(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.d("EventDetailsViewModel", "Error: " + e.getMessage());
                    });
        }
    }
}