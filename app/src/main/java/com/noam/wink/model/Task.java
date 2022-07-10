package com.noam.wink.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {

    private String taskName;
    private int taskTime;
    private String feedback = "";
    private int rate = 0;
    private List<SubTask> subTasks = new ArrayList<>();
    private boolean isDone = false;

    public Task() {
    }

    public Task(String taskName, int taskTime) {
        this.taskName = taskName;
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int  getTimeSubTasks() {
        int sum = 0;
        for (int i = 0; i < getSubTasks().size(); i++) {
            sum += getSubTasks().get(i).getSubTaskTime();
        }
        return sum;
    }

    public boolean isAllFinished(){
        for (int i = 0; i < subTasks.size(); i++) {
            if (!subTasks.get(i).isDone()){
                return false;
            }
        }
        return true;
    }


    public long  getDoneTimeSubTasks() {
        long sum = 0;
        for (int i = 0; i < getSubTasks().size(); i++) {
            sum += getSubTasks().get(i).getDoneSubTaskTime();
        }
        return sum;
    }


    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
