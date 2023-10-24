package com.nt.gamemingle.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Event implements Parcelable {

    String eventId;
    String eventName;
    String eventDescription;
    String eventDate;
    String eventTime;
    String eventLocation;
    String eventImageUrl;
    String eventMinPlayers;
    String eventMaxPlayers;
    String eventOwnerId;
    String eventGameId;

    public Event(String eventId, String eventName, String eventDescription, String eventDate, String eventTime, String eventLocation, String eventImageUrl, String eventMinPlayers, String eventMaxPlayers, String eventOwnerId, String eventGameId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventImageUrl = eventImageUrl;
        this.eventMinPlayers = eventMinPlayers;
        this.eventMaxPlayers = eventMaxPlayers;
        this.eventOwnerId = eventOwnerId;
        this.eventGameId = eventGameId;
    }

    public Event(String eventId, String eventName, String eventDescription, String eventDate, String eventTime, String eventLocation, String eventOwnerId, String eventGameId){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventOwnerId = eventOwnerId;
        this.eventGameId = eventGameId;
    }

    public Event() {
    }

    protected Event(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        eventDescription = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        eventLocation = in.readString();
        eventImageUrl = in.readString();
        eventMinPlayers = in.readString();
        eventMaxPlayers = in.readString();
        eventOwnerId = in.readString();
        eventGameId = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventMinPlayers() {
        return eventMinPlayers;
    }

    public void setEventMinPlayers(String eventMinPlayers) {
        this.eventMinPlayers = eventMinPlayers;
    }

    public String getEventMaxPlayers() {
        return eventMaxPlayers;
    }

    public void setEventMaxPlayers(String eventMaxPlayers) {
        this.eventMaxPlayers = eventMaxPlayers;
    }

    public String getEventOwnerId() {
        return eventOwnerId;
    }

    public void setEventOwnerId(String eventOwnerId) {
        this.eventOwnerId = eventOwnerId;
    }

    public String getEventGameId() {
        return eventGameId;
    }

    public void setEventGameId(String eventGameId) {
        this.eventGameId = eventGameId;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(eventDescription);
        dest.writeString(eventDate);
        dest.writeString(eventTime);
        dest.writeString(eventLocation);
        dest.writeString(eventImageUrl);
        dest.writeString(eventMinPlayers);
        dest.writeString(eventMaxPlayers);
        dest.writeString(eventOwnerId);
        dest.writeString(eventGameId);
    }
}
