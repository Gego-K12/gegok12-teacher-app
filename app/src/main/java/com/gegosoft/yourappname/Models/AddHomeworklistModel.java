package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddHomeworklistModel {
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

    }
}
