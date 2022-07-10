package com.noam.wink.helper.interfaces;

public interface DoneTaskListener {
    void onDoneTaskClicked(boolean feedback, String feedbackText, int rateFace);
}
