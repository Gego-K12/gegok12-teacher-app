package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoRoomSubjectModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
        @SerializedName("school_id")
        @Expose
        private Integer schoolId;
        @SerializedName("academic_year_id")
        @Expose
        private Integer academicYearId;
        @SerializedName("standard_id")
        @Expose
        private Object standardId;
        @SerializedName("subject_id")
        @Expose
        private Integer subjectId;
        @SerializedName("subject_name")
        @Expose
        private String subjectName;

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

        public Object getStandardId() {
            return standardId;
        }

        public void setStandardId(Object standardId) {
            this.standardId = standardId;
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

