package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentSubmittedAssignmentModel {

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
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("roll_number")
        @Expose
        private String rollNumber;
        @SerializedName("assignment_file")
        @Expose
        private List<AssignmentFile> assignmentFile = null;
        @SerializedName("submitted_on")
        @Expose
        private String submittedOn;
        @SerializedName("total_marks")
        @Expose
        private Integer totalMarks;
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("user_avatar")
        @Expose
        private String user_avatar;

        @SerializedName("title")
        @Expose
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }

        public List<AssignmentFile> getAssignmentFile() {
            return assignmentFile;
        }

        public void setAssignmentFile(List<AssignmentFile> assignmentFile) {
            this.assignmentFile = assignmentFile;
        }

        public String getSubmittedOn() {
            return submittedOn;
        }

        public void setSubmittedOn(String submittedOn) {
            this.submittedOn = submittedOn;
        }

        public Integer getTotalMarks() {
            return totalMarks;
        }

        public void setTotalMarks(Integer totalMarks) {
            this.totalMarks = totalMarks;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class AssignmentFile {

        @SerializedName("original_path")
        @Expose
        private String originalPath;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getOriginalPath() {
            return originalPath;
        }

        public void setOriginalPath(String originalPath) {
            this.originalPath = originalPath;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }
}