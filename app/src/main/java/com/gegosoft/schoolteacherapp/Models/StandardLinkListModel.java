package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandardLinkListModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> standarddata = null;

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

    public List<StandardLinkListModel.Datum> getStandarddata() {
        return standarddata;
    }

    public void setStandarddata(List<Datum> data) {
        this.standarddata = data;
    }
    public class Datum {

        @SerializedName("standardLink_id")
        @Expose
        private Integer standardLinkId;
        @SerializedName("standard_section")
        @Expose
        private String standardSection;

        public int getStandardLinkId() {
            return standardLinkId;
        }

        public void setStandardLinkId(Integer standardLinkId) {
            this.standardLinkId = standardLinkId;
        }

        public String getStandardSection() {
            return standardSection;
        }

        public void setStandardSection(String standardSection) {
            this.standardSection = standardSection;
        }

    }
}
