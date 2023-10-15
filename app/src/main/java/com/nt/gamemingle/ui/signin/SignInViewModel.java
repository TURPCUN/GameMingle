package com.nt.gamemingle.ui.signin;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.nt.gamemingle.app.AppViewModel;

public class SignInViewModel {
    private AppViewModel appViewModel;
    MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(false);

    public SignInViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }
    public void signInWithEmailAndPassword(String email, String password, Context context) {

        if (email.isEmpty() || password.isEmpty()) {
            showToast(context, "Email and password cannot be empty!");
            return;
        }

        appViewModel.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        handleSignInResult(task, context);
                    }
                });
    }

    private void handleSignInResult(Task<AuthResult> task, Context context) {
        if (task.isSuccessful()) {
            showToast(context, "Login successful!");
            isSignedIn.setValue(true);
        } else {
            showToast(context, "Login ERROR!");
        }
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void checkUser(Context context, boolean rememberMeChecked) {
        FirebaseUser currentUser = appViewModel.mAuth.getCurrentUser();
        if (currentUser != null ) {
            if(rememberMeChecked) {
                showToast(context, "User is logged in");
                isSignedIn.setValue(true);
            } else {
                showToast(context, "User is not logged in");
                appViewModel.mAuth.signOut();
                isSignedIn.setValue(false);
            }
        }
    }
}
