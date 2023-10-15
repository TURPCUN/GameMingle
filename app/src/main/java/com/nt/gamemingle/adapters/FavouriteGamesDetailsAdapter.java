package com.nt.gamemingle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt.gamemingle.R;
import com.nt.gamemingle.model.BoardGame;

import java.util.ArrayList;

public class FavouriteGamesDetailsAdapter extends RecyclerView.Adapter {

    ArrayList<BoardGame> favouriteGamesList;
    LayoutInflater inflater;

    public FavouriteGamesDetailsAdapter(Context context, ArrayList<BoardGame> favouriteGamesList){
        this.favouriteGamesList = favouriteGamesList;
        inflater = LayoutInflater.from(context);
    }

    public void setFavouriteGamesList(ArrayList<BoardGame> favouriteGamesList){
        this.favouriteGamesList = favouriteGamesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.game_card_rectangle, parent, false);
        ViewHolderGameDetails viewHolderGameDetails = new ViewHolderGameDetails(view);
        return viewHolderGameDetails;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolderGameDetails)holder).imgCard.setImageResource(R.drawable.icon);
        ((ViewHolderGameDetails)holder).titleCard.setText(favouriteGamesList.get(position).getGameName());
        ((ViewHolderGameDetails)holder).descriptionCard.setText(favouriteGamesList.get(position).getGameDescription());
    }

    @Override
    public int getItemCount() {
        return favouriteGamesList.size();
    }

    public class ViewHolderGameDetails extends RecyclerView.ViewHolder{

        ImageView imgCard;
        TextView titleCard;
        TextView descriptionCard;
        public ViewHolderGameDetails(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.cardImgGameDetail);
            titleCard = itemView.findViewById(R.id.cardTitleGameDetail);
            descriptionCard = itemView.findViewById(R.id.cardDescriptionGameDetail);
        }
    }
}