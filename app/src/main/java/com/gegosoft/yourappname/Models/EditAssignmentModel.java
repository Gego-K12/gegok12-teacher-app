package com.gegosoft.yourappname.Models;

import com.google.gson.annotations.SerializedName;

public class EditAssignmentModel {
    @SerializedName("success")
    private String success;
    @SerializedName("message")
    private String message;

    public EditAssignmentModel(String success, String message) {
        this.success = success;
        this.message = message;
    }
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
