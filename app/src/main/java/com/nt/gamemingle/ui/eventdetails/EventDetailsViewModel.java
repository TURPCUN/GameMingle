package com.nt.gamemingle.ui.eventdetails;

import com.nt.gamemingle.app.AppViewModel;

public class EventDetailsViewModel {

    private AppViewModel appViewModel;

    public EventDetailsViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void registerForEvent(String eventId) {
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).setValue(true);
        appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).child("isApproved").setValue(false);
    }

    public void cancelEvent(String eventId, String eventOwnerId) {
        if (appViewModel.mAuth.getCurrentUser().getUid().equals(eventOwnerId)) {
            appViewModel.databaseReference.child("EVENT").child(eventId).removeValue();
            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).removeValue();
        } else {
            appViewModel.databaseReference.child("USER_ATTEND_EVENT").child(eventId).child("participants").child(appViewModel.mAuth.getCurrentUser().getUid()).removeValue();
        }
    }
}