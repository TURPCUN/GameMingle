package com.nt.gamemingle.ui.requests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.GameRequestsAdapter;
import com.nt.gamemingle.databinding.FragmentRequestBinding;
import com.nt.gamemingle.model.Requests;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends BaseFragment implements GameRequestsAdapter.ItemClickListener {

    FragmentRequestBinding binding;
    ArrayList<Requests> requestsList = new ArrayList<>();
    GameRequestsAdapter gameRequestsAdapter;
    private RequestViewModel requestViewModel;
    NavController navController;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        gameRequestsAdapter = new GameRequestsAdapter(new ArrayList<Requests>(), this);
        binding.recyclerRequests.setAdapter(gameRequestsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarVisibility(false);
        setBottomBarVisibility(true);
        // Reset after coming back
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottom_navigation_admin);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menuRequests).setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequestBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(false);
        navController = appViewModel.getNavController().getValue();

        requestViewModel = new RequestViewModel(appViewModel);

        requestViewModel.getRequests();

        binding.recyclerRequests.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        gameRequestsAdapter = new GameRequestsAdapter(requestsList, this);
        binding.recyclerRequests.setAdapter(gameRequestsAdapter);

        final Observer<List<Requests>> isRequestsLoaded = new Observer<List<Requests>>() {
            @Override
            public void onChanged(List<Requests> requests) {
                if(requests != null){
                    requestsList.clear();
                    requestsList.addAll(requests.subList(0, requests.size()));
                    gameRequestsAdapter.setRequestsList(requestsList);
                    gameRequestsAdapter.notifyDataSetChanged();
                }
            }
        };

        requestViewModel.requestsLiveData.observe(getViewLifecycleOwner(), isRequestsLoaded);
    }

    @Override
    public void onItemClicked(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("requestedGame", requestsList.get(position));
        navController.navigate(R.id.action_requestFragment_to_createGameFragment, bundle);
    }
}