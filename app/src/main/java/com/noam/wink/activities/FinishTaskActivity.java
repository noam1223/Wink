package com.noam.wink.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.noam.wink.R;
import com.noam.wink.adapter.FinishSubTaskAdapter;
import com.noam.wink.helper.DataBaseLocally;
import com.noam.wink.helper.firebase.FirebaseHelper;
import com.noam.wink.helper.firebase.FirebaseListener;
import com.noam.wink.helper.uihelper.PopUps;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.interfaces.DoneTaskListener;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.Task;

public class FinishTaskActivity extends AppCompatActivity implements DoneTaskListener {

    LinearLayout exitImgView;
    ImageView starImgView;
    TextView taskNameTextView, taskTimeTextView;
    Button doneBtn;
    ListView finishSubTasksListView;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_task);

        exitImgView = findViewById(R.id.x_img_view_finish_task_activity);
        starImgView = findViewById(R.id.star_img_view_finish_task_activity);
        doneBtn = findViewById(R.id.done_btn_finish_task_activity);
        taskNameTextView = findViewById(R.id.task_name_text_view_finish_task_activity);
        taskTimeTextView = findViewById(R.id.task_time_text_view_finish_task_activity);
        finishSubTasksListView = findViewById(R.id.finish_list_view_finish_task_activity);

        task = SingletonUser.getInstance().getTaskByPosition();
        starImgView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

        if (task != null) {

            taskNameTextView.setText(task.getTaskName());
            taskTimeTextView.setText(ShortCutsClass.getHourString(task.getDoneTimeSubTasks()));

            finishSubTasksListView.setAdapter(new FinishSubTaskAdapter(this, 1, task.getSubTasks()));
        }


        exitImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDoneStatus();
            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDoneStatus();
            }
        });

    }


    @Override
    protected void onDestroy() {

        if (SingletonUser.getInstance().getPositionProgress() > -1 && SingletonUser.getInstance().getTaskByPosition() != null && !SingletonUser.getInstance().getTaskByPosition().isDone()){
            task.setDone(true);
            SingletonUser.getInstance().getUser().getTaskProgressList().set(SingletonUser.getInstance().getPositionProgress(), task);
            SingletonUser.getInstance().setPositionProgress(-1);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        checkDoneStatus();
    }

    private void checkDoneStatus() {
        if (!task.isDone()) {
            PopUps.openFeedbackPopUp(FinishTaskActivity.this, FinishTaskActivity.this);
            task.setDone(true);
        } else finish();
    }

    @Override
    public void onDoneTaskClicked(boolean feedback, String feedbackText, int rateFace) {

        if (feedback) {
            task.setFeedback(feedbackText);
            task.setRate(rateFace);
        }

        Intent intent = new Intent(FinishTaskActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        SingletonUser.getInstance().getUser().getTaskProgressList().set(SingletonUser.getInstance().getPositionProgress(), task);
        DataBaseLocally.saveUserLocally(this, SingletonUser.getInstance().getUser());
        SingletonUser.getInstance().setPositionProgress(-1);

        finish();
    }


}