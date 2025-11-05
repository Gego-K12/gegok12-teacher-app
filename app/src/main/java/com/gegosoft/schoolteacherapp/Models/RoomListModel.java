package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RoomListModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("roomId")
        @Expose
        private String roomId;
        @SerializedName("composeId")
        @Expose
        private Object composeId;
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("created_by")
        @Expose
        private Integer created_by;

        @SerializedName("subject")
        @Expose
        private String subject;

        @SerializedName("joining_date")
        @Expose
        private String joining_date;

        @SerializedName("start_time")
        @Expose
        private String start_time;

        @SerializedName("duration")
        @Expose
        private String duration;

        @SerializedName("end_time")
        @Expose
        private String end_time;


        @SerializedName("standard_name")
        @Expose
        private String standard_name;

        @SerializedName("avatar")
        @Expose
        private String avatar;

        public String getStandard_name() {
            return standard_name;
        }

        public void setStandard_name(String standard_name) {
            this.standard_name = standard_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public Object getComposeId() {
            return composeId;
        }

        public void setComposeId(Object composeId) {
            this.composeId = composeId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getCreated_by() {
            return created_by;
        }

        public void setCreated_by(Integer created_by) {
            this.created_by = created_by;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getJoining_date() {
            return joining_date;
        }

        public void setJoining_date(String joining_date) {
            this.joining_date = joining_date;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }

}
