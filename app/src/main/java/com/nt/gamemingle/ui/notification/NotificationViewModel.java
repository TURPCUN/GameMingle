package com.nt.gamemingle.ui.notification;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationViewModel {

    private AppViewModel appViewModel;
    public MutableLiveData<ArrayList<Notification>> notifications = new MutableLiveData<>();
    private ArrayList<Notification> notificationList = new ArrayList<>();

    public NotificationViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void getNotifications() {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference notificationReference = appViewModel.database.getReference("NOTIFICATION").child(userId);
            notificationReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    notificationList.clear();
                    for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                        Boolean isRead = notificationSnapshot.child("isRead").getValue(Boolean.class);
                        String notificationId = notificationSnapshot.getKey();
                        String eventId = notificationSnapshot.child("eventId").getValue(String.class);
                        String message = notificationSnapshot.child("message").getValue(String.class);
                        String notificationDate = notificationSnapshot.child("date").getValue(String.class);
                        String notificationTime = notificationSnapshot.child("time").getValue(String.class);
                        Notification notification = new Notification(notificationId, userId, eventId, message, notificationDate, notificationTime, isRead, "");
                        notificationList.add(notification);
                    }
                    try {
                        orderByNotificationTime(notificationList);
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    notifications.setValue(notificationList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("NotificationViewModel", "Error getting notifications");
                }
            });
        }
    }

    private void orderByNotificationTime(ArrayList<Notification> notifications) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (notifications.size() == 0 || notifications.size() == 1) {
            return;
        }
        for (int i = 0; i < notifications.size(); i++) {
            for (int j = i + 1; j < notifications.size(); j++) {
                Date date1 = dateFormat.parse(notifications.get(i).getDate() + " " + notifications.get(i).getTime());
                Date date2 = dateFormat.parse(notifications.get(j).getDate() + " " + notifications.get(j).getTime());
                if (date1.before(date2)) {
                    Notification temp = notifications.get(i);
                    notifications.set(i, notifications.get(j));
                    notifications.set(j, temp);
                }
            }
        }
    }

    public void readAllNotifications() {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference notificationReference = appViewModel.database.getReference("NOTIFICATION").child(userId);
            notificationReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                        notificationSnapshot.child("isRead").getRef().setValue(true);
                    }
                    getNotifications();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("NotificationViewModel", "Error getting notifications");
                }
            });
        }
    }

    public void readNotification(String notificationId) {
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference notificationReference = appViewModel.database.getReference("NOTIFICATION").child(userId);
            notificationReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                        if (notificationSnapshot.getKey().equals(notificationId)) {
                            appViewModel.databaseReference.child("NOTIFICATION").child(userId).child(notificationId).child("isRead").setValue(true);
                            break;
                        }
                    }
                    getNotifications();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("NotificationViewModel", "Error getting notifications");
                }
            });
        }
    }
}
