package com.nt.gamemingle.ui.signin;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentSignInBinding;
import com.nt.gamemingle.ui.common.BaseFragment;


public class SignInFragment extends BaseFragment {

    private  SignInViewModel signInViewModel;
    private FragmentSignInBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getPreferences(requireContext().MODE_PRIVATE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolBarVisibility(false);
        setBottomBarVisibility(false);
        signInViewModel = new SignInViewModel(appViewModel);
        setupClickListeners();
        observeSignInStatus();

        boolean rememberMeChecked = sharedPreferences.getBoolean("rememberMe", false);
        binding.switchRememberMe.setChecked(rememberMeChecked);
        signInViewModel.checkUser(requireContext(), rememberMeChecked);
    }

    private void setupClickListeners() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignIn();
            }
        });

        binding.txtBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUp();
            }
        });

        // Remember Me
        binding.switchRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean("rememberMe", isChecked);
                editor.apply();
            }
        });

        binding.btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(requireContext(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        appViewModel.mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(requireContext(), "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(requireContext(), "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }

    private void observeSignInStatus() {
        final NavController navController = appViewModel.getNavController().getValue();
        if (navController != null) {
            signInViewModel.isSignedIn.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSignedIn) {
                    if (isSignedIn) {
                        if (appViewModel.mAuth.getCurrentUser().getEmail().equals("admin@admin.com")) {
                            navController.navigate(R.id.action_signInFragment_to_requestFragment);
                        } else {
                            navController.navigate(R.id.action_signInFragment_to_myGamesFragment);
                        }
                    }
                }
            });
        }
    }

    private void performSignIn() {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        signInViewModel.signInWithEmailAndPassword(email, password, requireContext());
    }

    private void navigateToSignUp() {
        NavController navController = appViewModel.getNavController().getValue();
        if (navController != null) {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment);
        }
    }
}