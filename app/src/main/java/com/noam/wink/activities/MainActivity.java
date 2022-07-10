package com.noam.wink.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.noam.wink.R;
import com.noam.wink.activities.onboarding.OnBoardingStart;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.firebase.FirebaseHelper;
import com.noam.wink.helper.firebase.FirebaseListener;
import com.noam.wink.helper.interfaces.PopUpTaskListener;
import com.noam.wink.helper.interfaces.UserNameListener;
import com.noam.wink.helper.interfaces.WaitListener;
import com.noam.wink.helper.singleton.OnBoardingSingletonProgress;
import com.noam.wink.helper.uihelper.MovableFloatingActionButton;
import com.noam.wink.helper.uihelper.PopUps;
import com.noam.wink.adapter.TaskAdapter;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.SubTask;
import com.noam.wink.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements PopUpTaskListener, UserNameListener, FirebaseListener, WaitListener {

    ConstraintLayout onBoardingPopUp2ConstrainLayout, parentConstrainLayout;
    LinearLayout onBoarding1LinearLayout, onBoarding2LinearLayout, onBoarding3LinearLayout, onBoardingBackLinearLayout, onBoardingSkipLinearLayout;
    ImageView arrow1ImgView, waveIconImgView, arrow2ImgView, arrow3ImgView, moveToOnBoarding;
    Button continueOnBoardingBtn1, continueOnBoardingBtn2, continueOnBoardingBtn3;
    View includeOnBoarding;

    ConstraintLayout noTasksConstrainLayout;
    RecyclerView taskRecyclerView;
    TextView myTasksHeadTextView, waveTextView;
    TaskAdapter taskAdapter;
    MovableFloatingActionButton taskActionBtn;

    List<Task> tasks = new ArrayList<>();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    boolean isWait = true;

    @Override
    public void onBackPressed() {
        if (OnBoardingSingletonProgress.getInstance().isOnProgress()) {
            continueOnBoarding(-1);
        }else finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentConstrainLayout = findViewById(R.id.parent_constrain_layout_main_activity);
        taskActionBtn = findViewById(R.id.add_task_float_btn_main_activity);
        noTasksConstrainLayout = findViewById(R.id.constrain_layout_no_tasks_bg_main_activity);
        waveTextView = findViewById(R.id.wave_text_view_main_activity);
        myTasksHeadTextView = findViewById(R.id.my_tasks_text_view_main_activity);
        taskRecyclerView = findViewById(R.id.tasks_recycler_view_main_activity);
        waveIconImgView = findViewById(R.id.wave_icon_img_view_main_activity);
        moveToOnBoarding = findViewById(R.id.move_to_onboarding_img_view_main_activity);


        initializeTaskRecyclerView();


        if (OnBoardingSingletonProgress.getInstance().isOnProgress()) {

            onBoardingBackLinearLayout = findViewById(R.id.onboarding_back_linear_layout_main_activity);
            onBoardingSkipLinearLayout = findViewById(R.id.linear_layout_skip_area_onboarding_main_activity);
            includeOnBoarding = findViewById(R.id.include_task_pop_up_main_activity);
            onBoardingPopUp2ConstrainLayout = findViewById(R.id.constrain_layout_on_boarding2_pop_up_main_activity);
            onBoarding1LinearLayout = findViewById(R.id.linear_layout_onboarding1_bg_main_activity);
            onBoarding2LinearLayout = findViewById(R.id.linear_layout_onboarding2_bg_main_activity);
            onBoarding3LinearLayout = findViewById(R.id.linear_layout_onboarding3_bg_main_activity);


            continueOnBoardingBtn1 = findViewById(R.id.onboarding_btn1_main_activity);
            continueOnBoardingBtn2 = findViewById(R.id.onboarding_btn2_main_activity);
            continueOnBoardingBtn3 = findViewById(R.id.onboarding_btn3_main_activity);
            arrow1ImgView = findViewById(R.id.arrow1_onboarding_img_view_main_activity);
            arrow2ImgView = findViewById(R.id.arrow2_onboarding_img_view_main_activity);
            arrow3ImgView = findViewById(R.id.arrow3_onboarding_img_view_main_activity);
//            arrow6ImgView = findViewById(R.id.arrow_6_img_view_main_activity);

            parentConstrainLayout.setBackgroundColor(Color.parseColor("#3F3F3F"));
            waveIconImgView.setAlpha(.5f);
            arrow1ImgView.setVisibility(View.VISIBLE);
            moveToOnBoarding.setVisibility(View.INVISIBLE);
            onBoardingSkipLinearLayout.setVisibility(View.VISIBLE);
            onBoarding1LinearLayout.setVisibility(View.VISIBLE);
            onBoarding1LinearLayout.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            arrow1ImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            noTasksConstrainLayout.setVisibility(View.VISIBLE);
            noTasksConstrainLayout.setAlpha(.3f);

            taskActionBtn.setEnabled(true);
            taskActionBtn.setClickable(true);
            taskActionBtn.setOnTouchListener(null);


            taskActionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(1);
                }
            });

            continueOnBoardingBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(1);
                }
            });

            continueOnBoardingBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(1);
                }
            });

            continueOnBoardingBtn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(1);
                }
            });

            onBoardingBackLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(-1);
                }
            });

            onBoardingSkipLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    OnBoardingSingletonProgress.getInstance().setPositionScreen(-1);
                    OnBoardingSingletonProgress.getInstance().setOnProgress(false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    finish();
                    startActivity(intent);
                }
            });

        } else {

            myTasksHeadTextView.setVisibility(View.INVISIBLE);
            FirebaseHelper.isUserSigned(this, auth.getUid());
            PopUps.openWaitPopUp(MainActivity.this, this);

        }
    }

    public void continueOnBoarding(int incDec) {
        OnBoardingSingletonProgress.getInstance().setPositionScreen(OnBoardingSingletonProgress.getInstance().getPositionScreen() + incDec);
        int position = OnBoardingSingletonProgress.getInstance().getPositionScreen();

        if (position <= 0) {
            OnBoardingSingletonProgress.getInstance().setPositionScreen(0);

            arrow1ImgView.setVisibility(View.VISIBLE);
            onBoarding1LinearLayout.setVisibility(View.VISIBLE);

            noTasksConstrainLayout.setVisibility(View.VISIBLE);
            noTasksConstrainLayout.setAlpha(.3f);

            onBoardingBackLinearLayout.setVisibility(View.INVISIBLE);
            onBoardingPopUp2ConstrainLayout.setVisibility(View.INVISIBLE);
            taskActionBtn.setAlpha(1f);
            taskActionBtn.setEnabled(true);
            taskActionBtn.setClickable(true);


            taskActionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(1);
                }
            });

            onBoarding1LinearLayout.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            arrow1ImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));

            onBoarding2LinearLayout.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));
            arrow2ImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));

        } else if (position == 1) {

            arrow1ImgView.setVisibility(View.INVISIBLE);
            onBoarding1LinearLayout.setVisibility(View.INVISIBLE);
            onBoardingBackLinearLayout.setVisibility(View.VISIBLE);
            onBoardingPopUp2ConstrainLayout.setVisibility(View.VISIBLE);

            onBoarding2LinearLayout.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            arrow2ImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));


            onBoarding3LinearLayout.setVisibility(View.INVISIBLE);
            arrow3ImgView.setVisibility(View.INVISIBLE);
            noTasksConstrainLayout.setVisibility(View.VISIBLE);
            taskRecyclerView.setVisibility(View.INVISIBLE);

            taskActionBtn.setAlpha(.5f);
            includeOnBoarding.getLayoutParams().width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

            Button saveBtn = findViewById(R.id.save_btn_add_task_pop_up);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continueOnBoarding(1);
                }
            });
            EditText taskEditTask = findViewById(R.id.task_name_edit_text_add_task_pop_up);
            taskEditTask.setText("עבודה בהיסטוריה");
            taskEditTask.setEnabled(false);
            EditText timeEditTask = findViewById(R.id.task_time_edit_text_add_task_pop_up);
            timeEditTask.setEnabled(false);
            timeEditTask.setText("25 (דקות)");
            includeOnBoarding.setClickable(false);
            includeOnBoarding.setEnabled(false);
            taskActionBtn.setEnabled(false);
            taskActionBtn.setClickable(false);


        } else if (position == 2) {

            onBoarding2LinearLayout.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));
            arrow2ImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));

            onBoardingPopUp2ConstrainLayout.setVisibility(View.INVISIBLE);
            onBoarding3LinearLayout.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            arrow3ImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));

            onBoarding3LinearLayout.setVisibility(View.VISIBLE);
            arrow3ImgView.setVisibility(View.VISIBLE);
            noTasksConstrainLayout.setVisibility(View.INVISIBLE);
            taskRecyclerView.setVisibility(View.VISIBLE);


            if (tasks.size() == 0) {

                Task task1 = new Task("עבודה בהיסטוריה", 25);
                task1.getSubTasks().add(new SubTask("קריאת הוראות", 3));
                task1.getSubTasks().add(new SubTask("בחירת שותף", 4));
                task1.getSubTasks().add(new SubTask("בחירת שאלות", 7));
                task1.getSubTasks().add(new SubTask("מענה על השאלות", 11));

                tasks.add(task1);

                initializeTaskRecyclerView();
                taskAdapter.notifyDataSetChanged();
                taskRecyclerView.setEnabled(false);
                taskRecyclerView.setClickable(false);

            }

        } else if (position == 3) {

            Intent intent = new Intent(MainActivity.this, SubTasksActivity.class);
            startActivity(intent);

        }
    }


    @Override
    protected void onResume() {

        if (OnBoardingSingletonProgress.getInstance().getPositionScreen() == -1 && !OnBoardingSingletonProgress.getInstance().isOnProgress()) {

            showMsg();
            taskAdapter.notifyDataSetChanged();

        }

        super.onResume();
    }


    private void sortTaskListByDone() {

        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                Collections.sort(tasks, new Comparator<Task>() {
                    @Override
                    public int compare(Task task, Task t1) {
                        if (task.isDone())
                            return 1;
                        else return -1;
                    }
                });
            }
        });
    }


    @Override
    protected void onStop() {

        if (!OnBoardingSingletonProgress.getInstance().isOnProgress()) {
            FirebaseHelper.writeUserDB();
        }

        super.onStop();
    }


    @Override
    protected void onDestroy() {

        if (!OnBoardingSingletonProgress.getInstance().isOnProgress()) {
            FirebaseHelper.writeUserDB();
        }

        super.onDestroy();
    }


    private void showMsg() {

        if (tasks.size() == 0) {
            noTasksConstrainLayout.setVisibility(View.VISIBLE);
            myTasksHeadTextView.setVisibility(View.INVISIBLE);
            taskRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            noTasksConstrainLayout.setVisibility(View.GONE);
            myTasksHeadTextView.setVisibility(View.VISIBLE);
            taskRecyclerView.setVisibility(View.VISIBLE);
        }


    }


    private void initializeTaskRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        taskRecyclerView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(this, tasks, MainActivity.this);

        if (taskRecyclerView.getItemDecorationCount() > 0) {
            taskRecyclerView.removeItemDecoration(itemDecoration);
        }
        taskRecyclerView.addItemDecoration(itemDecoration);

        taskRecyclerView.setAdapter(taskAdapter);

    }


    RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.top = 24;
        }
    };


    public void setText(final String s) {
        Thread thread = new Thread() {
            int i;

            @Override
            public void run() {
                try {
                    for (i = 0; i < s.length(); i++) {
                        Thread.sleep(150);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                waveTextView.setText("היי " + s.substring(0, i));
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waveTextView.setText("היי " + s);
                            waveIconImgView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.waving));
                        }
                    });


                } catch (InterruptedException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waveTextView.setText("היי " + s.substring(0, i));
                        }
                    });
                }
            }
        };

        thread.start();
    }


    @Override
    public void addNewTask(String taskName, int taskTime) {
        tasks.add(new Task(taskName, taskTime));
        SingletonUser.getInstance().getUser().setTaskProgressList(new ArrayList<>());
        SingletonUser.getInstance().getUser().getTaskProgressList().addAll(tasks);
        taskAdapter.notifyDataSetChanged();
        showMsg();
    }


    @Override
    public void editTask(Task task, int position) {
        tasks.set(position, task);
        SingletonUser.getInstance().getUser().setTaskProgressList(tasks);
        taskAdapter.notifyDataSetChanged();
    }


    @Override
    public void deleteTask(int position) {
        tasks.remove(position);
        SingletonUser.getInstance().getUser().setTaskProgressList(tasks);
//        sortTaskListByDone();
        taskAdapter.notifyDataSetChanged();
        showMsg();
    }


    @Override
    public void namePopUpSaveClicked(String name) {

        SingletonUser.getInstance().getUser().setUserName(name);
        setText(name);

    }


    @Override
    public void retrieveUserData(String error) {

        if (error != null && !error.equals("")) {
            ShortCutsClass.toastMsg(MainActivity.this, error);

            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(intent);
            finish();
            return;
        }

        tasks.clear();
        tasks.addAll(SingletonUser.getInstance().getUser().getTaskProgressList());
        taskAdapter.notifyDataSetChanged();

        showMsg();
        isWait = false;

        String name = "";

        if (SingletonUser.getInstance().getUser() != null && !SingletonUser.getInstance().getUser().getUserName().equals("")) {
            name = SingletonUser.getInstance().getUser().getUserName();
        }

        waveTextView.setText("היי ");
        setText(name);
        taskActionBtn.setClickable(true);
        taskActionBtn.setEnabled(true);

        if (SingletonUser.getInstance().getUser().getUserName().equals(""))
            PopUps.userNamePopUp(MainActivity.this, MainActivity.this);

        taskActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUps.openPopUpNewTask(MainActivity.this, MainActivity.this, "הוסף משימה", "שם המשימה", "כמה זמן דרוש לי לביצוע המשימה?", "שם המשימה", "00:15 דקות", true);
            }
        });

        noTasksConstrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUps.openPopUpNewTask(MainActivity.this, MainActivity.this, "הוסף משימה", "שם המשימה", "כמה זמן דרוש לי לביצוע המשימה?", "שם המשימה", "00:15 דקות", true);
            }
        });

        moveToOnBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OnBoardingStart.class);
                OnBoardingSingletonProgress.getInstance().setOnProgress(true);
                OnBoardingSingletonProgress.getInstance().setPositionScreen(0);
                startActivity(intent);
            }
        });

    }


    @Override
    public void OnWaitListener(TextView textView, Dialog dialog) {
        Thread thread = new Thread() {
            int i;

            @Override
            public void run() {

                String dots = "...";

                try {

                    while (isWait) {
                        textView.setText("");
                        for (i = 0; i < dots.length(); i++) {
                            Thread.sleep(350);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(dots.substring(0, i));
                                }
                            });
                        }
                    }
                    dialog.dismiss();


                } catch (InterruptedException e) {
                    textView.setText(dots);
                }
            }
        };

        thread.start();
    }
}