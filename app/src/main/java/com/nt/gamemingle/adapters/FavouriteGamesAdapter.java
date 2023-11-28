package com.nt.gamemingle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nt.gamemingle.R;
import com.nt.gamemingle.model.BoardGame;

import java.util.ArrayList;

public class FavouriteGamesAdapter extends RecyclerView.Adapter {

    ArrayList<BoardGame> favouriteGamesList;
    LayoutInflater inflater;

    public FavouriteGamesAdapter(Context context, ArrayList<BoardGame> favoriteGamesList){
        this.favouriteGamesList = favoriteGamesList;
        inflater = LayoutInflater.from(context);
    }

    public void setFavouriteGamesList(ArrayList<BoardGame> favouriteGamesList){
        this.favouriteGamesList = favouriteGamesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.game_card_square, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String gameName = favouriteGamesList.get(position).getGameName();

        Glide.with(((ViewHolder) holder).imgCard.getContext())
                .load(favouriteGamesList.get(position).getGameImageUrl())
                .placeholder(R.drawable.loading_gif)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.icon)
                .into(((ViewHolder) holder).imgCard);

        ((ViewHolder) holder).titleCard.setText(gameName);
    }

    @Override
    public int getItemCount() {
        return favouriteGamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCard;
        TextView titleCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.cardImgGame);
            titleCard = itemView.findViewById(R.id.cardTitleGame);
        }
    }
}