package com.nt.gamemingle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt.gamemingle.databinding.EventSmallBinding;
import com.nt.gamemingle.model.Event;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter{

    ArrayList<Event> upcomingOrMyEventList;

    public EventsAdapter(Context context, ArrayList<Event> upcomingOrMyEventList){
        this.upcomingOrMyEventList = upcomingOrMyEventList;
    }

    public void setUpcomingOrMyEventList(ArrayList<Event> upcomingOrMyEventList){
        this.upcomingOrMyEventList = upcomingOrMyEventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EventSmallBinding binding = EventSmallBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();
        ViewHolderEvent holder = new ViewHolderEvent(view, binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderEvent)holder).holderBinding.eventName.setText(upcomingOrMyEventList.get(position).getEventName());
        // TODO: 2023-10-19 EVENT DATE AND Attendees
        ((ViewHolderEvent)holder).holderBinding.eventLocation.setText(upcomingOrMyEventList.get(position).getEventLocation());
    }


    @Override
    public int getItemCount() {
        return upcomingOrMyEventList.size();
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder{

        public EventSmallBinding holderBinding;
        public ViewHolderEvent(@NonNull View itemView, EventSmallBinding holderBinding) {
            super(itemView);
            this.holderBinding = holderBinding;
            this.holderBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
