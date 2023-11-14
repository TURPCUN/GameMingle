package com.nt.gamemingle.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentChangePasswordBinding;
import com.nt.gamemingle.ui.common.BaseFragment;

public class ChangePasswordFragment extends BaseFragment {

    private ProfileViewModel profileViewModel;
    private FragmentChangePasswordBinding binding;
    private NavController navController;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setBottomBarVisibility(false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBottomBarVisibility(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        profileViewModel = new ProfileViewModel(appViewModel);
        navController = appViewModel.getNavController().getValue();
        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performChangePassword();
            }
        });
    }



    private void performChangePassword() {
        String newPassword = binding.passwordNew.getText().toString();
        String confirmPassword = binding.passwordNewConfirm.getText().toString();

        if (newPassword.isEmpty()) {
            binding.passwordNew.setError("Please enter new password");
            return;
        }

        if (confirmPassword.isEmpty()) {
            binding.passwordNewConfirm.setError("Please enter confirm password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.passwordNewConfirm.setError("Password does not match");
            return;
        }

        profileViewModel.changePassword(newPassword, getContext());
    }
}