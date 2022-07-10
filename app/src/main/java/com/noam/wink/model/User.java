package com.noam.wink.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    String userName = "";
    List<Task> taskProgressList = new ArrayList<>(), taskDoneList = new ArrayList<>();

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Task> getTaskProgressList() {
        return taskProgressList;
    }

    public void setTaskProgressList(List<Task> taskProgressList) {
        this.taskProgressList = taskProgressList;
    }

    public List<Task> getTaskDoneList() {
        return taskDoneList;
    }

    public void setTaskDoneList(List<Task> taskDoneList) {
        this.taskDoneList = taskDoneList;
    }
}
