package com.gegosoft.schoolteacherapp.Models;

public class NotificationPoJo {
    private boolean isPrivateMessage;
    private boolean isHomeWork;

    public boolean isPrivateMessage() {
        return isPrivateMessage;
    }

    public void setPrivateMessage(boolean privateMessage) {
        isPrivateMessage = privateMessage;
    }

    public boolean isHomeWork() {
        return isHomeWork;
    }

    public void setHomeWork(boolean homeWork) {
        isHomeWork = homeWork;
    }
}
