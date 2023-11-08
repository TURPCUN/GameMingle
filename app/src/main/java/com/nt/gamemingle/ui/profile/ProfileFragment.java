package com.nt.gamemingle.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentProfileBinding;
import com.nt.gamemingle.model.User;
import com.nt.gamemingle.ui.common.BaseFragment;

public class ProfileFragment extends BaseFragment {

    private ProfileViewModel profileViewModel;

    private FragmentProfileBinding binding;

    private NavController navController;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        profileViewModel.getUserInformation();
        setToolBarVisibility(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarVisibility(false);
        profileViewModel = new ProfileViewModel(appViewModel);
        profileViewModel.getUserInformation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        profileViewModel.currentUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String fullName = user.getFullName();
                binding.userFullName.setText(fullName);

                String firstName = fullName.split(" ")[0];
                int profileImgResource = binding.getRoot().getContext().getResources()
                        .getIdentifier(firstName.toLowerCase(), "drawable", binding.getRoot().getContext().getPackageName());
                if (profileImgResource != 0) {
                    binding.imgProfile.setImageResource(profileImgResource);
                } else {
                    binding.imgProfile.setImageResource(R.drawable.icon);
                }
                makeVisibleElements();
            }
        });

        binding.userEmail.setText(appViewModel.mAuth.getCurrentUser().getEmail());

        return binding.getRoot();
    }

    private void makeVisibleElements(){
        binding.imgProfile.setVisibility(View.VISIBLE);
        binding.userFullName.setVisibility(View.VISIBLE);
        binding.userEmail.setVisibility(View.VISIBLE);
        binding.txtLogOut.setVisibility(View.VISIBLE);
        binding.txtChangePassword.setVisibility(View.VISIBLE);
        binding.txtEventHistory.setVisibility(View.VISIBLE);
        binding.txtTermsAndConditions.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = appViewModel.getNavController().getValue();

        binding.txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    appViewModel.signOut();
                    navController.navigate(R.id.action_profileFragment_to_signInFragment);
                }
            }
        });

        binding.txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_profileFragment_to_changePasswordFragment);
                }
            }
        });

        binding.txtTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Terms and Conditions")
                        .setMessage(HtmlCompat.fromHtml(getString(R.string.terms_and_conditions), HtmlCompat.FROM_HTML_MODE_LEGACY))
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });

    }
}