package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddMarkStudentListModel {


    @SerializedName("students")
    @Expose
    private List<Student> students = null;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public class Student {

        @SerializedName("standardLink_id")
        @Expose
        private Integer standardLinkId;
        @SerializedName("class")
        @Expose
        private String _class;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("avatar")
        @Expose
        private String avatar;

        private String marks;

        private String status;

        public String getMarks() {
            return marks;
        }

        public void setMarks(String marks) {
            this.marks = marks;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getStandardLinkId() {
            return standardLinkId;
        }

        public void setStandardLinkId(Integer standardLinkId) {
            this.standardLinkId = standardLinkId;
        }

        public String getClass_() {
            return _class;
        }

        public void setClass_(String _class) {
            this._class = _class;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
