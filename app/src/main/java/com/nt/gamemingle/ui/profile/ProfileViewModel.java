package com.nt.gamemingle.ui.profile;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.model.User;

public class ProfileViewModel {

    private AppViewModel appViewModel;

    public MutableLiveData<User> currentUser = new MutableLiveData<>();

    public ProfileViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void changePassword(String newPassword, Context context) {
        if (appViewModel.mAuth.getCurrentUser() != null){
        FirebaseUser user = appViewModel.mAuth.getCurrentUser();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(String.valueOf(context), "User password updated.");
                            Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(String.valueOf(context), "User password not updated.");
                            Toast.makeText(context, "Password not changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }


    public void getUserInformation() {
        User user = new User();
        String currentUserId = appViewModel.mAuth.getCurrentUser().getUid();
        DatabaseReference userReference = appViewModel.database.getReference("Users").child(currentUserId);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userFullName = snapshot.child("userFullName").getValue(String.class);
                user.setFullName(userFullName);
                currentUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileViewModel", "onCancelled: " + error.getMessage());
            }
        });
    }
}