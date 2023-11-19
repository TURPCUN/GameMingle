package com.nt.gamemingle.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nt.gamemingle.R;
import com.nt.gamemingle.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private List<Notification> notifications;

    ItemClickListener itemClickListener;

    public NotificationAdapter(List<Notification> notifications, ItemClickListener itemClickListener) {
        this.notifications = notifications;
        this.itemClickListener = itemClickListener;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String notificationMessage = notifications.get(position).getMessage();
        holder.notificationMessage.setText(notificationMessage);

        if (notifications.get(position).getRead()) {
            holder.notificationMessage.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.notificationMessage.setTypeface(null, Typeface.BOLD);
        }

        String ntTime = notifications.get(position).getTime();
        String ntDate = notifications.get(position).getDate();
        String ntDateTime = ntDate + " - " + ntTime;
        holder.notificationTime.setText(ntDateTime);

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public interface ItemClickListener {
        void makeItemReadClick(int position);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notificationImage;
        TextView notificationMessage;
        TextView notificationTime;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationImage = itemView.findViewById(R.id.imgNotification);
            notificationMessage = itemView.findViewById(R.id.txtNotificationMsg);
            notificationTime = itemView.findViewById(R.id.txtNotificationTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.makeItemReadClick(getAdapterPosition());
                }
            });
        }
    }
}
