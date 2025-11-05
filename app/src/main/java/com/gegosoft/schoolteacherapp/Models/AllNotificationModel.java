package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllNotificationModel {
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
        private String id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("notifiable_type")
        @Expose
        private String notifiableType;
        @SerializedName("notifiable_id")
        @Expose
        private Integer notifiableId;
        @SerializedName("data_message")
        @Expose
        private String dataMessage;
        @SerializedName("read_at")
        @Expose
        private Object readAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotifiableType() {
            return notifiableType;
        }

        public void setNotifiableType(String notifiableType) {
            this.notifiableType = notifiableType;
        }

        public Integer getNotifiableId() {
            return notifiableId;
        }

        public void setNotifiableId(Integer notifiableId) {
            this.notifiableId = notifiableId;
        }

        public String getDataMessage() {
            return dataMessage;
        }

        public void setDataMessage(String dataMessage) {
            this.dataMessage = dataMessage;
        }

        public Object getReadAt() {
            return readAt;
        }

        public void setReadAt(Object readAt) {
            this.readAt = readAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}
