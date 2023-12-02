package com.nt.gamemingle.ui.reports;

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
import com.nt.gamemingle.adapters.ReportsAdapter;
import com.nt.gamemingle.databinding.FragmentReportsBinding;
import com.nt.gamemingle.model.Report;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends BaseFragment implements ReportsAdapter.ItemClickListener{

    private ReportsViewModel mViewModel;
    ArrayList<Report> reportArrayList = new ArrayList<>();
    ReportsAdapter reportsAdapter;
    FragmentReportsBinding binding;
    NavController navController;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        reportsAdapter = new ReportsAdapter(new ArrayList<Report>(), this);
        binding.reportsRecyclerView.setAdapter(reportsAdapter);
        //mViewModel.getReports();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reset after coming back
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottom_navigation_admin);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menuReports).setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = appViewModel.getNavController().getValue();

        mViewModel = new ReportsViewModel(appViewModel);

        mViewModel.getReports();

        binding.reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reportsAdapter = new ReportsAdapter(reportArrayList, this);
        binding.reportsRecyclerView.setAdapter(reportsAdapter);

        final Observer<List<Report>> isReportsLoaded = new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                if(reports != null){
                    reportArrayList.clear();
                    reportArrayList.addAll(reports.subList(0, reports.size()));
                    reportsAdapter.setReportsList(reportArrayList);
                }
            }
        };

        mViewModel.reportsLiveData.observe(getViewLifecycleOwner(), isReportsLoaded);

    }

    @Override
    public void onSendWarningClicked(int position) {
        String reportId = reportArrayList.get(position).getReportId();
        String actionComment = "Warning sent";
        String senderId = reportArrayList.get(position).getChatMessage().getSender().getUserId();
        String eventId = reportArrayList.get(position).getEventId();
        String reporterId = reportArrayList.get(position).getReporterId();

        mViewModel.markReportAsRead(reportId, actionComment, senderId, eventId, reporterId);

    }
}