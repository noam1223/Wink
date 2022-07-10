package com.noam.wink.helper.interfaces;

import com.noam.wink.model.Task;

public interface PopUpTaskListener {
    void addNewTask(String taskName, int taskTime);
    void editTask(Task task, int position);
    void deleteTask(int position);
}
