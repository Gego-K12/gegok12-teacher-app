package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestedLeaveListModel {
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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("teacher_name")
        @Expose
        private String teacherName;
        @SerializedName("from_date")
        @Expose
        private String fromDate;
        @SerializedName("to_date")
        @Expose
        private String toDate;
        @SerializedName("reason")
        @Expose
        private String reason;
        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("leave_type")
        @Expose
        private String leaveType;
        @SerializedName("approved_by")
        @Expose
        private Object approvedBy;
        @SerializedName("approved_on")
        @Expose
        private Object approvedOn;
        @SerializedName("comments")
        @Expose
        private Object comments;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("session")
        @Expose
        private String session;

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public String getLeave_days() {
            return leave_days;
        }

        public void setLeave_days(String leave_days) {
            this.leave_days = leave_days;
        }

        @SerializedName("leave_days")
        @Expose
        private String leave_days;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(String leaveType) {
            this.leaveType = leaveType;
        }

        public Object getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(Object approvedBy) {
            this.approvedBy = approvedBy;
        }

        public Object getApprovedOn() {
            return approvedOn;
        }

        public void setApprovedOn(Object approvedOn) {
            this.approvedOn = approvedOn;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
