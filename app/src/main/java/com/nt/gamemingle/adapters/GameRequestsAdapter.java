package com.nt.gamemingle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt.gamemingle.databinding.RequestLayoutBinding;
import com.nt.gamemingle.model.Requests;

import java.util.ArrayList;

public class GameRequestsAdapter extends RecyclerView.Adapter {

    ItemClickListener itemClickListener;
    ArrayList<Requests> requestsList;

    public GameRequestsAdapter(ArrayList<Requests> requestsList, ItemClickListener itemClickListener){
        this.requestsList = requestsList;
        this.itemClickListener = itemClickListener;
    }

    public void setRequestsList(ArrayList<Requests> requestsList){
        this.requestsList = requestsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RequestLayoutBinding binding = RequestLayoutBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();
        return new RequestViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RequestViewHolder)holder).holderBinding.cardTitleRequestedGame.setText(requestsList.get(position).getRequestedGameName());
        ((RequestViewHolder)holder).holderBinding.cardDescriptionRequestedGame.setText(requestsList.get(position).getRequestedGameDetails());
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public interface ItemClickListener {
        void onItemClicked(int position);
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{
        RequestLayoutBinding holderBinding;
        public RequestViewHolder(@NonNull View itemView, RequestLayoutBinding holderBinding) {
            super(itemView);
            this.holderBinding = holderBinding;
            this.holderBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
