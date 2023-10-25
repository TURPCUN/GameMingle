package com.nt.gamemingle.ui.eventdetails;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentEventDetailsBinding;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.ui.common.BaseFragment;

public class EventDetailsFragment extends BaseFragment {

    private EventDetailsViewModel mViewModel;
    Event event;
    FragmentEventDetailsBinding binding;
    NavController navController;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        event = getArguments().getParcelable("event");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getArguments().getParcelable("event");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        binding.titleEvent.setText(event.getEventName());
        binding.eventDescription.setText(event.getEventDescription());
        binding.eventLocation.setText(event.getEventLocation());
        String date = event.getEventDate() + " - " + event.getEventTime();
        binding.eventTime.setText(date);
        String gameName = event.getEventGameName();
        int imageResource = binding.getRoot().getContext().getResources()
                .getIdentifier(gameName.toLowerCase().replaceAll("\\s", ""), "drawable", binding.getRoot().getContext().getPackageName());
        if (imageResource != 0) {
            binding.imgEvent.setImageResource(imageResource);
        } else {
            binding.imgEvent.setImageResource(R.drawable.icon);
        }

        String userId = appViewModel.mAuth.getCurrentUser().getUid();
        String eventOwnerId = event.getEventOwnerId();
        if (userId.equals(eventOwnerId)) {
            binding.btnCancelEvent.setVisibility(View.VISIBLE);
            binding.btnRegisterEvent.setVisibility(View.GONE);
        }

    }
}