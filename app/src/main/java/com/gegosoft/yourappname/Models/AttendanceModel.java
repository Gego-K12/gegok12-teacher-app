package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceModel {
    @SerializedName("standardlist")
    @Expose
    private List<Standardlist> standardlist = null;
    @SerializedName("studentlist")
    @Expose
    private List<Studentlist> studentlist = null;
    @SerializedName("absentReasonlist")
    @Expose
    private List<AbsentReasonlist> absentReasonlist = null;

    public List<Standardlist> getStandardlist() {
        return standardlist;
    }

    public void setStandardlist(List<Standardlist> standardlist) {
        this.standardlist = standardlist;
    }

    public List<Studentlist> getStudentlist() {
        return studentlist;
    }

    public void setStudentlist(List<Studentlist> studentlist) {
        this.studentlist = studentlist;
    }

    public List<AbsentReasonlist> getAbsentReasonlist() {
        return absentReasonlist;
    }

    public void setAbsentReasonlist(List<AbsentReasonlist> absentReasonlist) {
        this.absentReasonlist = absentReasonlist;
    }
    public class Standardlist {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("standard_name")
        @Expose
        private String standardName;
        @SerializedName("section_name")
        @Expose
        private String sectionName;
        @SerializedName("standard_section")
        @Expose
        private String standardSection;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStandardName() {
            return standardName;
        }

        public void setStandardName(String standardName) {
            this.standardName = standardName;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public String getStandardSection() {
            return standardSection;
        }

        public void setStandardSection(String standardSection) {
            this.standardSection = standardSection;
        }

    }
    public class Studentlist {


        @SerializedName("standardLink_id")
        @Expose
        private Integer standardLinkId;
        @SerializedName("student_id")
        @Expose
        private Integer studentId;
        @SerializedName("student_name")
        @Expose
        private String studentName;
        @SerializedName("attendance")
        @Expose

        private List<Attendance> attendance = null;
        @SerializedName("avatar")
        @Expose
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public List<Attendance> getAttendance() {
            return attendance;
        }

        public void setAttendance(List<Attendance> attendance) {
            this.attendance = attendance;
        }
        public class Attendance {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("session")
            @Expose
            private String session;
            @SerializedName("reason")
            @Expose
            private Object reason;
            @SerializedName("remarks")
            @Expose
            private Object remarks;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getSession() {
                return session;
            }

            public void setSession(String session) {
                this.session = session;
            }

            public Object getReason() {
                return reason;
            }

            public void setReason(Object reason) {
                this.reason = reason;
            }

            public Object getRemarks() {
                return remarks;
            }

            public void setRemarks(Object remarks) {
                this.remarks = remarks;
            }

        }
    }
    public class AbsentReasonlist {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;

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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

    }
}
