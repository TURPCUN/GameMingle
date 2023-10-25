package com.nt.gamemingle.ui.events;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.EventsAdapter;
import com.nt.gamemingle.databinding.FragmentEventsBinding;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends BaseFragment implements EventsAdapter.ItemClickListener{

    FragmentEventsBinding binding;
    private int selectedTabNumber = 1;

    private EventsViewModel mViewModel;
    private EventsAdapter eventsAdapter;
    ArrayList<Event> upcomingEventsList;
    ArrayList<Event> MyEventsList;

    NavController navController;

    @Override
    public void onResume() {
        super.onResume();
        eventsAdapter = new EventsAdapter(requireContext(), new ArrayList<Event>(), this);
        binding.recyclerEvents.setAdapter(eventsAdapter);
        mViewModel.getMyEventsFromFirebase(requireContext());
       // mViewModel.getMyEvents(requireContext());
    }

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        navController = appViewModel.getNavController().getValue();
        mViewModel = new EventsViewModel(appViewModel);

        mViewModel.getMyEventsFromFirebase(requireContext());
        binding.recyclerEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        eventsAdapter = new EventsAdapter(requireContext(), upcomingEventsList, this);
        binding.recyclerEvents.setAdapter(eventsAdapter);

        mViewModel.isEventsReceived.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    upcomingEventsList = mViewModel.upcomingEvents;
                    MyEventsList = mViewModel.myEvents;
                    eventsAdapter.setUpcomingOrMyEventList(upcomingEventsList);
                }
            }
        });

        binding.tabUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(1);
                if (upcomingEventsList != null) {
                    eventsAdapter.setUpcomingOrMyEventList(upcomingEventsList);
                }
            }
        });

        binding.tabMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(2);
                if (MyEventsList != null) {
                    eventsAdapter.setUpcomingOrMyEventList(MyEventsList);
                }
            }
        });

    }

    private void selectTab(int tabNumber){

        TextView selectedTextView;
        TextView unSelectedTextView;

        if(tabNumber == 1) {
            selectedTextView = binding.tabUpcoming;
            unSelectedTextView = binding.tabMyEvents;

            // Change recyclerview content
        } else {
            selectedTextView = binding.tabMyEvents;
            unSelectedTextView = binding.tabUpcoming;

            // Change recyclerview content
        }

        // Animation
        float slideTo = (tabNumber - selectedTabNumber) * selectedTextView.getWidth();

        // create translate animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);

        // checking for previously selected tab
        if(selectedTabNumber == 1){
            binding.tabUpcoming.startAnimation(translateAnimation);
        } else {
            binding.tabMyEvents.startAnimation(translateAnimation);
        }

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                // change design of selected tab's TextView
                selectedTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.primary_blue, null));
                selectedTextView.setBackgroundResource(R.drawable.round_back_white_100);
                selectedTextView.setTypeface(null, Typeface.BOLD);

                // change design of unselected tab's TextView
                //unSelectedTextView.setTextColor(Color.parseColor("#80FFFFFF"));
                unSelectedTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.background_gray2, null));
                unSelectedTextView.setBackgroundColor(ResourcesCompat.getColor(getResources(), android.R.color.transparent, null));
                unSelectedTextView.setTypeface(null, Typeface.NORMAL);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        selectedTabNumber = tabNumber;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(selectedTabNumber == 1){

        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", MyEventsList.get(position));
            navController.navigate(R.id.action_eventsFragment_to_eventDetailsFragment, bundle);
        }
    }
}