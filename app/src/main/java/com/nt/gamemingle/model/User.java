package com.nt.gamemingle.model;

public class User {

    String userId;
    String fullName;
    String userEventStatus;

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
}
