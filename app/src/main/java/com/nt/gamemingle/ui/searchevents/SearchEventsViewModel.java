package com.nt.gamemingle.ui.searchevents;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import java.util.List;

public class SearchEventsViewModel {
    private AppViewModel appViewModel;
    public ArrayList<Event> allEvents = new ArrayList<>();
    MutableLiveData<Boolean> isEventsReceived = new MutableLiveData<>(false);
    public SearchEventsViewModel(AppViewModel appViewModel) {
        this.appViewModel = appViewModel;
    }

    public void getEvents(Context context) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<Event> myEventsTemp = new ArrayList<>();
            DatabaseReference eventReference = appViewModel.database.getReference("EVENT");
            eventReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot eventSnapshot) {
                    if (eventSnapshot.exists()){
                        for(DataSnapshot eventS : eventSnapshot.getChildren()) {
                            HashMap<String, String> event = (HashMap<String, String>) eventS.getValue();
                            String ownerId = event.get("ownerId");
                            if(ownerId == null || userId == null) {
                                Log.d("SearchEventsViewModel", "ownerId or userId is null");
                                return;
                            }
                            if(ownerId.equals(userId)) {
                                System.out.println("Event Owner Id: " + ownerId);
                                System.out.println("User Id: " + userId);
                                continue;
                            }
                            String eventId = eventS.getKey();
                            String eventName = event.get("eventName");
                            String eventDescription = event.get("description");
                            String eventDate = event.get("date");
                            String eventTime = event.get("time");
                            String eventLocation = event.get("location");
                            String eventGameId = event.get("gameId");
                            Event eventObj = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, ownerId, eventGameId);
                            DatabaseReference gameReference = appViewModel.database.getReference("Games").child(eventGameId);
                            gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot gameSnapshot) {
                                    if (gameSnapshot.exists()){
                                        String gameName = gameSnapshot.child("gameName").getValue(String.class);
                                        String eventMaxPlayers = gameSnapshot.child("maxPlayer").getValue(String.class);
                                        String eventMinPlayers = gameSnapshot.child("minPlayer").getValue(String.class);
                                        eventObj.setEventMaxPlayers(eventMaxPlayers);
                                        eventObj.setEventMinPlayers(eventMinPlayers);
                                        eventObj.setEventGameName(gameName);
                                        myEventsTemp.add(eventObj);
                                        allEvents = myEventsTemp;
                                        isEventsReceived.setValue(true);
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
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
