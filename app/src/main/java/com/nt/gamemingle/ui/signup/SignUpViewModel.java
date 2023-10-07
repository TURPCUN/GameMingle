package com.nt.gamemingle.ui.signup;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nt.gamemingle.ui.common.BaseViewModel;

public class SignUpViewModel extends BaseViewModel {
    public void signUp(String email, String password, Context context) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(context, "User is registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "User is not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
