package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentList {
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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("mobile_no")
        @Expose
        private Object mobileNo;
        @SerializedName("class")
        @Expose
        private String _class;
        @SerializedName("roll_number")
        @Expose
        private String rollNumber;

        private boolean isStudentSelected = false;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(Object mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getClass_() {
            return _class;
        }

        public void setClass_(String _class) {
            this._class = _class;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }

        public String get_class() {
            return _class;
        }

        public void set_class(String _class) {
            this._class = _class;
        }

        public boolean isStudentSelected() {
            return isStudentSelected;
        }

        public void setStudentSelected(boolean studentSelected) {
            isStudentSelected = studentSelected;
        }
    }

}
