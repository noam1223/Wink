package com.noam.wink.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.noam.wink.R;
import com.noam.wink.adapter.SubTasksAdapter;
import com.noam.wink.helper.firebase.FirebaseHelper;
import com.noam.wink.helper.singleton.OnBoardingSingletonProgress;
import com.noam.wink.helper.uihelper.MovableFloatingActionButton;
import com.noam.wink.helper.uihelper.PopUps;
import com.noam.wink.helper.interfaces.PopUpTaskListener;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.SubTask;
import com.noam.wink.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubTasksActivity extends AppCompatActivity implements PopUpTaskListener {

    LinearLayout onBoarding1LinearLayout, onBoarding2LinearLayout, onBoarding3LinearLayout, onBoarding4LinearLayout, onBoardingSkipLinearLayout, backImgView, onBoardingBackLinearLayout;
    ImageView arrow1ImgView, arrow2ImgView, arrow3ImgView, arrow4ImgView;
    //    TextView onBoarding1TextView, onBoarding2TextView, onBoarding3TextView, onBoarding4TextView;
    Button onBoardingBtn1, onBoardingBtn2, onBoardingBtn3, onBoardingBtn4;


    ConstraintLayout noSubTasksConstrainLayout, parentConstrainLayout;
    LinearLayout timeAreaLinearLayout;
    RecyclerView subTasksRecyclerView;
    SubTasksAdapter subTaskAdapter;
    MovableFloatingActionButton taskActionBtn;
    ImageView reorderImgView;
    TextView taskNameTextView, timeLeftTextView, timeLeftToUseTextView;
    List<Task> taskList;
    Task task;
    ItemTouchHelper itemTouchHelper;
//    int position;

    @Override
    public void onBackPressed() {
        if (OnBoardingSingletonProgress.getInstance().isOnProgress()) {
            onBoardingContinue(-1);
        }else super.onBackPressed();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_tasks);

        timeLeftTextView = findViewById(R.id.task_time_left_text_view_sub_tasks_activity);
        taskActionBtn = findViewById(R.id.add_task_float_btn_sub_tasks_activity);
        timeLeftToUseTextView = findViewById(R.id.min_to_use_left_text_view_sub_tasks_activity);
        subTasksRecyclerView = findViewById(R.id.tasks_recycler_view_sub_tasks_activity);
        noSubTasksConstrainLayout = findViewById(R.id.constrain_layout_no_subs_tasks_bg_sub_tasks_activity);
        reorderImgView = findViewById(R.id.reorder_img_view_sub_task_activity);
        backImgView = findViewById(R.id.back_linear_layout_sub_task_activity);


        if (OnBoardingSingletonProgress.getInstance().isOnProgress()) {

            parentConstrainLayout = findViewById(R.id.parent_constrain_layout_sub_tasks_activity);
            timeAreaLinearLayout = findViewById(R.id.linear_layout_time_area_sub_tasks_activity);
            onBoardingSkipLinearLayout = findViewById(R.id.linear_layout_skip_area_onboarding_sub_tasks_activity);
            onBoardingBackLinearLayout = findViewById(R.id.back_onboardin_linear_layout_sub_task_activity);

            onBoarding1LinearLayout = findViewById(R.id.linear_layout_onboarding1_bg_sub_tasks_activity);
            onBoarding2LinearLayout = findViewById(R.id.linear_layout_onboarding2_bg_sub_tasks_activity);
            onBoarding3LinearLayout = findViewById(R.id.linear_layout_onboarding3_bg_sub_tasks_activity);
            onBoarding4LinearLayout = findViewById(R.id.linear_layout_onboarding4_bg_sub_tasks_activity);

            arrow1ImgView = findViewById(R.id.arrow1_img_view_sub_tasks_activity);
            arrow2ImgView = findViewById(R.id.arrow2_img_view_sub_tasks_activity);
            arrow3ImgView = findViewById(R.id.arrow3_img_view_sub_tasks_activity);
            arrow4ImgView = findViewById(R.id.arrow4_img_view_sub_tasks_activity);
//            onBoarding1TextView = findViewById(R.id.add_task_onboarding_txt_view_sub_tasks_activity);
//            onBoarding2TextView = findViewById(R.id.add_task_onboarding2_txt_view_sub_tasks_activity);
//            onBoarding3TextView = findViewById(R.id.add_task_onboarding3_txt_view_sub_tasks_activity);
//            onBoarding4TextView = findViewById(R.id.add_task_onboarding4_txt_view_sub_tasks_activity);
            onBoardingBtn1 = findViewById(R.id.onboarding_btn1_sub_tasks_activity);
            onBoardingBtn2 = findViewById(R.id.onboarding_btn2_sub_tasks_activity);
            onBoardingBtn3 = findViewById(R.id.onboarding_btn3_sub_tasks_activity);
            onBoardingBtn4 = findViewById(R.id.onboarding_btn4_sub_tasks_activity);

            timeAreaLinearLayout.setAlpha(.5f);
            parentConstrainLayout.setBackgroundColor(Color.parseColor("#3F3F3F"));

            arrow1ImgView.setVisibility(View.VISIBLE);
            onBoarding1LinearLayout.setVisibility(View.VISIBLE);
            onBoarding1LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));
            arrow1ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));

            onBoardingSkipLinearLayout.setVisibility(View.VISIBLE);
            onBoardingBackLinearLayout.setVisibility(View.VISIBLE);
            noSubTasksConstrainLayout.setVisibility(View.VISIBLE);
            backImgView.setVisibility(View.INVISIBLE);
            noSubTasksConstrainLayout.setAlpha(.3f);
            taskActionBtn.setEnabled(true);
            taskActionBtn.setClickable(true);
            taskActionBtn.setOnTouchListener(null);

            taskActionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBoardingContinue(1);
                }
            });

            onBoardingSkipLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SubTasksActivity.this, MainActivity.class);
                    OnBoardingSingletonProgress.getInstance().setPositionScreen(-1);
                    OnBoardingSingletonProgress.getInstance().setOnProgress(false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }
            });

            onBoardingBackLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBoardingContinue(-1);
                }
            });

            onBoardingBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBoardingContinue(1);
                }
            });
            onBoardingBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBoardingContinue(1);
                }
            });
            onBoardingBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBoardingContinue(1);
                }
            });
            onBoardingBtn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBoardingContinue(1);
                }
            });


        } else {

            taskNameTextView = findViewById(R.id.my_tasks_text_view_sub_tasks_activity);


//        Intent intent = getIntent();
//        position = intent.getIntExtra("task_position", -1);
            taskList = new ArrayList<>();
            taskList.addAll(SingletonUser.getInstance().getUser().getTaskProgressList());
            task = SingletonUser.getInstance().getTaskByPosition();

//        if (position > -1) {

            initializeSubTasksRecyclerView();
            initializeActivity();

//        }


            taskActionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getTimeSubTasks() == 0) {
                        Intent intent = new Intent(SubTasksActivity.this, PreviewTaskActivity.class);
//                    intent.putExtra("task", task);
                        startActivity(intent);
                        return;
                    }

                    PopUps.openPopUpNewTask(SubTasksActivity.this, SubTasksActivity.this, "תת-משימה חדשה", "שם תת-המשימה", "כמה זמן דרוש לי לביצוע תת-המשימה?", "לדוגמא: קריאת הוראות", "00:15 דקות", false);
                }
            });


            noSubTasksConstrainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopUps.openPopUpNewTask(SubTasksActivity.this, SubTasksActivity.this, "תת-משימה חדשה", "שם תת-המשימה", "כמה זמן דרוש לי לביצוע תת-המשימה?", "לדוגמא: קריאת הוראות", "00:15 דקות", false);
                }
            });


            backImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            reorderImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (subTaskAdapter.isShake()) {
                        itemTouchHelper.attachToRecyclerView(null);
                    } else itemTouchHelper.attachToRecyclerView(subTasksRecyclerView);

                    subTaskAdapter.setShake(!subTaskAdapter.isShake());
                    subTaskAdapter.notifyDataSetChanged();

                }
            });

        }
    }


    private void onBoardingContinue(int incDec) {
        OnBoardingSingletonProgress.getInstance().setPositionScreen(OnBoardingSingletonProgress.getInstance().getPositionScreen() + incDec);
        int position = OnBoardingSingletonProgress.getInstance().getPositionScreen();

        if (position == 2) {
            finish();

        } else if (position == 3) {

            arrow1ImgView.setVisibility(View.VISIBLE);
            onBoarding1LinearLayout.setVisibility(View.VISIBLE);

            onBoarding1LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));
            arrow1ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));

            onBoarding2LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
            arrow2ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));

            onBoardingSkipLinearLayout.setVisibility(View.VISIBLE);
            onBoardingBackLinearLayout.setVisibility(View.VISIBLE);
            noSubTasksConstrainLayout.setVisibility(View.VISIBLE);
            timeLeftTextView.setVisibility(View.VISIBLE);
            arrow2ImgView.setVisibility(View.INVISIBLE);
            onBoarding2LinearLayout.setVisibility(View.INVISIBLE);
            timeAreaLinearLayout.setAlpha(.5f);

            taskActionBtn.setEnabled(true);
            taskActionBtn.setClickable(true);
            taskActionBtn.setAlpha(1f);


            taskActionBtn.setImageResource(R.drawable.ic_plus);
            timeLeftToUseTextView.setText("דקות לניצול");
            subTasksRecyclerView.setVisibility(View.INVISIBLE);


        } else if (position == 4) {


            if (incDec > 0) {
                onBoarding1LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
                arrow1ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
            }else {
                onBoarding3LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
                arrow3ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
            }


            arrow1ImgView.setVisibility(View.INVISIBLE);
            onBoarding1LinearLayout.setVisibility(View.INVISIBLE);


            arrow2ImgView.setVisibility(View.VISIBLE);
            onBoarding2LinearLayout.setVisibility(View.VISIBLE);
            onBoarding2LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));
            arrow2ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));



            arrow3ImgView.setVisibility(View.INVISIBLE);
            onBoarding3LinearLayout.setVisibility(View.INVISIBLE);
            subTasksRecyclerView.setVisibility(View.VISIBLE);

            timeAreaLinearLayout.setAlpha(1f);
            timeLeftTextView.setVisibility(View.GONE);
            timeLeftToUseTextView.setText("כל הכבוד, אפשר להתחיל!");
            taskActionBtn.setAlpha(.5f);
            taskActionBtn.setEnabled(false);
            taskActionBtn.setClickable(false);

            Task task = new Task("עבודה בהיסטוריה", 25);
            task.getSubTasks().add(new SubTask("קריאת הוראות", 3));
            task.getSubTasks().add(new SubTask("בחירת שותף", 4));
            task.getSubTasks().add(new SubTask("בחירת שאלות", 7));
            task.getSubTasks().add(new SubTask("מענה על השאלות", 11));


            SubTasksActivity.this.task = task;
            initializeSubTasksRecyclerView();
            subTaskAdapter.notifyDataSetChanged();
            subTasksRecyclerView.setAlpha(.5f);
            taskActionBtn.setImageResource(R.drawable.ic_v_icon);
            noSubTasksConstrainLayout.setVisibility(View.GONE);
            reorderImgView.setBackground(null);


        } else if (position == 5) {

            timeAreaLinearLayout.setAlpha(.5f);
            taskActionBtn.setAlpha(.5f);
            taskActionBtn.setEnabled(false);
            taskActionBtn.setClickable(false);

            if (incDec > 0) {
                onBoarding2LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
                arrow2ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
            }else {
                onBoarding4LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
                arrow4ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
            }

            arrow2ImgView.setVisibility(View.INVISIBLE);
            onBoarding2LinearLayout.setVisibility(View.INVISIBLE);

            onBoarding3LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));
            arrow3ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));

            arrow3ImgView.setVisibility(View.VISIBLE);
            arrow4ImgView.setVisibility(View.INVISIBLE);
            onBoarding3LinearLayout.setVisibility(View.VISIBLE);
            onBoarding4LinearLayout.setVisibility(View.INVISIBLE);

            reorderImgView.setBackground(ContextCompat.getDrawable(SubTasksActivity.this, R.drawable.reorder_background));


        } else if (position == 6) {

            onBoarding3LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));
            arrow3ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_out));

            arrow3ImgView.setVisibility(View.INVISIBLE);
            arrow4ImgView.setVisibility(View.VISIBLE);
            onBoarding3LinearLayout.setVisibility(View.INVISIBLE);
            onBoarding4LinearLayout.setVisibility(View.VISIBLE);

            onBoarding4LinearLayout.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));
            arrow4ImgView.setAnimation(AnimationUtils.loadAnimation(SubTasksActivity.this, R.anim.fade_in));

            reorderImgView.setBackground(null);
            taskActionBtn.setAlpha(1f);

            taskActionBtn.setEnabled(true);
            taskActionBtn.setClickable(true);

        } else if (position == 7) {

            Intent intent = new Intent(SubTasksActivity.this, PlayTaskActivity.class);
            startActivity(intent);

        }
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            Collections.swap(task.getSubTasks(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
            recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    public void initializeActivity() {

        taskNameTextView.setText(task.getTaskName());
        timeLeftTextView.setText(getTimeSubTasks() + "");


        if (task.getSubTasks().size() == 0) {
            noSubTasksConstrainLayout.setVisibility(View.VISIBLE);
            subTasksRecyclerView.setVisibility(View.INVISIBLE);
            reorderImgView.setVisibility(View.INVISIBLE);
        } else {
            noSubTasksConstrainLayout.setVisibility(View.GONE);
            reorderImgView.setVisibility(View.VISIBLE);
            subTasksRecyclerView.setVisibility(View.VISIBLE);
        }


        if (getTimeSubTasks() == 0) {
            taskActionBtn.setImageResource(R.drawable.ic_v_icon);
            timeLeftTextView.setVisibility(View.GONE);
            timeLeftToUseTextView.setText("כל הכבוד, אפשר להתחיל!");
        } else {
            timeLeftTextView.setVisibility(View.VISIBLE);
            timeLeftToUseTextView.setText("דקות לניצול");
            taskActionBtn.setImageResource(R.drawable.ic_plus);
        }

    }


    private int getTimeSubTasks() {
        int sum = 0;
        for (int i = 0; i < task.getSubTasks().size(); i++) {
            sum += task.getSubTasks().get(i).getSubTaskTime();
        }

        sum = task.getTaskTime() - sum;

        return sum;
    }


    private void initializeSubTasksRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        itemTouchHelper = new ItemTouchHelper(simpleCallback);

        subTasksRecyclerView.setLayoutManager(layoutManager);
        subTaskAdapter = new SubTasksAdapter(this, task, this);

        if (subTasksRecyclerView.getItemDecorationCount() > 0) {
            subTasksRecyclerView.removeItemDecoration(itemDecoration);
        }
        subTasksRecyclerView.addItemDecoration(itemDecoration);

        subTasksRecyclerView.setAdapter(subTaskAdapter);

    }

    RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.top = 24;
        }
    };


    @Override
    protected void onStop() {

        if (!OnBoardingSingletonProgress.getInstance().isOnProgress() && taskList != null) {
            SingletonUser.getInstance().getUser().setTaskProgressList(new ArrayList<>());
            SingletonUser.getInstance().getUser().getTaskProgressList().addAll(taskList);
        }

        super.onStop();
    }


    @Override
    public void addNewTask(String taskName, int taskTime) {

        if (taskTime > getTimeSubTasks()) {
            Toast.makeText(SubTasksActivity.this, "סך הזמנים של תתי המשימות גדול מזמן המשימה (" + task.getTaskTime() + " דקות)", Toast.LENGTH_SHORT).show();
            return;
        }


        SubTask subTask = new SubTask(taskName, taskTime);
        task.getSubTasks().add(subTask);
        taskList.set(SingletonUser.getInstance().getPositionProgress(), task);
//        DataBaseLocally.saveListLocally(SubTasksActivity.this, taskList);
        SingletonUser.getInstance().getUser().setTaskProgressList(new ArrayList<>());
        SingletonUser.getInstance().getUser().getTaskProgressList().addAll(taskList);
        initializeActivity();
        subTaskAdapter.notifyDataSetChanged();

    }


    @Override
    public void editTask(Task task, int position) {

        if (task.getTaskTime() > getTimeSubTasks() + SubTasksActivity.this.task.getSubTasks().get(position).getSubTaskTime()) {
            Toast.makeText(SubTasksActivity.this, "הזמן שהכנסת גדול מדי, אנא בדוק את תתי המשימות", Toast.LENGTH_LONG).show();
            return;
        }

        SubTask subTask = new SubTask(task.getTaskName(), task.getTaskTime());
        SubTasksActivity.this.task.getSubTasks().set(position, subTask);
        taskList.set(SingletonUser.getInstance().getPositionProgress(), SubTasksActivity.this.task);
        SingletonUser.getInstance().getUser().setTaskProgressList(new ArrayList<>());
        SingletonUser.getInstance().getUser().getTaskProgressList().addAll(taskList);
        initializeActivity();
        subTaskAdapter.notifyDataSetChanged();
    }


    @Override
    public void deleteTask(int position) {
        task.getSubTasks().remove(position);
//        DataBaseLocally.saveListLocally(SubTasksActivity.this, taskList);
        SingletonUser.getInstance().getUser().setTaskProgressList(new ArrayList<>());
        SingletonUser.getInstance().getUser().getTaskProgressList().addAll(taskList);
        initializeActivity();
        subTaskAdapter.notifyDataSetChanged();
    }
}