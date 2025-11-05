package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentSubmittedHomeworkModel {
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
        @SerializedName("user_fullname")
        @Expose
        private String userFullname;
        @SerializedName("attachments")
        @Expose
        private List<Attachment> attachments = null;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("comments")
        @Expose
        private Object comments;
        @SerializedName("replycomment")
        @Expose
        private String replycomment;

        public String getReplycomment() {
            return replycomment;
        }

        public void setReplycomment(String replycomment) {
            this.replycomment = replycomment;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserFullname() {
            return userFullname;
        }

        public void setUserFullname(String userFullname) {
            this.userFullname = userFullname;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }
        public class Attachment {

            @SerializedName("original_path")
            @Expose
            private String originalPath;
            @SerializedName("path")
            @Expose
            private String path;
            @SerializedName("id")
            @Expose
            private Integer id;

            public String getOriginalPath() {
                return originalPath;
            }

            public void setOriginalPath(String originalPath) {
                this.originalPath = originalPath;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

        }
    }
}
