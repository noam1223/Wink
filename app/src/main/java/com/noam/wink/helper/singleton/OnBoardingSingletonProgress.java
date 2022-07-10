package com.noam.wink.helper.singleton;

import com.noam.wink.model.Task;
import com.noam.wink.model.User;

public class OnBoardingSingletonProgress {

    private static final OnBoardingSingletonProgress instance = new OnBoardingSingletonProgress();
    private int positionScreen = -1;
    private boolean onProgress = false;

    public static OnBoardingSingletonProgress getInstance() {
        return instance;
    }

    public int getPositionScreen() {
        return positionScreen;
    }

    public void setPositionScreen(int positionScreen) {
        this.positionScreen = positionScreen;
    }


    public boolean isOnProgress() {
        return onProgress;
    }

    public void setOnProgress(boolean onProgress) {
        this.onProgress = onProgress;
    }
}
