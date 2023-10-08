package com.nt.gamemingle.ui.signin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.nt.gamemingle.R;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.ui.common.BaseFragment;


public class SignInFragment extends BaseFragment {

    //AppViewModel appViewModel;

    private  SignInViewModel mViewModel;
    private TextInputEditText userEmail;
    private TextInputEditText userPassword;
    private Button signInButton;
    private TextView signUpButton;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        setToolBarVisibility(false);

        mViewModel = new SignInViewModel(appViewModel);
        navController = appViewModel.getNavController().getValue();
        userEmail = getActivity().findViewById(R.id.email);
        userPassword = getActivity().findViewById(R.id.password);
        signInButton = getActivity().findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignIn(userEmail.getText().toString(), userPassword.getText().toString());
            }
        });
        signUpButton = getActivity().findViewById(R.id.txt_btn_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });

        final Observer<Boolean> isSignedInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSignedIn) {
                if (isSignedIn) {
                    navController.navigate(R.id.action_signInFragment_to_myGamesEmpty);
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.isSignedIn.observe(getViewLifecycleOwner(), isSignedInObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void performSignIn(String email, String password) {
        mViewModel.signInWithEmailAndPassword(email, password,requireContext());
    }
}