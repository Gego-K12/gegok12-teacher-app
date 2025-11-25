package com.gegosoft.yourappname.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TImeTable_Model {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Datum data;

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

    public Datum getData(){
        return data;
    }

    public void setData(Datum data){
        this.data = data;
    }

    public static class Datum {
        @SerializedName("Monday")
        @Expose
        private List<subDatum> monday = null;

        @SerializedName("Tuesday")
        @Expose
        private List<subDatum> tuesday = null;

        @SerializedName("Wednesday")
        @Expose
        private List<subDatum> wednesday = null;

        @SerializedName("Thursday")
        @Expose
        private List<subDatum> thursday = null;

        @SerializedName("Friday")
        @Expose
        private List<subDatum> friday = null;


        public List<subDatum> getMonday() {
            return monday;
        }

        public void setMonday(List<subDatum> monday) {
            this.monday = monday;
        }

        public List<subDatum> getTuesday() {
            return tuesday;
        }

        public void setTuesday(List<subDatum> tuesday) {
            this.tuesday = tuesday;
        }

        public List<subDatum> getWednesday() {
            return wednesday;
        }

        public void setWednesday(List<subDatum> wednesday) {
            this.wednesday = wednesday;
        }

        public List<subDatum> getThursday() {
            return thursday;
        }

        public void setThursday(List<subDatum> thursday) {
            this.thursday = thursday;
        }

        public List<subDatum> getFriday() {
            return friday;
        }

        public void setFriday(List<subDatum> friday) {
            this.friday = friday;
        }

        public static class subDatum implements Parcelable {
            @SerializedName("subject_name")
            @Expose
            private String subjectName;

            @SerializedName("standard_name")
            @Expose
            private String standardName;

            protected subDatum(Parcel in) {
                subjectName = in.readString();
                standardName = in.readString();
            }

            public static final Creator<subDatum> CREATOR = new Creator<subDatum>() {
                @Override
                public subDatum createFromParcel(Parcel in) {
                    return new subDatum(in);
                }

                @Override
                public subDatum[] newArray(int size) {
                    return new subDatum[size];
                }
            };

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public String getStandardName() {
                return standardName;
            }

            public void setStandardName(String standardName) {
                this.standardName = standardName;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {
                dest.writeString(subjectName);
                dest.writeString(standardName);
            }
        }

    }


}
