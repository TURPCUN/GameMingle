package com.nt.gamemingle.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Requests implements Parcelable {

    String requestId;
    String requestSenderId;
    String requestedGameName;
    String requestedGameDetails;
    public Requests(String requestId, String requestSenderId, String requestedGameName, String requestedGameDetails) {
        this.requestId = requestId;
        this.requestSenderId = requestSenderId;
        this.requestedGameName = requestedGameName;
        this.requestedGameDetails = requestedGameDetails;
    }

    protected Requests(Parcel in) {
        requestId = in.readString();
        requestSenderId = in.readString();
        requestedGameName = in.readString();
        requestedGameDetails = in.readString();
    }

    public static final Creator<Requests> CREATOR = new Creator<Requests>() {
        @Override
        public Requests createFromParcel(Parcel in) {
            return new Requests(in);
        }

        @Override
        public Requests[] newArray(int size) {
            return new Requests[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestSenderId() {
        return requestSenderId;
    }

    public void setRequestSenderId(String requestSenderId) {
        this.requestSenderId = requestSenderId;
    }

    public String getRequestedGameName() {
        return requestedGameName;
    }

    public void setRequestedGameName(String requestedGameName) {
        this.requestedGameName = requestedGameName;
    }

    public String getRequestedGameDetails() {
        return requestedGameDetails;
    }

    public void setRequestedGameDetails(String requestedGameDetails) {
        this.requestedGameDetails = requestedGameDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(requestId);
            dest.writeString(requestSenderId);
            dest.writeString(requestedGameName);
            dest.writeString(requestedGameDetails);
    }
}
