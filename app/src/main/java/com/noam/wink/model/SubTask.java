package com.noam.wink.model;

import java.io.Serializable;

public class SubTask implements Serializable {

    private String subTaskName;
    private int subTaskTime;
    private long doneSubTaskTime;
    private boolean isDone = false;


    public SubTask() {
    }

    public SubTask(String subTaskName, int subTaskTime) {
        this.subTaskName = subTaskName;
        this.subTaskTime = subTaskTime;
    }


    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public int getSubTaskTime() {
        return subTaskTime;
    }

    public void setSubTaskTime(int subTaskTime) {
        this.subTaskTime = subTaskTime;
    }

    public long getDoneSubTaskTime() {
        return doneSubTaskTime;
    }

    public void setDoneSubTaskTime(long doneSubTaskTime) {
        this.doneSubTaskTime = doneSubTaskTime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
