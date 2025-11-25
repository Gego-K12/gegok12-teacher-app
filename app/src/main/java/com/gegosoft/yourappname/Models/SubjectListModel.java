package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectListModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> Subjectdata = null;

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

    public List<SubjectListModel.Datum> getSubjectdata() {
        return Subjectdata;
    }

    public void setSubjectdata(List<Datum> data) {
        this.Subjectdata = data;
    }
    public class Datum {

        @SerializedName("standardLink_id")
        @Expose
        private Integer standardLinkId;
        @SerializedName("subject_id")
        @Expose
        private Integer subjectId;
        @SerializedName("subject_name")
        @Expose
        private String subjectName;

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

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

    }
}
