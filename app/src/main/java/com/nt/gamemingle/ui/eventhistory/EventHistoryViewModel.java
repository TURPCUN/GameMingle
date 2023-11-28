package com.nt.gamemingle.ui.eventhistory;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventHistoryViewModel {

    private AppViewModel appViewModel;

    public ArrayList<Event> eventsHistory = new ArrayList<>();

    MutableLiveData<Boolean> isEventsReceived = new MutableLiveData<>(false);

    public EventHistoryViewModel(AppViewModel appViewModel) {
        this.appViewModel = appViewModel;
    }

    public void getEventsHistory() {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<Event> eventHistoryList = new ArrayList<>();
            DatabaseReference userAttendEventRef = appViewModel.database.getReference("USER_ATTEND_EVENT");
            userAttendEventRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            String eventId = dataSnapshot.getKey();
                            DatabaseReference eventParticipantsRef = appViewModel.database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId);
                            eventParticipantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                                        if (isApproved != null) {
                                            if (isApproved) {
                                                DatabaseReference eventReference = appViewModel.database.getReference("EVENT").child(eventId);
                                                eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            String eventName = snapshot.child("eventName").getValue(String.class);
                                                            String eventDescription = snapshot.child("description").getValue(String.class);
                                                            String eventDate = snapshot.child("date").getValue(String.class);
                                                            String eventTime = snapshot.child("time").getValue(String.class);
                                                            String eventImgUrl = snapshot.child("imageUrl").getValue(String.class);
                                                            if( isEventPassed(eventDate, eventTime)){
                                                                String eventLocation = snapshot.child("location").getValue(String.class);
                                                                String eventGameId = snapshot.child("gameId").getValue(String.class);
                                                                String eventOwnerId = snapshot.child("ownerId").getValue(String.class);
                                                                int eventAttendeesCount = snapshot.child("approvedAttendeesCount").getValue(Integer.class);
                                                                Event event = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, eventOwnerId, eventGameId);
                                                                event.setEventAttendees(eventAttendeesCount);
                                                                event.setEventImageUrl(eventImgUrl);
                                                                DatabaseReference gameReference = appViewModel.database.getReference("Games").child(eventGameId);
                                                                gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()) {
                                                                            String gameName = snapshot.child("gameName").getValue(String.class);
                                                                            String eventMaxPlayers = snapshot.child("maxPlayer").getValue(String.class);
                                                                            String eventMinPlayers = snapshot.child("minPlayer").getValue(String.class);
                                                                            event.setEventMaxPlayers(eventMaxPlayers);
                                                                            event.setEventMinPlayers(eventMinPlayers);
                                                                            event.setEventGameName(gameName);

                                                                            DatabaseReference eventOwnerReference = appViewModel.database.getReference("Users").child(eventOwnerId);
                                                                            eventOwnerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    String ownerName = snapshot.child("userFullName").getValue(String.class);
                                                                                    event.setEventOwnerName(ownerName);
                                                                                    eventHistoryList.add(event);
                                                                                    eventsHistory = eventHistoryList;
                                                                                    try {
                                                                                        orderByEventDate(eventsHistory);
                                                                                    } catch (
                                                                                            ParseException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    isEventsReceived.setValue(true);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                                    Log.d("EventHistoryViewModel", "Error: " + error.getMessage());
                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        Log.d("EventHistoryViewModel", "Error: " + error.getMessage());
                                                                    }
                                                                });
                                                            }
                                                        }

                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.d("EventHistoryViewModel", "Error: " + error.getMessage());
                                                    }
                                                });

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("EventHistoryViewModel", "Error: " + error.getMessage());
                                }
                            });
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("EventHistoryViewModel", "Error: " + error.getMessage());
                }
            });
        }
    }
    private boolean isEventPassed(String eventDate, String eventTime){
        // eventDate format is "dd/MM/yyyy"
        // eventTime format is "HH:mm"
        // compare it today and return true if eventDate is previous than today else false

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date eventDateTime = dateFormat.parse(eventDate + " " + eventTime);
            Date currentDateTime = new Date(); // Current date and time

            if (eventDateTime.before(currentDateTime)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void orderByEventDate(ArrayList<Event> events) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (events.size() == 0 || events.size() == 1)
            return;
        for (int i = 0; i<events.size(); i++){
            for (int j = i+1; j<events.size(); j++){
                Date prevEventDate = dateFormat.parse(events.get(i).getEventDate() + " " + events.get(i).getEventTime());
                Date eventDateTime = dateFormat.parse(events.get(j).getEventDate() + " " + events.get(j).getEventTime());
                if (eventDateTime.after(prevEventDate)) {
                    Event tempEvent = events.get(i);
                    events.set(i, events.get(j));
                    events.set(j, tempEvent);
                }
            }
        }
    }

}
