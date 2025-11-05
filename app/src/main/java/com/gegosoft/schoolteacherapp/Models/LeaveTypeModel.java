package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveTypeModel {
    @SerializedName("leavelist")
    @Expose
    private List<Leave> leavelist = null;
    @SerializedName("reasonlist")
    @Expose
    private List<Reason> reasonlist = null;
    @SerializedName("session")
    @Expose
    private List<Session> session = null;

    public List<Leave> getLeavelist() {
        return leavelist;
    }

    public void setLeavelist(List<Leave> leavelist) {
        this.leavelist = leavelist;
    }

    public List<Reason> getReasonlist() {
        return reasonlist;
    }

    public void setReasonlist(List<Reason> reasonlist) {
        this.reasonlist = reasonlist;
    }

    public List<Session> getSession() {
        return session;
    }

    public void setSession(List<Session> session) {
        this.session = session;
    }
    public class Leave {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("school_id")
        @Expose
        private Integer schoolId;
        @SerializedName("academic_year_id")
        @Expose
        private Integer academicYearId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("max_no_of_days")
        @Expose
        private String maxNoOfDays;
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

        public Integer getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(Integer schoolId) {
            this.schoolId = schoolId;
        }

        public Integer getAcademicYearId() {
            return academicYearId;
        }

        public void setAcademicYearId(Integer academicYearId) {
            this.academicYearId = academicYearId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMaxNoOfDays() {
            return maxNoOfDays;
        }

        public void setMaxNoOfDays(String maxNoOfDays) {
            this.maxNoOfDays = maxNoOfDays;
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
    public class Reason {

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
    public class Session {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}


