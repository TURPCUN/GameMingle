package com.nt.gamemingle.model;

public class User {

    String userId;
    String fullName;
    String userEventStatus;
    private String profileImageUrl;

    private String isAdmin;
    public User(){

    }
    public User(String userId, String fullName, String userEventStatus){
        this.userId = userId;
        this.fullName = fullName;
        this.userEventStatus = userEventStatus;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getUserEventStatus(){
        return userEventStatus;
    }

    public void setUserEventStatus(String userEventStatus){
        this.userEventStatus = userEventStatus;
    }

    public void setUserProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserProfileImageUrl() {
        return profileImageUrl;
    }
}
