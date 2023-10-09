package com.nt.gamemingle.ui.signin;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nt.gamemingle.app.AppViewModel;

public class SignInViewModel {
    private AppViewModel appViewModel;

    MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(false);


    public SignInViewModel(AppViewModel appViewModel) {
        super();
        this.appViewModel = appViewModel;
    }
    public void signInWithEmailAndPassword(String email, String password, Context context) {
        appViewModel.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                            isSignedIn.setValue(true);
                        } else {
                            Toast.makeText(context, "Login ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
