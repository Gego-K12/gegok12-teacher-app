package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InvitesModel {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum implements Serializable {

        @SerializedName("vidoe_conference_user_id")
        @Expose
        private Integer vidoeConferenceUserId;
        @SerializedName("user_fullname")
        @Expose
        private String userFullname;
        @SerializedName("user_email")
        @Expose
        private String userEmail;
        @SerializedName("roll_number")
        @Expose
        private String rollNumber;

        private boolean isAdmitted = false;

        public boolean isAdmitted() {
            return isAdmitted;
        }

        public void setAdmitted(boolean admitted) {
            isAdmitted = admitted;
        }

        public Integer getVidoeConferenceUserId() {
            return vidoeConferenceUserId;
        }

        public void setVidoeConferenceUserId(Integer vidoeConferenceUserId) {
            this.vidoeConferenceUserId = vidoeConferenceUserId;
        }

        public String getUserFullname() {
            return userFullname;
        }

        public void setUserFullname(String userFullname) {
            this.userFullname = userFullname;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }

    }

}
