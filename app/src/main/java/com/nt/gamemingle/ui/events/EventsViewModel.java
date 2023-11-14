package com.nt.gamemingle.ui.events;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsViewModel {

    private AppViewModel appViewModel;

    public ArrayList<Event> upcomingEvents = new ArrayList<>();
    public ArrayList<Event> myEvents = new ArrayList<>();
    MutableLiveData<Boolean> isEventsReceived = new MutableLiveData<>(false);

    private boolean upcomingEventsChecked = false;
    private boolean myEventsChecked = false;

    public EventsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void getUpcomingEvents(Context context) {
        upcomingEvents.clear();
        getMyEvents(context);
    }

    public void getUpcomingEventsFromFirebase(Context context) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<Event> upcomingEventsTemp = new ArrayList<>();
            ArrayList<String> upcomingEventsId = new ArrayList<>();
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
                                                upcomingEventsId.add(eventId);
                                                    DatabaseReference eventReference = appViewModel.database.getReference("EVENT").child(eventId);
                                                    eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                String eventName = snapshot.child("eventName").getValue(String.class);
                                                                String eventDescription = snapshot.child("description").getValue(String.class);
                                                                String eventDate = snapshot.child("date").getValue(String.class);
                                                                String eventTime = snapshot.child("time").getValue(String.class);
                                                                String eventLocation = snapshot.child("location").getValue(String.class);
                                                                String eventGameId = snapshot.child("gameId").getValue(String.class);
                                                                String eventOwnerId = snapshot.child("ownerId").getValue(String.class);
                                                                //   int eventAttendeesCount = snapshot.child("approvedAttendeesCount").getValue(Integer.class);
                                                                Event event = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, eventOwnerId, eventGameId);
                                                                //  event.setEventAttendees(eventAttendeesCount);
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
                                                                                    upcomingEventsTemp.add(event);
                                                                                    upcomingEvents = upcomingEventsTemp;
                                                                                    try {
                                                                                        orderByEventDate(upcomingEvents);
                                                                                    } catch (ParseException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    // isEventsReceived.setValue(true);
                                                                                    upcomingEventsChecked = true;
                                                                                    checkEventsReceived();
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
                                                                        Log.d("EventsViewModel", "Error: " + error.getMessage());
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.d("EventsViewModel", "Error: " + error.getMessage());
                                                        }
                                                    });

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("EventsViewModel", "Error: " + error.getMessage());
                                }
                            });
                        }
                        else {
                            upcomingEventsChecked = true;
                            checkEventsReceived();
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("EventsViewModel", "Error: " + error.getMessage());
                }
            });
        }
    }

    private void checkEventsReceived(){
        if (upcomingEventsChecked && myEventsChecked) {
            isEventsReceived.setValue(true);
        }
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
                                        if(!isEventPassed(eventDate, eventTime)){
                                            String eventLocation = eventSnapshot.child("location").getValue(String.class);
                                            String eventGameId = eventSnapshot.child("gameId").getValue(String.class);
                                            int eventAttendeesCount = eventSnapshot.child("approvedAttendeesCount").getValue(Integer.class);
                                            Event event = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, userId, eventGameId);
                                            event.setEventAttendees(eventAttendeesCount);
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

                                                        DatabaseReference eventOwnerReference = appViewModel.database.getReference("Users").child(userId);
                                                        eventOwnerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String ownerName = snapshot.child("userFullName").getValue(String.class);
                                                                event.setEventOwnerName(ownerName);
                                                                myEventsTemp.add(event);
                                                                myEvents = myEventsTemp;
                                                                try {
                                                                    orderByEventDate(myEvents);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                //isEventsReceived.setValue(true);
                                                                myEventsChecked = true;
                                                                checkEventsReceived();
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

    public void getMyEvents(Context context){
        myEvents.clear();
        isEventsReceived.setValue(true);
    }
}
