package com.nt.gamemingle.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentProfileBinding;
import com.nt.gamemingle.model.User;
import com.nt.gamemingle.ui.chat.ReportDialogFragment;
import com.nt.gamemingle.ui.common.BaseFragment;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends BaseFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;

    private Uri mImageUri;
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
        setBottomBarVisibility(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reset after coming back
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottom_navigation);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menuProfile).setChecked(true);

        setToolBarVisibility(false);
        setBottomBarVisibility(true);
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
                if (user.getUserProfileImageUrl() != null){
                    Glide.with(requireContext())
                            .load(user.getUserProfileImageUrl())
                            .placeholder(R.drawable.loading_gif)
                            .error(R.drawable.profile)
                            .into(binding.imgProfile);
                } else {
                   binding.imgProfile.setImageResource(R.drawable.profile);
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
        binding.btnUpload.setVisibility(View.VISIBLE);
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

        binding.txtEventHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_profileFragment_to_eventHistoryFragment);
                }
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(requireContext()).load(mImageUri).into(binding.imgProfile);
            profileViewModel.uploadImage(mImageUri);
        }
    }
}