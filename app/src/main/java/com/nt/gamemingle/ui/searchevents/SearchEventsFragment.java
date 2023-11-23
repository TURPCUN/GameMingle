package com.nt.gamemingle.ui.searchevents;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.EventSearchAdapter;
import com.nt.gamemingle.model.Event;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchEventsFragment extends BaseFragment implements EventSearchAdapter.ItemClickListener{

    private SearchEventsViewModel mViewModel = new SearchEventsViewModel(appViewModel);
    private List<Event> eventList = new ArrayList<>();
    private EventSearchAdapter eventSearchAdapter;
    private SearchView searchView;
    RecyclerView recyclerSearchEvents;
    List<Event> filteredList = new ArrayList<>();
    NavController navController;
    public SearchEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        eventSearchAdapter = new EventSearchAdapter(requireContext(), new ArrayList<Event>(), this);
        recyclerSearchEvents.setAdapter(eventSearchAdapter);
        searchView.setQuery("", false);
        searchView.clearFocus();
       // eventList.clear();
        mViewModel.getEvents(requireContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        navController = appViewModel.getNavController().getValue();

        mViewModel.getEvents(requireContext());
        recyclerSearchEvents = getActivity().findViewById(R.id.recyclerSearchEvents);
        recyclerSearchEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventSearchAdapter = new EventSearchAdapter(getActivity(), eventList, this);
        recyclerSearchEvents.setAdapter(eventSearchAdapter);

        searchView = getActivity().findViewById(R.id.searchViewEvents);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        mViewModel.isEventsReceived.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    eventList = mViewModel.allEvents;
                    filteredList.clear();
                    filteredList.addAll(eventList);
                    eventSearchAdapter.setFilteredList(filteredList);
                }
            }
        });
    }

    private void filterList(String text) {
        filteredList.clear();
        if(text.equals("")){
            eventSearchAdapter.setFilteredList(eventList);
        } else {
            for(Event event : eventList){
                if(event.getEventGameName().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(event);
                }
            }
            if(!filteredList.isEmpty()){
                eventSearchAdapter.setFilteredList(filteredList);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(filteredList.size() >= position && filteredList.size() != 0) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", filteredList.get(position));
            navController.navigate(R.id.action_searchEventsFragment_to_eventDetailsFragment, bundle);
        }
    }
}