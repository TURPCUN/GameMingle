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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                            String eventImageUrl = event.get("imageUrl");
                            if(isEventPassed(eventDate, eventTime)) {
                                continue;
                            }
                            String eventLocation = event.get("location");
                            String eventGameId = event.get("gameId");
                            int eventAttendeesCount = eventS.child("approvedAttendeesCount").getValue(Integer.class);
                            Event eventObj = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, ownerId, eventGameId);
                            eventObj.setEventImageUrl(eventImageUrl);
                            eventObj.setEventAttendees(eventAttendeesCount);
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
                                        DatabaseReference eventOwnerReference = appViewModel.database.getReference("Users").child(ownerId);
                                        eventOwnerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String ownerName = snapshot.child("userFullName").getValue(String.class);
                                                eventObj.setEventOwnerName(ownerName);
                                                myEventsTemp.add(eventObj);
                                                allEvents = myEventsTemp;
                                                try {
                                                    orderByEventDate(allEvents);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                isEventsReceived.setValue(true);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("SearchEventsViewModel", "Error: " + error.getMessage());
                                            }
                                        });
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

}
