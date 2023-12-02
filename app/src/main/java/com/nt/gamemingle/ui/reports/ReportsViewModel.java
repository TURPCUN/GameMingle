package com.nt.gamemingle.ui.reports;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.ChatMessage;
import com.nt.gamemingle.model.Report;
import com.nt.gamemingle.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ReportsViewModel {

    AppViewModel appViewModel;

    MutableLiveData<List<Report>> reportsLiveData = new MutableLiveData<>();

    public ReportsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void getReports(){
        ArrayList<Report> reports = new ArrayList<>();
        DatabaseReference reportsReference = appViewModel.databaseReference.child("REPORTING");
        reportsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reports.clear();
                for (DataSnapshot reportSnapshot : snapshot.getChildren()) {
                    String reportId = reportSnapshot.getKey();
                    Boolean isRead = reportSnapshot.child("isRead").getValue(Boolean.class);
                        String eventId = reportSnapshot.child("eventId").getValue(String.class);
                        String reporterId = reportSnapshot.child("reporter").getValue(String.class);
                        String reportReason = reportSnapshot.child("reportReason").getValue(String.class);
                        String chatMessageId = reportSnapshot.child("messageId").getValue(String.class);
                        if (eventId != null && chatMessageId != null) {
                            DatabaseReference eventChatReference = appViewModel.databaseReference.child("EVENT").child(eventId).child("Chat").child(chatMessageId);
                            eventChatReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot eventChatSnapshot) {
                                    String message = eventChatSnapshot.child("message").getValue(String.class);
                                    String messageTime = eventChatSnapshot.child("messageTime").getValue(String.class);
                                    String messageSenderId = eventChatSnapshot.child("userId").getValue(String.class);
                                    String messageDate = eventChatSnapshot.child("messageDate").getValue(String.class);
                                    if (messageSenderId != null) {
                                        DatabaseReference userReference = appViewModel.databaseReference.child("Users").child(messageSenderId);
                                        userReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                                String senderId = userSnapshot.getKey();
                                                String userFullName = userSnapshot.child("userFullName").getValue(String.class);
                                                User messageSender = new User();
                                                messageSender.setUserId(senderId);
                                                messageSender.setFullName(userFullName);
                                                ChatMessage chatMessage = new ChatMessage(messageSender, message, messageTime, messageDate);
                                                chatMessage.setChatMessageId(chatMessageId);
                                                Report report = new Report(reportId, eventId, reporterId, reportReason, chatMessage);
                                                report.setIsRead(isRead);
                                                reports.add(report);
                                                orderArrayByReadStatus(reports);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.e("ReportsViewModel", "onCancelled: ", error.toException());
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("ReportsViewModel", "onCancelled: ", error.toException());
                                }
                            });
                        }
                    }
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ReportsViewModel", "onCancelled: ", error.toException());
            }
        });
    }

    private void orderArrayByReadStatus(ArrayList<Report> reports){
        ArrayList<Report> unreadReports = new ArrayList<>();
        ArrayList<Report> readReports = new ArrayList<>();
        for (Report report : reports){
            if (report.getIsRead()){
                readReports.add(report);
            } else {
                unreadReports.add(report);
            }
        }
        reports.clear();
        reports.addAll(unreadReports);
        reports.addAll(readReports);
        reportsLiveData.setValue(reports);
    }

    public void markReportAsRead(String reportId, String actionComment, String senderId, String eventId, String reporterId){
        appViewModel.databaseReference.child("REPORTING").child(reportId).child("isRead").setValue(true);
        appViewModel.databaseReference.child("REPORTING").child(reportId).child("actionComment").setValue(actionComment);

        // send notification to the user
        UUID uuid = UUID.randomUUID();
        String notificationUuid = uuid.toString();
        appViewModel.databaseReference.child("NOTIFICATION").child(senderId).child(notificationUuid).child("eventId").setValue(eventId);
        appViewModel.databaseReference.child("NOTIFICATION").child(senderId).child(notificationUuid).child("message").setValue("Your account has been reported. Please review and follow platform rules for interactions.");
        appViewModel.databaseReference.child("NOTIFICATION").child(senderId).child(notificationUuid).child("isRead").setValue(false);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        appViewModel.databaseReference.child("NOTIFICATION").child(senderId).child(notificationUuid).child("time").setValue(formattedTime);
        appViewModel.databaseReference.child("NOTIFICATION").child(senderId).child(notificationUuid).child("date").setValue(formattedDate);

        // send notification to the reporter
        UUID uuid2 = UUID.randomUUID();
        String notificationUuid2 = uuid2.toString();
        appViewModel.databaseReference.child("NOTIFICATION").child(reporterId).child(notificationUuid2).child("eventId").setValue(eventId);
        appViewModel.databaseReference.child("NOTIFICATION").child(reporterId).child(notificationUuid2).child("message").setValue("Your report has been processed. Thank you for helping us maintain a safe environment.");
        appViewModel.databaseReference.child("NOTIFICATION").child(reporterId).child(notificationUuid2).child("isRead").setValue(false);

        appViewModel.databaseReference.child("NOTIFICATION").child(reporterId).child(notificationUuid2).child("time").setValue(formattedTime);
        appViewModel.databaseReference.child("NOTIFICATION").child(reporterId).child(notificationUuid2).child("date").setValue(formattedDate);

        getReports();
    }
}
