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

public class FavouriteGamesSeeAllAdapter extends RecyclerView.Adapter<FavouriteGamesSeeAllAdapter.ViewHolderGameDetails> {

    ArrayList<BoardGame> favouriteGamesList;
    LayoutInflater inflater;
    ItemClickListener itemClickListener;


    public FavouriteGamesSeeAllAdapter(Context context, ArrayList<BoardGame> favouriteGamesList, ItemClickListener itemClickListener){
        this.favouriteGamesList = favouriteGamesList;
        inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    public void setFavouriteGamesList(ArrayList<BoardGame> favouriteGamesList){
        this.favouriteGamesList = favouriteGamesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderGameDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.game_card_rectangle, parent, false);
        ViewHolderGameDetails viewHolderGameDetails = new ViewHolderGameDetails(view);
        return viewHolderGameDetails;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGameDetails holder, int position) {

        Glide.with(holder.imgCard.getContext())
                .load(favouriteGamesList.get(position).getGameImageUrl())
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.loading_gif)
                .error(R.drawable.icon)
                .into(holder.imgCard);

        holder.titleCard.setText(favouriteGamesList.get(position).getGameName());
        holder.descriptionCard.setText(favouriteGamesList.get(position).getGameDescription());
    }

    @Override
    public int getItemCount() {
        return favouriteGamesList.size();
    }

    public interface ItemClickListener{
        void onItemClick(int position);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}