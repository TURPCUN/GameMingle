package com.nt.gamemingle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nt.gamemingle.R;
import com.nt.gamemingle.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String chatMessage = chatMessages.get(position).getMessage();
        holder.chatMessage.setText(chatMessage);

        String chatSender = chatMessages.get(position).getSender().getFullName();
        holder.chatSender.setText(chatSender);

        String chatTime = chatMessages.get(position).getMessageTime();
        if(chatTime == null) {
            chatTime = "00:00:00";
        }
        String shortChatTime = chatTime.substring(0, 5);
        holder.chatTime.setText(shortChatTime);

        Glide.with(holder.chatImage.getContext())
                .load(chatMessages.get(position).getSender().getUserProfileImageUrl())
                .placeholder(R.drawable.loading_gif)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.icon)
                .into(holder.chatImage);

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView chatMessage;
        public TextView chatSender;
        public TextView chatTime;
        public ImageView chatImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            chatMessage = itemView.findViewById(R.id.recycler_view_text_view_chat);
            chatSender = itemView.findViewById(R.id.text_sender_name_chat);
            chatTime = itemView.findViewById(R.id.text_sent_time);
            chatImage = itemView.findViewById(R.id.img_sender_profile_chat);
        }
    }
}
