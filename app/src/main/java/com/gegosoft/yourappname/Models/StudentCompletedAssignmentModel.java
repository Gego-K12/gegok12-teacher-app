package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentCompletedAssignmentModel {

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
        @SerializedName("user_avatar")
        @Expose
        private String userAvatar;
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
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("subject")
        @Expose
        private String subject;
        @SerializedName("obtained_marks")
        @Expose
        private Integer obtainedMarks;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("marks_given_on")
        @Expose
        private String marksGivenOn;
        @SerializedName("status")
        @Expose
        private String status;

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

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public Integer getObtainedMarks() {
            return obtainedMarks;
        }

        public void setObtainedMarks(Integer obtainedMarks) {
            this.obtainedMarks = obtainedMarks;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getMarksGivenOn() {
            return marksGivenOn;
        }

        public void setMarksGivenOn(String marksGivenOn) {
            this.marksGivenOn = marksGivenOn;
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