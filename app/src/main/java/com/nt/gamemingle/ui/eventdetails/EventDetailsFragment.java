package com.nt.gamemingle.ui.eventdetails;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.EventAttendeesAdapter;
import com.nt.gamemingle.databinding.FragmentEventDetailsBinding;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.model.User;
import com.nt.gamemingle.ui.common.BaseFragment;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventDetailsFragment extends BaseFragment implements EventAttendeesAdapter.ItemClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;
    private EventDetailsViewModel mViewModel;
    Event event;
    String eventIdFromNotification;
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
        if(event != null) {
            mViewModel.userEventStatus(event.getEventId(), event.getEventOwnerId());
            mViewModel.getEventAttendees(event.getEventId());
        }
        eventIdFromNotification = getArguments().getString("eventId");
        if (eventIdFromNotification != null) {
            mViewModel.getEvent(eventIdFromNotification);
        }

        setBottomBarVisibility(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getArguments().getParcelable("event");
        setBottomBarVisibility(false);
        navController = appViewModel.getNavController().getValue();
        mViewModel = new EventDetailsViewModel(appViewModel);
        eventIdFromNotification = getArguments().getString("eventId");
        if (eventIdFromNotification != null) {
            mViewModel.getEvent(eventIdFromNotification);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void configurationWithEvent() {
        if(event != null) {
            binding.titleEvent.setText(event.getEventName());
            binding.eventDescription.setText(event.getEventDescription());
            binding.eventLocation.setText(event.getEventLocation());
            String date = event.getEventDate() + " - " + event.getEventTime();
            binding.eventTime.setText(date);
            binding.eventOwner.setText(event.getEventOwnerName());

            Glide.with(binding.imgEvent.getContext())
                    .load(event.getEventImageUrl())
                    .placeholder(R.drawable.loading_gif)
                    .fitCenter()
                    .centerCrop()
                    .error(R.drawable.icon)
                    .into(binding.imgEvent);

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

            mViewModel.getEventAttendees(event.getEventId());
            binding.recyclerViewAttendees.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            eventAttendeesAdapter = new EventAttendeesAdapter(getContext(), attendeesList, this);
            binding.recyclerViewAttendees.setAdapter(eventAttendeesAdapter);

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

            binding.btnRegisterEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.registerForEvent(event.getEventId(), event.getEventName());
                    mViewModel.userEventStatus(event.getEventId(), event.getEventOwnerId());
                    Toast.makeText(getContext(), "Registered for event", Toast.LENGTH_SHORT).show();
                }
            });

            binding.fabUploadPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     openFileChooser();
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);


        configurationWithEvent();

        mViewModel.approvedEventAttendeesCount.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.attendeesCount.setText(integer + " Going");
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
                        binding.btnCancelEvent.setText("UNREGISTER");
                        binding.btnCancelEvent.setVisibility(View.VISIBLE);
                        binding.txtStatus.setVisibility(View.VISIBLE);
                        binding.fab.setVisibility(View.GONE);
                        binding.attendeesListLinearLayout.setVisibility(View.GONE);
                    } else if (s.equals("approved")){
                        binding.btnRegisterEvent.setVisibility(View.GONE);
                        binding.btnCancelEvent.setText("UNREGISTER");
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
                        binding.fabUploadPhoto.setVisibility(View.VISIBLE);
                    }
                    // if event is passed then hide all buttons and text, event history event details
                    if(event != null){
                        if(isEventPassed(event.getEventDate(), event.getEventTime())){
                            binding.btnRegisterEvent.setVisibility(View.INVISIBLE);
                            binding.btnCancelEvent.setVisibility(View.GONE);
                            binding.txtStatus.setVisibility(View.GONE);
                            binding.fab.setVisibility(View.VISIBLE);
                            binding.attendeesListLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
        });

        mViewModel.mutableEvent.observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(Event e) {
                event = e;
                configurationWithEvent();
            }
        });

        mViewModel.isAttendeesReceived.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    attendeesList = mViewModel.attendeesList;
                    eventAttendeesAdapter.setAttendeesList(attendeesList);
                }
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

    private boolean isEventPassed(String eventDate, String eventTime){
        // eventDate format is "dd/MM/yyyy"
        // eventTime format is "HH:mm"
        // compare it today and return true if eventDate is previous than today else false

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date eventDateTime = dateFormat.parse(eventDate + " " + eventTime);
            Date currentDateTime = new Date(); // Current date and time

            if (eventDateTime.before(currentDateTime)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            Picasso.with(requireContext()).load(data.getData()).into(binding.imgEvent);
            mViewModel.uploadImage(data.getData(), event.getEventId());
        }
    }
}