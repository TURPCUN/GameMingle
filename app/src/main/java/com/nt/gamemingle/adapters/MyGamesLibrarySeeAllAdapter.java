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

public class MyGamesLibrarySeeAllAdapter extends RecyclerView.Adapter {

    ArrayList<BoardGame> myLibraryBoardGameList;
    LayoutInflater inflater;
    ItemClickListener itemClickListener;

    public MyGamesLibrarySeeAllAdapter(Context context, ArrayList<BoardGame> myLibraryBoardGameList, ItemClickListener itemClickListener){
        this.myLibraryBoardGameList = myLibraryBoardGameList;
        inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    public void setMyLibraryBoardGameList(ArrayList<BoardGame> myLibraryBoardGameList){
        this.myLibraryBoardGameList = myLibraryBoardGameList;
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

        Glide.with(((ViewHolderGameDetails) holder).imgCard.getContext())
                .load(myLibraryBoardGameList.get(position).getGameImageUrl())
                .placeholder(R.drawable.loading_gif)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.icon)
                .into(((ViewHolderGameDetails) holder).imgCard);

        ((ViewHolderGameDetails)holder).titleCard.setText(myLibraryBoardGameList.get(position).getGameName());
        ((ViewHolderGameDetails)holder).descriptionCard.setText(myLibraryBoardGameList.get(position).getGameDescription());
    }

    @Override
    public int getItemCount() {
        return myLibraryBoardGameList.size();
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
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
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
