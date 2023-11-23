package com.nt.gamemingle.ui.events;

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
import java.util.HashMap;

public class RecentEventsViewModel {

    private AppViewModel appViewModel;

    ArrayList<Event> recentEvents = new ArrayList<>();
    ArrayList<String> userFavoriteGames = new ArrayList<>();
    public MutableLiveData<Boolean> isRecentEventsReceived = new MutableLiveData<>(false);
    MutableLiveData<Integer> approvedEventAttendeesCount = new MutableLiveData<>(0);

    public RecentEventsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void getRecommendedRecentEvents() {
        // Get user favorite games
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference userGamesReference = appViewModel.database.getReference("USER_GAME").child(userId);
            userGamesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                        String gameId = gameSnapshot.getKey();
                        if (gameId != null) {
                            userFavoriteGames.add(gameId);
                        }
                    }
                    // then get recent events which match user favorite games
                    getRecentEvents();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("RecentEventsViewModel", "Error getting user favorite games");
                }
            });
        }
    }

    private void getRecentEvents() {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<Event> recentEventsTemp = new ArrayList<>();
            DatabaseReference eventsReference = appViewModel.database.getReference("EVENT");
            eventsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot eventSnapshot) {
                    if (eventSnapshot.exists()){
                        for(DataSnapshot eventS : eventSnapshot.getChildren()) {
                            HashMap<String, String> event = (HashMap<String, String>) eventS.getValue();
                            String ownerId = event.get("ownerId");
                            if(ownerId == null || userId == null) {
                                return;
                            }
                            if(ownerId.equals(userId)) {
                                continue;
                            }
                            String eventGameId = event.get("gameId");
                            if(!userFavoriteGames.contains(eventGameId)) {
                                continue;
                            }
                            String eventId = eventS.getKey();
                            String eventName = event.get("eventName");
                            String eventDescription = event.get("description");
                            String eventDate = event.get("date");
                            String eventTime = event.get("time");
                            if(isEventPassed(eventDate, eventTime)) {
                                continue;
                            }
                            String eventLocation = event.get("location");
                            int eventAttendeesCount = eventS.child("approvedAttendeesCount").getValue(Integer.class);
                            Event eventObj = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, ownerId, eventGameId);
                            eventObj.setEventAttendees(eventAttendeesCount);
                            DatabaseReference gameReference = appViewModel.database.getReference("Games").child(eventGameId);
                            gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot gameSnapshot) {
                                    if (gameSnapshot.exists()) {
                                        String gameName = gameSnapshot.child("gameName").getValue(String.class);
                                        String eventMaxPlayers = gameSnapshot.child("maxPlayer").getValue(String.class);
                                        String eventMinPlayers = gameSnapshot.child("minPlayer").getValue(String.class);
                                        eventObj.setEventGameName(gameName);
                                        eventObj.setEventMaxPlayers(eventMaxPlayers);
                                        eventObj.setEventMinPlayers(eventMinPlayers);

                                        DatabaseReference eventOwnerReference = appViewModel.database.getReference("Users").child(ownerId);
                                        eventOwnerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String ownerName = snapshot.child("userFullName").getValue(String.class);
                                                eventObj.setEventOwnerName(ownerName);
                                                recentEventsTemp.add(eventObj);
                                                recentEvents = recentEventsTemp;
                                                try {
                                                    orderByEventDate(recentEvents);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                isRecentEventsReceived.setValue(true);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("RecentEventsViewModel", "Error: " + error.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("RecentEventsViewModel", "Error getting recent events game details");
                                }
                            });
                        }
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
                            if (eventDateTime.before(prevEventDate)) {
                                Event tempEvent = events.get(i);
                                events.set(i, events.get(j));
                                events.set(j, tempEvent);
                            }
                        }
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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("RecentEventsViewModel", "Error getting recent events");
                }
            });
        }
    }


}
