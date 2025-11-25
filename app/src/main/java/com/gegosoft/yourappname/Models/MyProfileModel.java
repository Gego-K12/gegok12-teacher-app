package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyProfileModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("dateOfBirth")
        @Expose
        private String dateOfBirth;
        @SerializedName("employeeId")
        @Expose
        private String employeeId;
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("subDesignation")
        @Expose
        private Object subDesignation;
        @SerializedName("mobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("emailId")
        @Expose
        private String emailId;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("bloodGroup")
        @Expose
        private String bloodGroup;
        @SerializedName("aadharNumber")
        @Expose
        private Object aadharNumber;
        @SerializedName("maritalStatus")
        @Expose
        private Object maritalStatus;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("joiningDate")
        @Expose
        private String joiningDate;
        @SerializedName("ugDegree")
        @Expose
        private Integer ugDegree;
        @SerializedName("pgDegree")
        @Expose
        private Integer pgDegree;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("additionalCertificateId")
        @Expose
        private List<AdditionalCertificateId> additionalCertificateId = null;
        @SerializedName("otherAdditionalCertificateId")
        @Expose
        private Object otherAdditionalCertificateId;
        @SerializedName("notes")
        @Expose
        private Object notes;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("classTeacher")
        @Expose
        private String classTeacher;
        @SerializedName("permissions")
        @Expose
        private List<String> permissions = null;

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public Object getSubDesignation() {
            return subDesignation;
        }

        public void setSubDesignation(Object subDesignation) {
            this.subDesignation = subDesignation;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public Object getAadharNumber() {
            return aadharNumber;
        }

        public void setAadharNumber(Object aadharNumber) {
            this.aadharNumber = aadharNumber;
        }

        public Object getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(Object maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getJoiningDate() {
            return joiningDate;
        }

        public void setJoiningDate(String joiningDate) {
            this.joiningDate = joiningDate;
        }

        public Integer getUgDegree() {
            return ugDegree;
        }

        public void setUgDegree(Integer ugDegree) {
            this.ugDegree = ugDegree;
        }

        public Integer getPgDegree() {
            return pgDegree;
        }

        public void setPgDegree(Integer pgDegree) {
            this.pgDegree = pgDegree;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public List<AdditionalCertificateId> getAdditionalCertificateId() {
            return additionalCertificateId;
        }

        public void setAdditionalCertificateId(List<AdditionalCertificateId> additionalCertificateId) {
            this.additionalCertificateId = additionalCertificateId;
        }

        public Object getOtherAdditionalCertificateId() {
            return otherAdditionalCertificateId;
        }

        public void setOtherAdditionalCertificateId(Object otherAdditionalCertificateId) {
            this.otherAdditionalCertificateId = otherAdditionalCertificateId;
        }

        public Object getNotes() {
            return notes;
        }

        public void setNotes(Object notes) {
            this.notes = notes;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getClassTeacher() {
            return classTeacher;
        }

        public void setClassTeacher(String classTeacher) {
            this.classTeacher = classTeacher;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
        public class AdditionalCertificateId {

            @SerializedName("qualification_id")
            @Expose
            private Integer qualificationId;

            public Integer getQualificationId() {
                return qualificationId;
            }

            public void setQualificationId(Integer qualificationId) {
                this.qualificationId = qualificationId;
            }

        }
    }

}
