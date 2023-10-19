package com.nt.gamemingle.ui.signup;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nt.gamemingle.app.AppViewModel;

import java.util.UUID;

public class SignUpViewModel {

    private AppViewModel appViewModel;
    MutableLiveData<Boolean> isSignedUp = new MutableLiveData<>(false);
    public SignUpViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }

    public void signUp(String fullName, String email, String password, Context context, String city) {
        appViewModel.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        handleSignUpResult(task, context, fullName, city);
                    }
                });
    }

    private void handleSignUpResult(Task<AuthResult> task, Context context, String fullName, String city) {
        if (task.isComplete()) {
            showToast(context, "Registration successful!");
            if (appViewModel.mAuth.getCurrentUser() != null) {
                saveUserToDatabase(appViewModel.mAuth.getCurrentUser().getUid(), fullName, context, city);
                isSignedUp.setValue(true);
            }
        } else {
            showToast(context, "Registration ERROR!");
        }
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void saveUserToDatabase(String uuid, String fullName, Context context, String city) {
        appViewModel.databaseReference.child("Users").child(uuid).child("userFullName").setValue(fullName);
        appViewModel.databaseReference.child("Users").child(uuid).child("userCity").setValue(city);
        appViewModel.databaseReference.child("Users").child(uuid).child("registerDate").setValue
                                                                        (System.currentTimeMillis());
    }

}
