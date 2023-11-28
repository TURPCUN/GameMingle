package com.nt.gamemingle.model;

public class ChatMessage {

    String message;
    User sender;
    String messageTime;
    String messageDate;
    String chatMessageId;

    public ChatMessage() {
    }

    public ChatMessage(User sender, String message, String messageTime, String messageDate) {
        this.sender = sender;
        this.message = message;
        this.messageTime = messageTime;
        this.messageDate = messageDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public void setChatMessageId(String chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public String getChatMessageId() {
        return chatMessageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
