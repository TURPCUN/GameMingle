package com.nt.gamemingle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.AttendeesLayoutBinding;
import com.nt.gamemingle.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventAttendeesAdapter extends RecyclerView.Adapter{

    ItemClickListener itemClickListener;
    public List<User> attendeesList;
    LayoutInflater inflater;

    public EventAttendeesAdapter(Context context, List<User> attendeesList, ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
        this.attendeesList = attendeesList;
        inflater = LayoutInflater.from(context);
    }

    public void setAttendeesList(ArrayList<User> attendeesList){
        this.attendeesList = attendeesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttendeesLayoutBinding binding = AttendeesLayoutBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();
        ViewHolderAttendees holder = new ViewHolderAttendees(view, binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderAttendees)holder).holderBinding.attendeeName.setText(attendeesList.get(position).getFullName());
        if (attendeesList.get(position).getUserProfileImageUrl() == null) {
            ((ViewHolderAttendees)holder).holderBinding.attendeeImg.setImageResource(R.drawable.icon);
        } else {
            Picasso.with(((ViewHolderAttendees) holder).holderBinding.getRoot().getContext())
                    .load(attendeesList.get(position).getUserProfileImageUrl())
                    .fit()
                    .centerCrop()
                    .into(((ViewHolderAttendees) holder).holderBinding.attendeeImg);
        }
        if(attendeesList.get(position).getUserEventStatus().equals("true")){
            ((ViewHolderAttendees)holder).holderBinding.btnApprove.setVisibility(View.GONE);
            ((ViewHolderAttendees)holder).holderBinding.btnDeny.setVisibility(View.GONE);
            ((ViewHolderAttendees)holder).holderBinding.attendeeDone.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolderAttendees)holder).holderBinding.btnApprove.setVisibility(View.VISIBLE);
            ((ViewHolderAttendees)holder).holderBinding.btnDeny.setVisibility(View.VISIBLE);
            ((ViewHolderAttendees)holder).holderBinding.attendeeDone.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

    public interface ItemClickListener{
        void onApproveClick(View view, int position);
        void onDenyClick(View view, int position);
    }

    public class ViewHolderAttendees extends RecyclerView.ViewHolder {

        public AttendeesLayoutBinding holderBinding;

        public ViewHolderAttendees(@NonNull View itemView, AttendeesLayoutBinding holderBinding) {
            super(itemView);
            this.holderBinding = holderBinding;
            this.holderBinding.btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onApproveClick(v, getAdapterPosition());
                }
            });

            this.holderBinding.btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onDenyClick(v, getAdapterPosition());
                }
            });
        }
    }
}
