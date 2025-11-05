package com.gegosoft.schoolteacherapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExamListModel {

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

        @SerializedName("exam_id")
        @Expose
        private Integer examId;
        @SerializedName("standard_id")
        @Expose
        private String standardId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("exam_standard")
        @Expose
        private String examStandard;
        @SerializedName("schedule")
        @Expose
        private List<Schedule> schedule = null;

        public Integer getExamId() {
            return examId;
        }

        public void setExamId(Integer examId) {
            this.examId = examId;
        }

        public String getStandardId() {
            return standardId;
        }

        public void setStandardId(String standardId) {
            this.standardId = standardId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExamStandard() {
            return examStandard;
        }

        public void setExamStandard(String examStandard) {
            this.examStandard = examStandard;
        }

        public List<Schedule> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<Schedule> schedule) {
            this.schedule = schedule;
        }

        public class Schedule implements Parcelable {

            @SerializedName("schedule_id")
            @Expose
            private Integer scheduleId;
            @SerializedName("exam_name")
            @Expose
            private String examName;
            @SerializedName("class")
            @Expose
            private String _class;
            @SerializedName("subject_name")
            @Expose
            private String subjectName;
            @SerializedName("exam_date")
            @Expose
            private String examDate;
            @SerializedName("portion")
            @Expose
            private String portion;
            @SerializedName("start_time")
            @Expose
            private String startTime;
            @SerializedName("end_time")
            @Expose
            private String endTime;
            @SerializedName("today_date")
            @Expose
            private String todayDate;
            @SerializedName("count")
            @Expose
            private Integer count;
            @SerializedName("upload_staus")
            @Expose
            private String uploadStaus;
            @SerializedName("present_count")
            @Expose
            private Integer presentCount;
            @SerializedName("absent_count")
            @Expose
            private Integer absentCount;
            @SerializedName("exam_time")
            @Expose
            private String examTime;

            public Integer getScheduleId() {
                return scheduleId;
            }

            public void setScheduleId(Integer scheduleId) {
                this.scheduleId = scheduleId;
            }

            public String getExamName() {
                return examName;
            }

            public void setExamName(String examName) {
                this.examName = examName;
            }

            public String getClass_() {
                return _class;
            }

            public void setClass_(String _class) {
                this._class = _class;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public String getExamDate() {
                return examDate;
            }

            public void setExamDate(String examDate) {
                this.examDate = examDate;
            }

            public String getPortion() {
                return portion;
            }

            public void setPortion(String portion) {
                this.portion = portion;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getTodayDate() {
                return todayDate;
            }

            public void setTodayDate(String todayDate) {
                this.todayDate = todayDate;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public String getUploadStaus() {
                return uploadStaus;
            }

            public void setUploadStaus(String uploadStaus) {
                this.uploadStaus = uploadStaus;
            }

            public Integer getPresentCount() {
                return presentCount;
            }

            public void setPresentCount(Integer presentCount) {
                this.presentCount = presentCount;
            }

            public Integer getAbsentCount() {
                return absentCount;
            }

            public void setAbsentCount(Integer absentCount) {
                this.absentCount = absentCount;
            }

            public String getExamTime() {
                return examTime;
            }

            public void setExamTime(String examTime) {
                this.examTime = examTime;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        }

    }
}
