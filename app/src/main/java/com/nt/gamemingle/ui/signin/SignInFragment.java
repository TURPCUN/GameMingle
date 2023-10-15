package com.nt.gamemingle.ui.signin;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
    }

    private void observeSignInStatus() {
        final NavController navController = appViewModel.getNavController().getValue();
        if (navController != null) {
            signInViewModel.isSignedIn.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSignedIn) {
                    if (isSignedIn) {
                        navigateToMyGamesFragment(navController);
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

    private void navigateToMyGamesFragment(NavController navController) {
        navController.navigate(R.id.action_signInFragment_to_myGamesFragment);
    }
}