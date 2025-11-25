package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowHomeworklistModel {
    @SerializedName("standardLink_id")
    @Expose
    private Integer standardLinkId;
    @SerializedName("subject_id")
    @Expose
    private Integer subjectId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("pending_count")
    @Expose
    private Integer pendingCount;

    public Integer getStandardLinkId() {
        return standardLinkId;
    }

    public void setStandardLinkId(Integer standardLinkId) {
        this.standardLinkId = standardLinkId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }
}
