package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceAbsentiesModel {
   private int Standardlinkid;
   private String Session;
   private String Date;
   private List<Students>AbsentList;
   private List<Students>PresentList;

    public int getStandardlinkid() {
        return Standardlinkid;
    }

    public void setStandardlinkid(int standardlinkid) {
        Standardlinkid = standardlinkid;
    }

    public String getSession() {
        return Session;
    }

    public void setSession(String session) {
        Session = session;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public List<Students> getAbsentList() {
        return AbsentList;
    }

    public void setAbsentList(List<Students> absentList) {
        AbsentList = absentList;
    }

    public List<Students> getPresentList() {
        return PresentList;
    }

    public void setPresentList(List<Students> presentList) {
        PresentList = presentList;
    }

    public static class Students {

        @SerializedName("standardLink_id")
        @Expose
        private Integer standardLinkId;
        @SerializedName("student_id")
        @Expose
        private Integer studentId;
        @SerializedName("student_name")
        @Expose
        private String studentName;
        private String remarks;
        private Integer reasonId;

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Integer getReasonId() {
            return reasonId;
        }

        public void setReasonId(Integer reasonId) {
            this.reasonId = reasonId;
        }

        public Integer getStandardLinkId() {
            return standardLinkId;
        }

        public void setStandardLinkId(Integer standardLinkId) {
            this.standardLinkId = standardLinkId;
        }

        public Integer getStudentId() {
            return studentId;
        }

        public void setStudentId(Integer studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

    }

}
