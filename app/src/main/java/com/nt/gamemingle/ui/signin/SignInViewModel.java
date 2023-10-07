package com.nt.gamemingle.ui.signin;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nt.gamemingle.HomeActivity;
import com.nt.gamemingle.ui.common.BaseViewModel;

public class SignInViewModel extends BaseViewModel {
    public void signInWithEmailAndPassword(String email, String password, Context context) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);

                        } else {
                            Toast.makeText(context, "Login ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
