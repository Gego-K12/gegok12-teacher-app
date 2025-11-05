package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassNoticeBoardModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose

    private List<Datum> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("class_name")
        @Expose
        private String className;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("publish_date")
        @Expose
        private String publishDate;
        @SerializedName("expire_date")
        @Expose
        private String expireDate;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("backgroundimage")
        @Expose
        private String backgroundimage;
        @SerializedName("attachment_file")
        @Expose
        private String attachmentFile;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBackgroundimage() {
            return backgroundimage;
        }

        public void setBackgroundimage(String backgroundimage) {
            this.backgroundimage = backgroundimage;
        }

        public String getAttachmentFile() {
            return attachmentFile;
        }

        public void setAttachmentFile(String attachmentFile) {
            this.attachmentFile = attachmentFile;
        }

    }
}
