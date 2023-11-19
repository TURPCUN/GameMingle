package com.nt.gamemingle.ui.eventdetails;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.EventAttendeesAdapter;
import com.nt.gamemingle.databinding.FragmentEventDetailsBinding;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.model.User;
import com.nt.gamemingle.ui.common.BaseFragment;
import java.util.ArrayList;

public class EventDetailsFragment extends BaseFragment implements EventAttendeesAdapter.ItemClickListener {

    private EventDetailsViewModel mViewModel;
    Event event;
    FragmentEventDetailsBinding binding;
    NavController navController;
    EventAttendeesAdapter eventAttendeesAdapter;
    ArrayList<User> attendeesList = new ArrayList<>();

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        eventAttendeesAdapter = new EventAttendeesAdapter(requireContext(), new ArrayList<User>(), this);
        binding.recyclerViewAttendees.setAdapter(eventAttendeesAdapter);
        event = getArguments().getParcelable("event");
        mViewModel.userEventStatus(event.getEventId(), event.getEventOwnerId());
        mViewModel.getEventAttendees(event.getEventId());
        setBottomBarVisibility(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getArguments().getParcelable("event");
        setBottomBarVisibility(false);
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

        navController = appViewModel.getNavController().getValue();
        mViewModel = new EventDetailsViewModel(appViewModel);

        binding.titleEvent.setText(event.getEventName());
        binding.eventDescription.setText(event.getEventDescription());
        binding.eventLocation.setText(event.getEventLocation());
        String date = event.getEventDate() + " - " + event.getEventTime();
        binding.eventTime.setText(date);
        binding.eventOwner.setText(event.getEventOwnerName());
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
            binding.fab.setVisibility(View.VISIBLE);
        } else {
            binding.btnCancelEvent.setVisibility(View.GONE);
            binding.btnRegisterEvent.setVisibility(View.VISIBLE);
        }

        mViewModel.userEventStatus(event.getEventId(), eventOwnerId);

        mViewModel.approvedEventAttendeesCount.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.attendeesCount.setText("+" + integer + " Going");
            }
        });

        mViewModel.user_EventStatus.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty() && !s.equals("")){
                    if (s.equals("not registered")){
                        binding.btnRegisterEvent.setVisibility(View.VISIBLE);
                        binding.btnCancelEvent.setVisibility(View.GONE);
                        binding.txtStatus.setVisibility(View.GONE);
                        binding.fab.setVisibility(View.GONE);
                        binding.attendeesListLinearLayout.setVisibility(View.GONE);
                    } else if (s.equals("pending")){
                        binding.btnRegisterEvent.setVisibility(View.GONE);
                        binding.btnCancelEvent.setVisibility(View.VISIBLE);
                        binding.txtStatus.setVisibility(View.VISIBLE);
                        binding.fab.setVisibility(View.GONE);
                        binding.attendeesListLinearLayout.setVisibility(View.GONE);
                    } else if (s.equals("approved")){
                        binding.btnRegisterEvent.setVisibility(View.GONE);
                        binding.btnCancelEvent.setVisibility(View.VISIBLE);
                        binding.txtStatus.setVisibility(View.GONE);
                        binding.fab.setVisibility(View.VISIBLE);
                        binding.attendeesListLinearLayout.setVisibility(View.GONE);
                    } else if (s.equals("owner")){
                        binding.btnRegisterEvent.setVisibility(View.GONE);
                        binding.btnCancelEvent.setVisibility(View.VISIBLE);
                        binding.txtStatus.setVisibility(View.GONE);
                        binding.fab.setVisibility(View.VISIBLE);
                        binding.attendeesListLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mViewModel.getEventAttendees(event.getEventId());
        binding.recyclerViewAttendees.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        eventAttendeesAdapter = new EventAttendeesAdapter(getContext(), attendeesList, this);
        binding.recyclerViewAttendees.setAdapter(eventAttendeesAdapter);

        mViewModel.isAttendeesReceived.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    attendeesList = mViewModel.attendeesList;
                    eventAttendeesAdapter.setAttendeesList(attendeesList);
                }
            }

        });
        binding.btnRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.registerForEvent(event.getEventId(), event.getEventName());
                mViewModel.userEventStatus(event.getEventId(), event.getEventOwnerId());
                Toast.makeText(getContext(), "Registered for event", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.cancelEvent(event.getEventId(), eventOwnerId, event.getEventName());
                mViewModel.userEventStatus(event.getEventId(), eventOwnerId);
                Toast.makeText(getContext(), "Cancelled event", Toast.LENGTH_SHORT).show();
                if ((appViewModel.mAuth.getCurrentUser().getUid()).equals(eventOwnerId)) {
                    navController.navigate(R.id.action_eventDetailsFragment_to_eventsFragment);
                }
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("event", event);
                navController.navigate(R.id.action_eventDetailsFragment_to_chatFragment, bundle);
            }
        });
    }

    @Override
    public void onApproveClick(View view, int position) {
        mViewModel.approveUser(event.getEventId(), attendeesList.get(position).getUserId(), event.getEventName());
    }

    @Override
    public void onDenyClick(View view, int position) {
        mViewModel.denyUser(event.getEventId(), attendeesList.get(position).getUserId());
    }
}