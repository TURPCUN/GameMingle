package com.nt.gamemingle.ui.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.RecentEventsAdapter;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;

public class RecentEventsFragment extends BaseFragment implements RecentEventsAdapter.ItemClickListener {

    private RecentEventsViewModel mViewModel;

    ArrayList<Event> recentEvents = new ArrayList<>();

    private RecentEventsAdapter recentEventsAdapter;

    RecyclerView recyclerRecentEvents;
    NavController navController;

    Button btnExploreEvents;

    CardView cardViewNoEvent;


    public RecentEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        recentEventsAdapter = new RecentEventsAdapter(requireContext(), new ArrayList<Event>(), this);
        recyclerRecentEvents.setAdapter(recentEventsAdapter);
        mViewModel.getRecommendedRecentEvents();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = appViewModel.getNavController().getValue();

        mViewModel = new RecentEventsViewModel(appViewModel);

        mViewModel.getRecommendedRecentEvents();

        recyclerRecentEvents = view.findViewById(R.id.recycler_recent_events);
        recyclerRecentEvents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false ));
        recentEventsAdapter = new RecentEventsAdapter(requireContext(), recentEvents, this);
        recyclerRecentEvents.setAdapter(recentEventsAdapter);

        cardViewNoEvent = view.findViewById(R.id.cardViewNoEvent);

        mViewModel.isRecentEventsReceived.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    recentEvents = mViewModel.recentEvents;
                    if(recentEvents.size() > 0){
                        cardViewNoEvent.setVisibility(View.GONE);
                        recentEventsAdapter.setRecentEventsList(recentEvents);
                    } else {
                        cardViewNoEvent.setVisibility(View.VISIBLE);
                    }
                }else {
                    cardViewNoEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        btnExploreEvents = getActivity().findViewById(R.id.btnExploreAll);
        btnExploreEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_myGamesFragment_to_searchEventsFragment);
            }
        });

        cardViewNoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_myGamesFragment_to_searchEventsFragment);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("event", recentEvents.get(position));
        navController.navigate(R.id.action_myGamesFragment_to_eventDetailsFragment, bundle);
    }
}