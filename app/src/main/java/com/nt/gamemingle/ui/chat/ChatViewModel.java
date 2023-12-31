package com.nt.gamemingle.ui.chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.ChatMessage;
import com.nt.gamemingle.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ChatViewModel {
    private AppViewModel appViewModel;
    public MutableLiveData<ArrayList<ChatMessage>> chatMessageMutableLiveData = new MutableLiveData<>();

    public ChatViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void sendMessage(String message, String eventId, String time, String date) {

        UUID uuid = UUID.randomUUID();
        String messageUuid = uuid.toString();
        String userId = appViewModel.mAuth.getCurrentUser().getUid();

        DatabaseReference userReference = appViewModel.database.getReference("Users").child(userId);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, String> user = (HashMap<String, String>) snapshot.getValue();
                String userName = user.get("userFullName");
                appViewModel.databaseReference.child("EVENT").child(eventId).child("Chat").child(messageUuid).child("userName").setValue(userName);
                appViewModel.databaseReference.child("EVENT").child(eventId).child("Chat").child(messageUuid).child("message").setValue(message);
                appViewModel.databaseReference.child("EVENT").child(eventId).child("Chat").child(messageUuid).child("userId").setValue(userId);
                appViewModel.databaseReference.child("EVENT").child(eventId).child("Chat").child(messageUuid).child("messageTime").setValue(time);
                appViewModel.databaseReference.child("EVENT").child(eventId).child("Chat").child(messageUuid).child("messageDate").setValue(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatViewModel", "onCancelled: ", error.toException());
            }
        });
    }

    public void getMessages(String eventId) {

        DatabaseReference chatReference = appViewModel.database.getReference("EVENT").child(eventId).child("Chat");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HashMap<String, String> chatMessage = (HashMap<String, String>) dataSnapshot.getValue();
                    String chatMessageId = dataSnapshot.getKey();
                    String message = chatMessage.get("message");
                    String userId = chatMessage.get("userId");
                    if(userId != null) {
                        DatabaseReference userReference = appViewModel.database.getReference("Users").child(userId);
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                                String username = chatMessage.get("userName");
                                User sender = new User();
                                sender.setUserProfileImageUrl(profileImageUrl);
                                sender.setFullName(username);
                                sender.setUserId(userId);

                                String time = chatMessage.get("messageTime");
                                String date = chatMessage.get("messageDate");

                                ChatMessage chatMessage1 = new ChatMessage(sender, message, time, date);
                                chatMessage1.setChatMessageId(chatMessageId);
                                chatMessages.add(chatMessage1);

                                try {
                                    orderByMessageDate(chatMessages);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                chatMessageMutableLiveData.setValue(chatMessages);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("ChatViewModel", "onCancelled: ", error.toException());
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatViewModel", "onCancelled: ", error.toException());
            }
        });
    }

    private void orderByMessageDate(ArrayList<ChatMessage> messages) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (messages.size() == 0 || messages.size() == 1)
            return;
        for (int i = 0; i<messages.size(); i++){
            for (int j = i+1; j<messages.size(); j++){
                Date prevEventDate = dateFormat.parse(messages.get(i).getMessageDate() + " " + messages.get(i).getMessageTime());
                Date eventDateTime = dateFormat.parse(messages.get(j).getMessageDate() + " " + messages.get(j).getMessageTime());
                if (eventDateTime.before(prevEventDate)) {
                    ChatMessage tempMsg = messages.get(i);
                    messages.set(i, messages.get(j));
                    messages.set(j, tempMsg);
                }
            }
        }
    }

    public void reportSpamMessage(String eventId, String messageId, String reportReason) {
        UUID uuid = UUID.randomUUID();
        String reportUuid = uuid.toString();
        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        appViewModel.databaseReference.child("REPORTING").child(reportUuid).child("eventId").setValue(eventId);
        appViewModel.databaseReference.child("REPORTING").child(reportUuid).child("messageId").setValue(messageId);
        appViewModel.databaseReference.child("REPORTING").child(reportUuid).child("reportReason").setValue(reportReason);
        appViewModel.databaseReference.child("REPORTING").child(reportUuid).child("reporter").setValue(userId);
        appViewModel.databaseReference.child("REPORTING").child(reportUuid).child("isRead").setValue(false);
        appViewModel.databaseReference.child("REPORTING").child(reportUuid).child("actionComment").setValue("");
    }
}
