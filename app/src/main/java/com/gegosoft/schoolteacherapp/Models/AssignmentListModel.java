package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignmentListModel {

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
        @SerializedName("class")
        @Expose
        private String _class;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("assigned_date")
        @Expose
        private String assignedDate;
        @SerializedName("submission_date")
        @Expose
        private String submissionDate;
        @SerializedName("attachment")
        @Expose
        private String attachment;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("studentAssignmentStatus")
        @Expose
        private Integer studentAssignmentStatus;
        @SerializedName("assignment_file")
        @Expose
        private AssignmentFile assignmentFile;
        @SerializedName("marks")
        @Expose
        private Integer marks;
        @SerializedName("student_assignment_id")
        @Expose
        private Object studentAssignmentId;

        @SerializedName("comments")
        @Expose
        private String comments;

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        @SerializedName("subject")
        @Expose
        private String subject;


        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getClass_() {
            return _class;
        }

        public void setClass_(String _class) {
            this._class = _class;
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

        public String getAssignedDate() {
            return assignedDate;
        }

        public void setAssignedDate(String assignedDate) {
            this.assignedDate = assignedDate;
        }

        public String getSubmissionDate() {
            return submissionDate;
        }

        public void setSubmissionDate(String submissionDate) {
            this.submissionDate = submissionDate;
        }

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getStudentAssignmentStatus() {
            return studentAssignmentStatus;
        }

        public void setStudentAssignmentStatus(Integer studentAssignmentStatus) {
            this.studentAssignmentStatus = studentAssignmentStatus;
        }

        public AssignmentFile getAssignmentFile() {
            return assignmentFile;
        }

        public void setAssignmentFile(AssignmentFile assignmentFile) {
            this.assignmentFile = assignmentFile;
        }

        public Integer getMarks() {
            return marks;
        }

        public void setMarks(Integer marks) {
            this.marks = marks;
        }

        public Object getStudentAssignmentId() {
            return studentAssignmentId;
        }

        public void setStudentAssignmentId(Object studentAssignmentId) {
            this.studentAssignmentId = studentAssignmentId;
        }

        public class AssignmentFile{

        }

    }

}
