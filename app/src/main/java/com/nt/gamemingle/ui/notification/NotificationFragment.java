package com.nt.gamemingle.ui.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.NotificationAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.model.Notification;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;

public class NotificationFragment extends BaseFragment implements NotificationAdapter.ItemClickListener {

    private NotificationViewModel mViewModel;
    ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    RecyclerView recyclerNotifications;
    TextView txtMarkAllAsRead;
    NavController navController;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolBarVisibility(false);
        setBottomBarVisibility(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reset after coming back
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottom_navigation);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menuNotification).setChecked(true);

        setToolBarVisibility(false);
        setBottomBarVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = appViewModel.getNavController().getValue();
        mViewModel = new NotificationViewModel(appViewModel);
        mViewModel.getNotifications();

        recyclerNotifications = view.findViewById(R.id.recycler_view_notification);
        txtMarkAllAsRead = view.findViewById(R.id.txtMarkAllAsRead);

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        notificationAdapter = new NotificationAdapter(notifications, this);
        recyclerNotifications.setAdapter(notificationAdapter);

        mViewModel.notifications.observe(getViewLifecycleOwner(), notifications -> {
            this.notifications = notifications;
            notificationAdapter.setNotifications(notifications);
        });

        txtMarkAllAsRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Notification notification : notifications) {
                    notification.setRead(true);
                }
                mViewModel.readAllNotifications();
            }
        });
    }

    @Override
    public void makeItemReadClick(int position) {
        notifications.get(position).setRead(true);
        mViewModel.readNotification(notifications.get(position).getNotificationId());
        Bundle bundle = new Bundle();
        try {
            if (notifications.get(position).getEventId() != null) {
                bundle.putString("eventId", notifications.get(position).getEventId());
                navController.navigate(R.id.action_notificationFragment_to_eventDetailsFragment, bundle);
            } else if (notifications.get(position).getGameId() != null) {
                bundle.putString("gameId", notifications.get(position).getGameId());
                navController.navigate(R.id.action_notificationFragment_to_addFavGameFragment, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}