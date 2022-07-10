package com.noam.wink.helper.singleton;

import com.noam.wink.model.Task;
import com.noam.wink.model.User;

import java.io.Serializable;

public class SingletonUser implements Serializable {

    private static final SingletonUser instance = new SingletonUser();
    private User user = new User();
    private int positionProgress = -1;

    public static SingletonUser getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPositionProgress() {
        return positionProgress;
    }

    public void setPositionProgress(int positionProgress) {
        this.positionProgress = positionProgress;
    }

    public Task getTaskByPosition(){
        return getUser().getTaskProgressList().get(positionProgress);
    }
}
