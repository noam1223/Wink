package com.noam.wink.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.noam.wink.R;
import com.noam.wink.adapter.PreviewSubTaskAdapter;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.Task;

public class PreviewTaskActivity extends AppCompatActivity {

    LinearLayout backImgView;
    ImageView groupPlayImgView;
    TextView taskNameTextView, taskTimeTextView;
    ListView subTaskListView;
    Button startBtn;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_task);

        backImgView = findViewById(R.id.back_img_view_preview_task_activity);
        groupPlayImgView = findViewById(R.id.group_play_img_view_preview_task_activity);
        taskNameTextView = findViewById(R.id.my_task_text_view_preview_task_activity);
        taskTimeTextView = findViewById(R.id.my_task_time_text_view_preview_task_activity);
        subTaskListView = findViewById(R.id.sub_tasks_list_view_preview_task_activity);
        startBtn = findViewById(R.id.start_play_task_btn_preview_task_activity);

        task = SingletonUser.getInstance().getTaskByPosition();

        if (task != null) {

            taskNameTextView.setText(task.getTaskName());
            taskTimeTextView.setText(task.getTaskTime() + " דקות");

            subTaskListView.setAdapter(new PreviewSubTaskAdapter(this, 1, task.getSubTasks()));


            backImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            groupPlayImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    moveToPlayActivity();

                }
            });

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToPlayActivity();
                }
            });
        }

    }



    private void moveToPlayActivity() {
        Intent intent = new Intent(PreviewTaskActivity.this, PlayTaskActivity.class);
        startActivity(intent);
    }

}