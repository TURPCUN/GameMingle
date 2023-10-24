package com.nt.gamemingle.ui.events;

import android.content.Context;
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

public class EventsViewModel {

    private AppViewModel appViewModel;

    public ArrayList<Event> upcomingEvents = new ArrayList<>();
    public ArrayList<Event> myEvents = new ArrayList<>();
    MutableLiveData<Boolean> isEventsReceived = new MutableLiveData<>(false);

    public EventsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void getUpcomingEvents(Context context) {
        upcomingEvents.clear();
        getMyEvents(context);
    }

    public void getUpcomingEventsFromFirebase(Context context) {


    }

    public void getMyEventsFromFirebase(Context context) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<Event> myEventsTemp = new ArrayList<>();
            DatabaseReference userEventsReference = appViewModel.database.getReference("Users").child(userId).child("events");
            userEventsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String eventId = dataSnapshot.getKey();
                        if(eventId != null) {
                            DatabaseReference eventReference = appViewModel.database.getReference("EVENT").child(eventId);
                            eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot eventSnapshot) {
                                    if (eventSnapshot.exists()){

                                        String eventName = eventSnapshot.child("eventName").getValue(String.class);
                                        String eventDescription = eventSnapshot.child("description").getValue(String.class);
                                        String eventDate = eventSnapshot.child("date").getValue(String.class);
                                        String eventTime = eventSnapshot.child("time").getValue(String.class);
                                        String eventLocation = eventSnapshot.child("location").getValue(String.class);
                                        String eventGameId = eventSnapshot.child("gameId").getValue(String.class);

                                        Event event = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, userId, eventGameId);
                                        DatabaseReference gameReference = appViewModel.database.getReference("Games").child(eventGameId);
                                        gameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot gameSnapshot) {
                                                if (gameSnapshot.exists()){
                                                    String gameName = gameSnapshot.child("gameName").getValue(String.class);
                                                    String eventMaxPlayers = gameSnapshot.child("maxPlayer").getValue(String.class);
                                                    String eventMinPlayers = gameSnapshot.child("minPlayer").getValue(String.class);
                                                    event.setEventMaxPlayers(eventMaxPlayers);
                                                    event.setEventMinPlayers(eventMinPlayers);
                                                    event.setEventGameName(gameName);
                                                    myEventsTemp.add(event);
                                                    myEvents = myEventsTemp;
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

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
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

    public void getMyEvents(Context context){
        myEvents.clear();
        isEventsReceived.setValue(true);
    }
}
