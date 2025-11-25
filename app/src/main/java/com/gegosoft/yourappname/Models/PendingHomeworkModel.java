package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingHomeworkModel {

        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public class Datum {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("class_name")
            @Expose
            private String className;
            @SerializedName("subject_name")
            @Expose
            private String subjectName;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("attachment")
            @Expose
            private String attachment;
            @SerializedName("pending_count")
            @Expose
            private Integer pendingCount;
            @SerializedName("status_display")
            @Expose
            private String statusDisplay;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("comments")
            @Expose
            private String comments;

            @SerializedName("submission_date")
            @Expose
            private String submission_date;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public String getStatusDisplay() {
                return statusDisplay;
            }

            public void setStatusDisplay(String statusDisplay) {
                this.statusDisplay = statusDisplay;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getComments() {
                return comments;
            }

            public void setComments(String comments) {
                this.comments = comments;
            }

            public String getSubmission_date() {
                return submission_date;
            }

            public void setSubmission_date(String submission_date) {
                this.submission_date = submission_date;
            }

        }
    }