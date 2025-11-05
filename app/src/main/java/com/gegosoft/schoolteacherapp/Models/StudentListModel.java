package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentListModel {
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
    public class Datum {

        @SerializedName("standardLink_id")
        @Expose
        private Integer standardLinkId;
        @SerializedName("class")
        @Expose
        private String _class;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        private boolean isStudentSelected = false;

        public Integer getStandardLinkId() {
            return standardLinkId;
        }

        public void setStandardLinkId(Integer standardLinkId) {
            this.standardLinkId = standardLinkId;
        }

        public String getClass_() {
            return _class;
        }

        public void setClass_(String _class) {
            this._class = _class;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public boolean isStudentSelected() {
            return isStudentSelected;
        }

        public void setStudentSelected(boolean studentSelected) {
            isStudentSelected = studentSelected;
        }
    }
}
