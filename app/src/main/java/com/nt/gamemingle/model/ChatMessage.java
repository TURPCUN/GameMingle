package com.nt.gamemingle.model;

public class ChatMessage {

    String message;
    String userId;
    String userFullName;

    String messageTime;
    String messageDate;

    String chatMessageId;

    public ChatMessage() {
    }

    public ChatMessage(String message, String userId, String messageTime, String messageDate, String userFullName) {
        this.message = message;
        this.userId = userId;
        this.messageTime = messageTime;
        this.messageDate = messageDate;
        this.userFullName = userFullName;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public void setChatMessageId(String chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public String getChatMessageId() {
        return chatMessageId;
    }
}
