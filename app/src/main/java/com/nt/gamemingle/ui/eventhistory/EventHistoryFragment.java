package com.nt.gamemingle.ui.eventhistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.EventHistoryAdapter;
import com.nt.gamemingle.databinding.FragmentEventHistoryBinding;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;

public class EventHistoryFragment extends BaseFragment implements EventHistoryAdapter.ItemClickListener {

    FragmentEventHistoryBinding binding;
    private EventHistoryViewModel mViewModel;
    private EventHistoryAdapter eventHistoryAdapter;
    ArrayList<Event> eventHistoryList;
    NavController navController;

    @Override
    public void onResume() {
        super.onResume();
        eventHistoryAdapter = new EventHistoryAdapter(new ArrayList<Event>(), this);
        binding.recyclerEventsHistory.setAdapter(eventHistoryAdapter);
        mViewModel.getEventsHistory();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBottomBarVisibility(false);
        setToolBarVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = appViewModel.getNavController().getValue();
        mViewModel = new EventHistoryViewModel(appViewModel);

        mViewModel.getEventsHistory();
        binding.recyclerEventsHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        eventHistoryAdapter = new EventHistoryAdapter(new ArrayList<Event>(), this);
        binding.recyclerEventsHistory.setAdapter(eventHistoryAdapter);

        mViewModel.isEventsReceived.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
            if (aBoolean) {
                    eventHistoryList = mViewModel.eventsHistory;
                    eventHistoryAdapter.setEventHistoryList(eventHistoryList);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("event", eventHistoryList.get(position));
        navController.navigate(R.id.action_eventHistoryFragment_to_eventDetailsFragment, bundle);
    }
}