package com.nt.gamemingle.ui.signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nt.gamemingle.R;
import com.nt.gamemingle.ui.common.BaseFragment;
import com.nt.gamemingle.ui.common.SharedViewModel;

public class SignUpFragment extends BaseFragment {

    private SignUpViewModel mViewModel;
    private TextInputEditText userFullName;
    private TextInputEditText userEmail;
    private TextInputEditText userPassword;
    private TextInputEditText userConfirmPassword;
    private Button signUpButton;
    NavController navController;
    public SignUpFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = sharedViewModel.getNavController().getValue();
        userFullName = getActivity().findViewById(R.id.fullName);
        userEmail = getActivity().findViewById(R.id.emailSignUp);
        userPassword = getActivity().findViewById(R.id.passwordSignUp);
        userConfirmPassword = getActivity().findViewById(R.id.confPasswordSignUp);
        signUpButton = getActivity().findViewById(R.id.btn_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp(userFullName.getText().toString(), userEmail.getText().toString(), userPassword.getText().toString(), userConfirmPassword.getText().toString());
            }
        });

    }

    private void performSignUp(String fullName, String email, String password, String confirmPassword) {
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Log.d("SignUpFragment", "Empty fields");
        } else if (!password.equals(confirmPassword)) {
            Log.d("SignUpFragment", "Passwords do not match");
        } else {
            mViewModel.signUp(email, password, getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
}