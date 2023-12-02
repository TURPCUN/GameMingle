package com.nt.gamemingle.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AppViewModel extends ViewModel {
    /** Firebase Instances **/
    public final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final DatabaseReference databaseReference = database.getReference();
    private final MutableLiveData<NavController> navController = new MutableLiveData<>();

    public void setNavController(NavController controller) {
        navController.setValue(controller);
    }

    public LiveData<NavController> getNavController() {
        return navController;
    }

    public void signOut() {
        mAuth.signOut();
    }

    public static AppViewModel instance = new AppViewModel();
    private AppViewModel() {
        super();
    }

    // Notification management

    public void upcomingEventsNotification() {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId != null) {
            ArrayList<Event> upcomingEventsTemp = new ArrayList<>();
            DatabaseReference userAttendEventRef = database.getReference("USER_ATTEND_EVENT");
            userAttendEventRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            String eventId = dataSnapshot.getKey();
                            DatabaseReference eventParticipantsRef = database.getReference("USER_ATTEND_EVENT").child(eventId).child("participants").child(userId);
                            eventParticipantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                                        if (isApproved != null) {
                                            if (isApproved) {
                                                DatabaseReference eventReference = database.getReference("EVENT").child(eventId);
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
                                                            Event event = new Event(eventId, eventName, eventDescription, eventDate, eventTime, eventLocation, eventOwnerId, eventGameId);
                                                            DatabaseReference gameReference = database.getReference("Games").child(eventGameId);
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

                                                                        DatabaseReference eventOwnerReference = database.getReference("Users").child(eventOwnerId);
                                                                        eventOwnerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                String ownerName = snapshot.child("userFullName").getValue(String.class);
                                                                                event.setEventOwnerName(ownerName);
                                                                                upcomingEventsTemp.add(event);
                                                                                // Event date difference, create notification if less than one day left
                                                                                try {
                                                                                    String dateTimeString = eventDate + " " + eventTime;
                                                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                                                                    Date eventDateTime = sdf.parse(dateTimeString);
                                                                                    // Get current date and time
                                                                                    Date currentDate = new Date();
                                                                                    long timeDifference = eventDateTime.getTime() - currentDate.getTime();
                                                                                    long oneDayInMillis = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
                                                                                    if (timeDifference < oneDayInMillis && timeDifference > 0) {
                                                                                        // Less than one day left for the event
                                                                                        // Notification
                                                                                        // add a notification without a notification id, eventId will be used as notification id
                                                                                        // as prevention for multiple notifications for the same event for every login
                                                                                        Calendar calendar = Calendar.getInstance();
                                                                                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                                                                        int minute = calendar.get(Calendar.MINUTE);
                                                                                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                                                                                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                                                                                        int month = calendar.get(Calendar.MONTH) + 1;
                                                                                        int year = calendar.get(Calendar.YEAR);
                                                                                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
                                                                                        databaseReference.child("NOTIFICATION").child(userId).child(eventId).child("eventId").setValue(eventId);
                                                                                        databaseReference.child("NOTIFICATION").child(userId).child(eventId).child("message").setValue("You have an upcoming event: " + eventName);
                                                                                        databaseReference.child("NOTIFICATION").child(userId).child(eventId).child("isRead").setValue(false);
                                                                                        databaseReference.child("NOTIFICATION").child(userId).child(eventId).child("time").setValue(formattedTime);
                                                                                        databaseReference.child("NOTIFICATION").child(userId).child(eventId).child("date").setValue(formattedDate);
                                                                                    }
                                                                                } catch (
                                                                                        ParseException e) {
                                                                                    throw new RuntimeException(e);
                                                                                }
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
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("AppViewModel", "Error: on upcomingEventsNotification" + error.getMessage());
                }
            });
        }
    }
}
