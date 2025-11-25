package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewMarkModel {


    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("mark_id")
        @Expose
        private Integer markId;
        @SerializedName("standard_name")
        @Expose
        private String standardName;
        @SerializedName("exam_name")
        @Expose
        private String examName;
        @SerializedName("subject_name")
        @Expose
        private String subjectName;
        @SerializedName("student_name")
        @Expose
        private String studentName;
        @SerializedName("obtained_marks")
        @Expose
        private String obtainedMarks;
        @SerializedName("mark_status")
        @Expose
        private String markStatus;

        public Integer getMarkId() {
            return markId;
        }

        public void setMarkId(Integer markId) {
            this.markId = markId;
        }

        public String getStandardName() {
            return standardName;
        }

        public void setStandardName(String standardName) {
            this.standardName = standardName;
        }

        public String getExamName() {
            return examName;
        }

        public void setExamName(String examName) {
            this.examName = examName;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getObtainedMarks() {
            return obtainedMarks;
        }

        public void setObtainedMarks(String obtainedMarks) {
            this.obtainedMarks = obtainedMarks;
        }

        public String getMarkStatus() {
            return markStatus;
        }

        public void setMarkStatus(String markStatus) {
            this.markStatus = markStatus;
        }

    }
}

