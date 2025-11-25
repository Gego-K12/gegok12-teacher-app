package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowLeaveModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("from_date")
        @Expose
        private String fromDate;
        @SerializedName("to_date")
        @Expose
        private String toDate;
        @SerializedName("reason_id")
        @Expose
        private Object reasonId;
        @SerializedName("remarks")
        @Expose
        private Object remarks;
        @SerializedName("leave_type_id")
        @Expose
        private Object leaveTypeId;

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

        public Object getReasonId() {
            return reasonId;
        }

        public void setReasonId(Object reasonId) {
            this.reasonId = reasonId;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getLeaveTypeId() {
            return leaveTypeId;
        }

        public void setLeaveTypeId(Object leaveTypeId) {
            this.leaveTypeId = leaveTypeId;
        }

    }

}
