package com.gegosoft.schoolteacherapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyActiveTaskListModel {

    private List<Datum> task = null;


    public List<Datum> getTask() {
        return task;
    }

    public void setTask(List<Datum> task) {
        this.task = task;
    }

    public class Datum implements Parcelable {

        @SerializedName("task_id")
        @Expose
        private Integer taskId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("to_do_list")
        @Expose
        private String toDoList;
        @SerializedName("task_date")
        @Expose
        private String taskDate;
        @SerializedName("task_flag")
        @Expose
        private Integer taskFlag;
        @SerializedName("task_status")
        @Expose
        private Integer taskStatus;
        @SerializedName("assignee")
        @Expose
        private String assignee;
        @SerializedName("assignee_display")
        @Expose
        private String assigneeDisplay;
        @SerializedName("standardLink")
        @Expose
        private Object standardLink;
        @SerializedName("user_name")
        @Expose
        private List<UserName> userName = null;
        @SerializedName("snooze")
        @Expose
        private Integer snooze;
        @SerializedName("reminder_date")
        @Expose
        private String reminderDate;
        @SerializedName("auth_id")
        @Expose
        private Integer authId;
        @SerializedName("created_by")
        @Expose
        private Integer createdBy;

        protected Datum(Parcel in) {
            if (in.readByte() == 0) {
                taskId = null;
            } else {
                taskId = in.readInt();
            }
            title = in.readString();
            toDoList = in.readString();
            taskDate = in.readString();
            if (in.readByte() == 0) {
                taskFlag = null;
            } else {
                taskFlag = in.readInt();
            }
            if (in.readByte() == 0) {
                taskStatus = null;
            } else {
                taskStatus = in.readInt();
            }
            assignee = in.readString();
            assigneeDisplay = in.readString();
            if (in.readByte() == 0) {
                snooze = null;
            } else {
                snooze = in.readInt();
            }
            reminderDate = in.readString();
            if (in.readByte() == 0) {
                authId = null;
            } else {
                authId = in.readInt();
            }
            if (in.readByte() == 0) {
                createdBy = null;
            } else {
                createdBy = in.readInt();
            }
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (taskId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(taskId);
            }
            dest.writeString(title);
            dest.writeString(toDoList);
            dest.writeString(taskDate);
            if (taskFlag == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(taskFlag);
            }
            if (taskStatus == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(taskStatus);
            }
            dest.writeString(assignee);
            dest.writeString(assigneeDisplay);
            if (snooze == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(snooze);
            }
            dest.writeString(reminderDate);
            if (authId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(authId);
            }
            if (createdBy == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(createdBy);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public  final Creator<Datum> CREATOR = new Creator<Datum>() {
            @Override
            public Datum createFromParcel(Parcel in) {
                return new Datum(in);
            }

            @Override
            public Datum[] newArray(int size) {
                return new Datum[size];
            }
        };

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getToDoList() {
            return toDoList;
        }

        public void setToDoList(String toDoList) {
            this.toDoList = toDoList;
        }

        public String getTaskDate() {
            return taskDate;
        }

        public void setTaskDate(String taskDate) {
            this.taskDate = taskDate;
        }

        public Integer getTaskFlag() {
            return taskFlag;
        }

        public void setTaskFlag(Integer taskFlag) {
            this.taskFlag = taskFlag;
        }

        public Integer getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(Integer taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }

        public String getAssigneeDisplay() {
            return assigneeDisplay;
        }

        public void setAssigneeDisplay(String assigneeDisplay) {
            this.assigneeDisplay = assigneeDisplay;
        }

        public Object getStandardLink() {
            return standardLink;
        }

        public void setStandardLink(Object standardLink) {
            this.standardLink = standardLink;
        }

        public List<UserName> getUserName() {
            return userName;
        }

        public void setUserName(List<UserName> userName) {
            this.userName = userName;
        }

        public Integer getSnooze() {
            return snooze;
        }

        public void setSnooze(Integer snooze) {
            this.snooze = snooze;
        }

        public String getReminderDate() {
            return reminderDate;
        }

        public void setReminderDate(String reminderDate) {
            this.reminderDate = reminderDate;
        }

        public Integer getAuthId() {
            return authId;
        }

        public void setAuthId(Integer authId) {
            this.authId = authId;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public class UserName {

            @SerializedName("user_name")
            @Expose
            private String userName;
            @SerializedName("user")
            @Expose
            private String user;

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }

        }

    }
}
