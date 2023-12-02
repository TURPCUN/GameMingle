package com.nt.gamemingle.model;

public class Report {

    private String reportId;
    private String eventId;
    private String reporterId;
    private String reportReason;
    private ChatMessage chatMessage;
    private Boolean isRead;
    private String actionComment;

    public Report() {
    }

    public Report(String reportId, String eventId, String reporterId, String reportReason, ChatMessage chatMessage) {
        this.reportId = reportId;
        this.eventId = eventId;
        this.reporterId = reporterId;
        this.reportReason = reportReason;
        this.chatMessage = chatMessage;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getActionComment() {
        return actionComment;
    }

    public void setActionComment(String actionComment) {
        this.actionComment = actionComment;
    }
}
