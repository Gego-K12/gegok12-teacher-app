package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToDolistAdd_SpinnerModel {

    @SerializedName("task_assignee_list")
    @Expose
    private List<TaskAssignee> taskAssigneeList = null;
    @SerializedName("task_reminder_list")
    @Expose
    private List<TaskReminder> taskReminderList = null;
    @SerializedName("standardlinks")
    @Expose
    private List<Standardlink> standardlinks = null;

    public List<TaskAssignee> getTaskAssigneeList() {
        return taskAssigneeList;
    }

    public void setTaskAssigneeList(List<TaskAssignee> taskAssigneeList) {
        this.taskAssigneeList = taskAssigneeList;
    }

    public List<TaskReminder> getTaskReminderList() {
        return taskReminderList;
    }

    public void setTaskReminderList(List<TaskReminder> taskReminderList) {
        this.taskReminderList = taskReminderList;
    }

    public List<Standardlink> getStandardlinks() {
        return standardlinks;
    }

    public void setStandardlinks(List<Standardlink> standardlinks) {
        this.standardlinks = standardlinks;
    }
    public class Standardlink {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("standard_section")
        @Expose
        private String standardSection;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStandardSection() {
            return standardSection;
        }

        public void setStandardSection(String standardSection) {
            this.standardSection = standardSection;
        }

    }
    public class TaskAssignee {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class TaskReminder {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}

