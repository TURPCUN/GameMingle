package com.nt.gamemingle.ui.testfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentTestBinding;
import com.nt.gamemingle.ui.common.BaseFragment;

public class TestFragment extends BaseFragment {

    FragmentTestBinding binding;

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(false);

        NavController navController = appViewModel.getNavController().getValue();

        binding.testAddFavGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("gameId","622a554e-8298-49a0-8c5f-242d01b99da1");
                    bundle.putString("title","Ticket to Ride");
                    bundle.putString("imageUrl", "test");
                    navController.navigate(R.id.action_testFragment_to_addFavGameFragment, bundle);
                }
            }
        });

        binding.testCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_testFragment_to_createEventFragment);
                }
            }
        });

        binding.testEmptyGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_testFragment_to_myGamesEmpty);
                }
            }
        });

        binding.testEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_testFragment_to_eventDetailsFragment);
                }
            }
        });

        binding.testEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_testFragment_to_eventsFragment);
                }
            }
        });


        //8
        binding.testMyGamesScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_testFragment_to_myGamesFragment);
                }
            }
        });

        //9
        binding.testSearchGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.action_testFragment_to_searchGamesFragment);
                }
            }
        });


    }
}