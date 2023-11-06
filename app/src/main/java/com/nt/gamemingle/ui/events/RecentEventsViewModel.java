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

import java.util.ArrayList;
import java.util.HashMap;

public class RecentEventsViewModel {

    private AppViewModel appViewModel;

    ArrayList<Event> recentEvents = new ArrayList<>();
    ArrayList<String> userFavoriteGames = new ArrayList<>();
    public MutableLiveData<Boolean> isRecentEventsReceived = new MutableLiveData<>(false);

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
                            String eventLocation = event.get("location");
                            Event eventObj = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, ownerId, eventGameId);
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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("RecentEventsViewModel", "Error getting recent events");
                }
            });
        }
    }


}
