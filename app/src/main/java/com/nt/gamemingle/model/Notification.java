package com.nt.gamemingle.model;

public class Notification {

    String notificationId;
    String userId;
    String eventId;
    String message;
    String date;
    String time;
    Boolean isRead;
    String eventName;

    public Notification() {
    }

    public Notification(String notificationId, String userId, String eventId, String message, String date, String time, Boolean isRead, String eventName) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.eventId = eventId;
        this.message = message;
        this.date = date;
        this.time = time;
        this.isRead = isRead;
        this.eventName = eventName;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
