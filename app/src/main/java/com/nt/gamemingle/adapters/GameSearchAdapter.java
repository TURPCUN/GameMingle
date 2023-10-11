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
        View view = inflater.inflate(R.layout.small_game_lay_search, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // TODO. Img should come with url
        ((ViewHolder)holder).imgSmall.setImageResource(R.drawable.icon);
        ((ViewHolder)holder).tvTitle.setText(boardGameList.get(position).getGameName());
        ((ViewHolder)holder).tvDescription.setText(boardGameList.get(position).getGameDescription());
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

        ImageView imgSmall;
        TextView tvTitle;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // TODO
            imgSmall = itemView.findViewById(R.id.imgSmallGame);
            tvTitle = itemView.findViewById(R.id.textViewGameTitleSearch);
            tvDescription = itemView.findViewById(R.id.textViewGameDescSearch);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }
    }

}
