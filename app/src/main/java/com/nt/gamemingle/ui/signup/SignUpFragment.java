package com.nt.gamemingle.ui.signup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentSignUpBinding;
import com.nt.gamemingle.ui.common.BaseFragment;

public class SignUpFragment extends BaseFragment {

    private SignUpViewModel mViewModel;
    private FragmentSignUpBinding binding;
    private NavController navController;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);
        setBottomBarVisibility(false);

        mViewModel = new SignUpViewModel(appViewModel);
        navController = appViewModel.getNavController().getValue();

        setUpCheckBox();
        setUpListeners();
        initializeCities();
        observeSignUpStatus();

    }

    private void setUpCheckBox() {
        SpannableString spannableString = new SpannableString(getString(R.string.accept_terms));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Terms and Conditions")
                        .setMessage(HtmlCompat.fromHtml(getString(R.string.terms_and_conditions), HtmlCompat.FROM_HTML_MODE_LEGACY))
                        .setPositiveButton("Ok", null)
                        .show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(Color.BLACK);
            }
        };

        spannableString.setSpan(clickableSpan, 15, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.checkBox.setText(spannableString);
        binding.checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        binding.checkBox.setHighlightColor(Color.TRANSPARENT);

    }

    private void setUpListeners() {

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });
    }

    private void initializeCities() {
        String[] citiesArray = getResources().getStringArray(R.array.cities);
        // AutoCompleteTextView for cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                            getContext(),
                                            android.R.layout.simple_dropdown_item_1line,
                                            citiesArray);
        binding.autoCompleteTextView.setAdapter(adapter);
    }

    private void observeSignUpStatus() {
        final NavController navController = appViewModel.getNavController().getValue();
        if (navController != null) {
            mViewModel.isSignedUp.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSignedUp) {
                    if (isSignedUp) {
                        navController.navigate(R.id.action_signUpFragment_to_myGamesFragment);
                    }
                }
            });
        }
    }

    private void performSignUp() {

        String fullName = binding.fullName.getText().toString();
        String email = binding.emailSignUp.getText().toString();
        String password = binding.passwordSignUp.getText().toString();
        String confirmPassword = binding.confPasswordSignUp.getText().toString();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Log.d("SignUpFragment", "Empty fields");
        } else if (!password.equals(confirmPassword)) {
            Log.d("SignUpFragment", "Passwords do not match");
        } else if (!binding.checkBox.isChecked()) {
            Log.d("SignUpFragment", "User agreement not checked");
        } else if (binding.autoCompleteTextView.getText().toString().isEmpty()) {
            Log.d("SignUpFragment", "City not selected");
        } else if (password.length() < 6) {
            Log.d("SignUpFragment", "Password too short");
        } else {
            mViewModel.signUp(fullName, email, password, getContext(), binding.autoCompleteTextView.getText().toString());
        }
    }
}