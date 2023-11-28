package com.nt.gamemingle.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.EventSmallerBinding;
import com.nt.gamemingle.model.Event;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class RecentEventsAdapter extends RecyclerView.Adapter {

    ArrayList<Event> recentEventsList;
    ItemClickListener itemClickListener;

    public RecentEventsAdapter(Context context, ArrayList<Event> recentEventsList, ItemClickListener itemClickListener){
        this.recentEventsList = recentEventsList;
        this.itemClickListener = itemClickListener;
    }
    public void setRecentEventsList(ArrayList<Event> recentEventsList){
        this.recentEventsList = recentEventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EventSmallerBinding binding = EventSmallerBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();
        ViewHolder holder = new ViewHolder(view, binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).holderBinding.eventName.setText(recentEventsList.get(position).getEventName());

        if (recentEventsList.get(position).getEventImageUrl() == null) {
            ((ViewHolder) holder).holderBinding.linearLayoutImgEvent.setBackgroundResource(R.drawable.icon);
        } else {
            ImageView imageView = ((ViewHolder) holder).holderBinding.imgLoading;

            // Loading gif using Glide library
            Glide.with(imageView.getContext())
                    .asGif()
                    .load(R.drawable.loading_gif)
                    .into(imageView);

            Picasso.with(imageView.getContext())
                    .load(recentEventsList.get(position).getEventImageUrl())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ((ViewHolder) holder).holderBinding.linearLayoutImgEvent.setBackground(new BitmapDrawable(imageView.getContext().getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
        }

        ((ViewHolder)holder).holderBinding.eventLocation.setText(recentEventsList.get(position).getEventLocation());
        String eventDate = recentEventsList.get(position).getEventDate();
        String[] eventDateSplit = eventDate.split("/");
        String dateDay = eventDateSplit[0];
        String dateMonth = dateFormat(eventDateSplit[1]);
        String dateFormat = dateDay + "\n" + dateMonth;
        ((ViewHolder)holder).holderBinding.eventDate.setText(dateFormat);
        ((ViewHolder)holder).holderBinding.attendeesCount.setText(String.valueOf(recentEventsList.get(position).getEventAttendees()) + " Going");
    }

    @Override
    public int getItemCount() {
        return recentEventsList.size();
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

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EventSmallerBinding holderBinding;

        public ViewHolder(@NonNull View itemView, EventSmallerBinding holderBinding) {
            super(itemView);
            this.holderBinding = holderBinding;
            this.holderBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
