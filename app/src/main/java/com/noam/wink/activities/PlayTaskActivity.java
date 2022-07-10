package com.noam.wink.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noam.wink.R;
import com.noam.wink.helper.singleton.OnBoardingSingletonProgress;
import com.noam.wink.helper.uihelper.PopUps;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.interfaces.PlayTaskListener;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.Task;

import java.util.Timer;
import java.util.TimerTask;

public class PlayTaskActivity extends AppCompatActivity implements PlayTaskListener {

    ConstraintLayout parentConstrainLayout;
    LinearLayout onBoardingLinearLayout, onBoardingBackLinearLayout, exitImgView;
    RelativeLayout progressAreaRelativeLayout, controllerRelativeLayout;
    Button onBoardingBtn;
    ImageView arrowImgView, doneImgView, pauseImgView, forwardImgView;
    TextView taskNameTextView, currentSubTaskTextView, subTaskTimeTextView, nextSubTaskTextView, headLineNextSubTextView;
    ProgressBar timeDonutProgress;

    CountDownTimer countDownTimer;

    private Task task;
    int subPosition = 0;
    int oneMin = 60000;
    long timeForStopper = 0;
    boolean isRun = false;

    Timer timer;
    TimerTask timerTask;
    Double overTime = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_task);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        parentConstrainLayout = findViewById(R.id.parent_constrain_layout_play_task_activity);
        progressAreaRelativeLayout = findViewById(R.id.relative_layout_progress_area_play_task_activity);
        controllerRelativeLayout = findViewById(R.id.relative_layout_controller_play_task_activity);

        timeDonutProgress = findViewById(R.id.donut_progress_bar_play_task_activity);

        exitImgView = findViewById(R.id.x_img_view_play_task_activity);

        taskNameTextView = findViewById(R.id.task_name_text_view_play_task_activity);
        currentSubTaskTextView = findViewById(R.id.current_sub_task_play_task_activity);
        subTaskTimeTextView = findViewById(R.id.time_of_sub_task_text_view_play_task_activity);
        doneImgView = findViewById(R.id.done_img_view_play_task_activity);
        pauseImgView = findViewById(R.id.pause_img_view_play_task_activity);
        forwardImgView = findViewById(R.id.forward_img_view_play_task_activity);
        nextSubTaskTextView = findViewById(R.id.next_sub_task_text_view_play_task_activity);
        headLineNextSubTextView = findViewById(R.id.next_sub_task_head_line_play_task_activity);


        if (OnBoardingSingletonProgress.getInstance().isOnProgress()) {

            onBoardingLinearLayout = findViewById(R.id.linear_layout_onboarding1_bg_play_task_activity);
            onBoardingBackLinearLayout = findViewById(R.id.onboarding_back_linear_layout_play_task_activity);
            onBoardingBtn = findViewById(R.id.onboarding_btn1_play_task_activity);
            arrowImgView = findViewById(R.id.arrow1_img_view_play_task_activity);

            progressAreaRelativeLayout.setAlpha(.3f);
            parentConstrainLayout.setBackgroundColor(Color.parseColor("#3F3F3F"));

            onBoardingBackLinearLayout.setVisibility(View.VISIBLE);
            onBoardingLinearLayout.setVisibility(View.VISIBLE);
            arrowImgView.setVisibility(View.VISIBLE);

            onBoardingLinearLayout.setAnimation(AnimationUtils.loadAnimation(PlayTaskActivity.this, R.anim.fade_in));
            arrowImgView.setAnimation(AnimationUtils.loadAnimation(PlayTaskActivity.this, R.anim.fade_in));

            onBoardingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToMainFromBoarding();
                }
            });

            doneImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToMainFromBoarding();
                }
            });

            onBoardingBackLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnBoardingSingletonProgress.getInstance().setPositionScreen(OnBoardingSingletonProgress.getInstance().getPositionScreen() - 1);
                    finish();
                }
            });


        } else {

            task = SingletonUser.getInstance().getTaskByPosition();

            if (task != null) {

                taskNameTextView.setText(task.getTaskName());
                updateUI();
                playProgressTime(task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin);

            }

            exitImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isRun) {
                        handleStopper();
                    }

                    PopUps.exitPlayTaskPopUp(PlayTaskActivity.this, PlayTaskActivity.this, true);

                }
            });


            pauseImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleStopper();
                }
            });


            doneImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    task.getSubTasks().get(subPosition).setDoneSubTaskTime((task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin) - timeForStopper + (overTime.longValue() * 1000));
                    task.getSubTasks().get(subPosition).setDone(true);

                    overTime = 0.0;

                    if (!moveSubTaskPosition()) {
                        Intent intent = new Intent(PlayTaskActivity.this, FinishTaskActivity.class);
                        intent.putExtra("task", task);
                        startActivity(intent);
                        finish();
                    } else updateUI();


                }
            });


            forwardImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    task.getSubTasks().get(subPosition).setDoneSubTaskTime((task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin) - timeForStopper + (overTime.longValue() * 1000));

                    if (!moveSubTaskPosition()) {
                        Toast.makeText(PlayTaskActivity.this, "זוהי התת-משימה האחרונה שנשארה לך לבצע", Toast.LENGTH_SHORT).show();
                    } else {
                        updateUI();
                        overTime = 0.0;
                    }
                }
            });
        }
    }


    private void moveToMainFromBoarding() {
        Intent intent = new Intent(PlayTaskActivity.this, MainActivity.class);
        OnBoardingSingletonProgress.getInstance().setPositionScreen(-1);
        OnBoardingSingletonProgress.getInstance().setOnProgress(false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


    private boolean moveSubTaskPosition() {

        int currentPosition = subPosition;
        subPosition++;

        if (subPosition >= task.getSubTasks().size()) {
            subPosition = 0;
        }


        int position = getPosition(currentPosition);


        if (position > -1) {
            resetStoppers();
            playProgressTime((task.getSubTasks().get(position).getSubTaskTime() * oneMin) - (int) task.getSubTasks().get(position).getDoneSubTaskTime());
            subPosition = position;
            return true;
        }

        subPosition = currentPosition;

        return false;
    }


    private int getPosition(int currentPosition) {

        for (int i = subPosition; i < task.getSubTasks().size(); i++) {
            if (!task.getSubTasks().get(i).isDone() && currentPosition != i) {
                return i;
            }
        }

        for (int i = 0; i < currentPosition; i++) {
            if (!task.getSubTasks().get(i).isDone()) {
                return i;
            }
        }

        return -1;
    }


    private void resetStoppers() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        isRun = false;

        if (timer != null)
            timer.cancel();

        if (timerTask != null)
            timerTask.cancel();

    }


    private void updateUI() {

        currentSubTaskTextView.setText(task.getSubTasks().get(subPosition).getSubTaskName());
        headLineNextSubTextView.setVisibility(View.VISIBLE);

        int position = getPosition(subPosition);

        if (position > -1) {
            nextSubTaskTextView.setText(task.getSubTasks().get(position).getSubTaskName());
            subTaskTimeTextView.setText(ShortCutsClass.getHourString(task.getSubTasks().get(position).getSubTaskTime() * oneMin));
        } else {
            headLineNextSubTextView.setVisibility(View.INVISIBLE);
            nextSubTaskTextView.setText("תת-משימה אחרונה");
        }

    }


    private void playProgressTime(int time) {

        time = Math.abs(time);

        timeForStopper = time;
        timeDonutProgress.setMax(task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin);
        timeDonutProgress.setProgress(time);

        subTaskTimeTextView.setText(ShortCutsClass.getHourString(time));
        handleStopper();

    }


    public void handleStopper() {

        if (isRun) {

            resetStoppers();

            isRun = false;
            pauseImgView.setImageResource(R.drawable.ic_play_circle_filled);
            doneImgView.setAlpha(.5f);
            doneImgView.setEnabled(false);
            forwardImgView.setAlpha(.5f);
            forwardImgView.setEnabled(false);


        } else {

            if (overTime + task.getSubTasks().get(subPosition).getDoneSubTaskTime() >= task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin) {
                startCountForward();
            } else startStopper(timeForStopper);

            pauseImgView.setImageResource(R.drawable.ic_pause);
            doneImgView.setAlpha(1f);
            doneImgView.setEnabled(true);
            forwardImgView.setAlpha(1f);
            forwardImgView.setEnabled(true);
            isRun = true;
        }
    }


    private void startStopper(long time) {

        countDownTimer = new CountDownTimer(time, 25) {

            @Override
            public void onTick(long millisUntilFinished) {

                subTaskTimeTextView.setText(ShortCutsClass.getHourString(millisUntilFinished));

                timeForStopper = millisUntilFinished;
                timeDonutProgress.setProgress((int) millisUntilFinished);

            }

            @Override
            public void onFinish() {

                timeForStopper = 0;
                startCountForward();
            }

        }.start();

    }


    private void startCountForward() {
        timeDonutProgress.setProgress(0);
        timeDonutProgress.setMax(0);
        timeForStopper = 0;

        if (task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin <= task.getSubTasks().get(subPosition).getDoneSubTaskTime())
            overTime = ((double) Math.abs((double) (task.getSubTasks().get(subPosition).getSubTaskTime() * oneMin - task.getSubTasks().get(subPosition).getDoneSubTaskTime()))) / 1000;

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PlayTaskActivity.this.overTime++;
                        subTaskTimeTextView.setText(ShortCutsClass.getHourString((overTime.longValue() * 1000)));
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }


    @Override
    public void onStop() {
        resetStoppers();
        super.onStop();
    }


    @Override
    public void exitPlayTaskYesBtn(boolean exitBack) {

        if (exitBack) {
            Intent intent = new Intent(PlayTaskActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        finish();
    }
}