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

public class MyGamesLibrarySeeAllAdapter extends RecyclerView.Adapter {

    ArrayList<BoardGame> myLibraryBoardGameList;
    LayoutInflater inflater;

    public MyGamesLibrarySeeAllAdapter(Context context, ArrayList<BoardGame> myLibraryBoardGameList){
        this.myLibraryBoardGameList = myLibraryBoardGameList;
        inflater = LayoutInflater.from(context);
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
        String gameName = myLibraryBoardGameList.get(position).getGameName();
        int imageResource = holder.itemView.getContext().getResources()
                .getIdentifier(gameName.toLowerCase(), "drawable", holder.itemView.getContext().getPackageName());
        if (imageResource != 0) {
            ((ViewHolderGameDetails)holder).imgCard.setImageResource(imageResource);
        } else {
            ((ViewHolderGameDetails)holder).imgCard.setImageResource(R.drawable.icon);
        }
        ((ViewHolderGameDetails)holder).titleCard.setText(myLibraryBoardGameList.get(position).getGameName());
        ((ViewHolderGameDetails)holder).descriptionCard.setText(myLibraryBoardGameList.get(position).getGameDescription());
    }

    @Override
    public int getItemCount() {
        return myLibraryBoardGameList.size();
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
