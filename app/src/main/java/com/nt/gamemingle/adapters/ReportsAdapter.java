package com.nt.gamemingle.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt.gamemingle.databinding.ReportLayoutBinding;
import com.nt.gamemingle.model.Report;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter{

    ArrayList<Report> reportsList;
    ItemClickListener itemClickListener;

    public ReportsAdapter(ArrayList<Report> reportsList, ItemClickListener itemClickListener){
        this.reportsList = reportsList;
        this.itemClickListener = itemClickListener;
    }

    public void setReportsList(ArrayList<Report> reportsList){
        this.reportsList = reportsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReportLayoutBinding binding = ReportLayoutBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();
        ViewHolder holder = new ViewHolder(view, binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String reportId = reportsList.get(position).getReportId();
        String eventId = reportsList.get(position).getEventId();
        String reporterId = reportsList.get(position).getReporterId();
        String reportReason = reportsList.get(position).getReportReason();
        String chatMessageId = reportsList.get(position).getChatMessage().getChatMessageId();
        String message = reportsList.get(position).getChatMessage().getMessage();
        String messageTime = reportsList.get(position).getChatMessage().getMessageTime();
        String messageSenderId = reportsList.get(position).getChatMessage().getSender().getUserId();
        String messageDate = reportsList.get(position).getChatMessage().getMessageDate();
        String senderName = reportsList.get(position).getChatMessage().getSender().getFullName();

        ((ViewHolder)holder).holderBinding.reportId.setText(reportId);
        ((ViewHolder)holder).holderBinding.eventId.setText(eventId);
        ((ViewHolder)holder).holderBinding.reporterId.setText(reporterId);
        ((ViewHolder)holder).holderBinding.reportReason.setText(reportReason);
        ((ViewHolder)holder).holderBinding.chatMessageId.setText(chatMessageId);
        ((ViewHolder)holder).holderBinding.message.setText(message);
        ((ViewHolder)holder).holderBinding.chatMessageTime.setText(messageTime);
        ((ViewHolder)holder).holderBinding.chatMessageSenderId.setText(messageSenderId);
        ((ViewHolder)holder).holderBinding.chatMessageDate.setText(messageDate);
        ((ViewHolder)holder).holderBinding.chatMessageSenderName.setText(senderName);

        try{
        if (reportsList.get(position).getIsRead()) {
            ((ViewHolder) holder).holderBinding.btnSendWarning.setVisibility(View.GONE);
            ((ViewHolder) holder).holderBinding.actionComment.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).holderBinding.actionComment.setText(reportsList.get(position).getActionComment());
            ((ViewHolder)holder).holderBinding.reportIdTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.eventIdTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.reporterIdTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.reportReasonTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.chatMessageIdTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.messageTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.chatMessageTimeTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.chatMessageSenderIdTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.chatMessageDateTxt.setTextColor(Color.GRAY);
            ((ViewHolder)holder).holderBinding.chatMessageSenderNameTxt.setTextColor(Color.GRAY);
        }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public interface ItemClickListener{
        void onSendWarningClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ReportLayoutBinding holderBinding;
        public ViewHolder(@NonNull View itemView, ReportLayoutBinding holderBinding) {
            super(itemView);
            this.holderBinding = holderBinding;
            this.holderBinding.btnSendWarning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onSendWarningClicked(getAdapterPosition());
                }
            });
        }
    }
}
