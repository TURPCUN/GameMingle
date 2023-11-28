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

public class MyGamesLibraryAdapter extends RecyclerView.Adapter{

    ArrayList<BoardGame> libraryGamesList;
    LayoutInflater inflater;

    ItemClickListener itemClickListener;

    public MyGamesLibraryAdapter(Context context, ArrayList<BoardGame> libraryGamesList, ItemClickListener itemClickListener){
        this.libraryGamesList = libraryGamesList;
        inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    public void setLibraryGamesList(ArrayList<BoardGame> libraryGamesList){
        this.libraryGamesList = libraryGamesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(com.nt.gamemingle.R.layout.game_card_square, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String gameName = libraryGamesList.get(position).getGameName();

        Glide.with(((MyGamesLibraryAdapter.ViewHolder) holder).imgCard.getContext())
                .load(libraryGamesList.get(position).getGameImageUrl())
                .placeholder(R.drawable.loading_gif)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.icon)
                .into(((MyGamesLibraryAdapter.ViewHolder) holder).imgCard);

        ((MyGamesLibraryAdapter.ViewHolder) holder).titleCard.setText(gameName);
    }

    @Override
    public int getItemCount() {
        return libraryGamesList.size();
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCard;
        TextView titleCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.cardImgGame);
            titleCard = itemView.findViewById(R.id.cardTitleGame);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
