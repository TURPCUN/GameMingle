package com.nt.gamemingle.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.EventSmallBinding;
import com.nt.gamemingle.model.Event;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter{

    ArrayList<Event> upcomingOrMyEventList;
    ItemClickListener itemClickListener;

    public EventsAdapter(Context context, ArrayList<Event> upcomingOrMyEventList, ItemClickListener itemClickListener){
        this.upcomingOrMyEventList = upcomingOrMyEventList;
        this.itemClickListener = itemClickListener;
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

        Glide.with(((ViewHolderEvent)holder).holderBinding.imgEvent.getContext())
                .load(upcomingOrMyEventList.get(position).getEventImageUrl())
                .placeholder(R.drawable.loading_gif)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.icon)
                .into(((ViewHolderEvent)holder).holderBinding.imgEvent);

        ((ViewHolderEvent)holder).holderBinding.eventLocation.setText(upcomingOrMyEventList.get(position).getEventLocation());
        String eventDate = upcomingOrMyEventList.get(position).getEventDate();
        String[] eventDateSplit = eventDate.split("/"); // day/ month/ year
        String dateDay = eventDateSplit[0];
        String dateMonth = dateFormat(eventDateSplit[1]);
        String dateFormat = dateDay + "\n" + dateMonth;
        ((ViewHolderEvent)holder).holderBinding.eventDate.setText(dateFormat);
        ((ViewHolderEvent)holder).holderBinding.attendeesCount.setText(String.valueOf(upcomingOrMyEventList.get(position).getEventAttendees()) + " Going");
    }


    @Override
    public int getItemCount() {
        return upcomingOrMyEventList.size();
    }

    private String dateFormat(String dateMonth){
        switch (dateMonth){
            case "01":
                dateMonth = "JAN";
                break;
            case "02":
                dateMonth = "FEB";
                break;
            case "03":
                dateMonth = "MAR";
                break;
            case "04":
                dateMonth = "APR";
                break;
            case "05":
                dateMonth = "MAY";
                break;
            case "06":
                dateMonth = "JUN";
                break;
            case "07":
                dateMonth = "JUL";
                break;
            case "08":
                dateMonth = "AUG";
                break;
            case "09":
                dateMonth = "SEP";
                break;
            case "10":
                dateMonth = "OCT";
                break;
            case "11":
                dateMonth = "NOV";
                break;
            case "12":
                dateMonth = "DEC";
                break;
        }

        return dateMonth;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
    public class ViewHolderEvent extends RecyclerView.ViewHolder{

        public EventSmallBinding holderBinding;
        public ViewHolderEvent(@NonNull View itemView, EventSmallBinding holderBinding) {
            super(itemView);
            this.holderBinding = holderBinding;
            this.holderBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}