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

import java.util.List;

public class GameSearchAdapter extends RecyclerView.Adapter {

    ItemClickListener itemClickListener;
    public List<BoardGame> boardGameList;
    LayoutInflater inflater;

    public GameSearchAdapter(Context context, List<BoardGame> boardGameList, ItemClickListener itemClickListener){
        this.boardGameList = boardGameList;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.game_card_rectangle, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String gameName = boardGameList.get(position).getGameName();

        Glide.with(((ViewHolder) holder).cardImgGameDetail.getContext())
                .load(boardGameList.get(position).getGameImageUrl())
                .placeholder(R.drawable.loading_gif)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.icon)
                .into(((ViewHolder) holder).cardImgGameDetail);

        ((ViewHolder)holder).cardTitleGameDetail.setText(gameName);
        ((ViewHolder)holder).cardDescriptionGameDetail.setText(boardGameList.get(position).getGameDescription());
        ((ViewHolder)holder).cardTitleGameDetail.setText(boardGameList.get(position).getGameName());
        ((ViewHolder)holder).cardDescriptionGameDetail.setText(boardGameList.get(position).getGameDescription());
    }

    public void setFilteredList(List<BoardGame> filteredList){
        this.boardGameList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return boardGameList.size();
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cardImgGameDetail;
        TextView cardTitleGameDetail;
        TextView cardDescriptionGameDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImgGameDetail = itemView.findViewById(R.id.cardImgGameDetail);
            cardTitleGameDetail = itemView.findViewById(R.id.cardTitleGameDetail);
            cardDescriptionGameDetail = itemView.findViewById(R.id.cardDescriptionGameDetail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }
    }
}
