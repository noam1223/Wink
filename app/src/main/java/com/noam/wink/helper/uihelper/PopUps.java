package com.noam.wink.helper.uihelper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.noam.wink.R;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.interfaces.DoneTaskListener;
import com.noam.wink.helper.interfaces.PlayTaskListener;
import com.noam.wink.helper.interfaces.PopUpTaskListener;
import com.noam.wink.helper.interfaces.UserNameListener;
import com.noam.wink.helper.interfaces.WaitListener;
import com.noam.wink.model.Task;
import com.noam.wink.model.Time;


public class PopUps {


    public static void openPopUpNewTask(Context context, PopUpTaskListener popUpTaskListener, String headLine, String subHeadLine, String subHeadLineTime, String nameString, String timeString, boolean isTask){

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.add_task_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_xy));

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.70);
        dialog.getWindow().setLayout(width, height);


        TextView headLineTextView = dialogView.findViewById(R.id.my_tasks_text_view_add_task_pop_up);
        headLineTextView.setText(headLine);
        TextView subHeadLineTextView = dialogView.findViewById(R.id.head_line_task_name_text_view_add_task_pop_up);
        TextView subHeadLineTimeTextView = dialogView.findViewById(R.id.time_line_task_name_text_view_add_task_pop_up);
        subHeadLineTextView.setText(subHeadLine);
        subHeadLineTimeTextView.setText(subHeadLineTime);

        LinearLayout exitImgView = dialogView.findViewById(R.id.x_img_view_add_task_pop_up);
        Button saveBtn = dialogView.findViewById(R.id.save_btn_add_task_pop_up);
        EditText nameEditText = dialogView.findViewById(R.id.task_name_edit_text_add_task_pop_up);
        EditText timeEditText = dialogView.findViewById(R.id.task_time_edit_text_add_task_pop_up);

        nameEditText.setHint(nameString);
        timeEditText.setHint(timeString);

        Time time = new Time();
        if (isTask){
            time.setMinute(5);
        }

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogNumberPicker = new Dialog(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.my_pick_number_dialog, null);
                dialogNumberPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogNumberPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNumberPicker.getWindow().setGravity(Gravity.BOTTOM);
                dialogNumberPicker.setContentView(dialogView);
                dialogView.getLayoutParams().width = (int) context.getResources().getDisplayMetrics().widthPixels;

                dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_to_top));

                LinearLayout exitLinearLayout = dialogView.findViewById(R.id.x_img_view_my_pick_number_dialog);
                Button saveBtn = dialogView.findViewById(R.id.save_btn_my_pick_number_dialog_pop_up);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker_my_pick_number_dialog);
                timePicker.setCurrentMinute(time.getMinute());
                timePicker.setCurrentHour(time.getHour());
                timePicker.setIs24HourView(true);

                exitLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_to_bottom));
                        dialogNumberPicker.dismiss();
                    }
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        time.setHour(timePicker.getCurrentHour());
                        time.setMinute(timePicker.getCurrentMinute());

                        String s = String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute());

                        timeEditText.setText(s);

                        dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_to_bottom));
                        dialogNumberPicker.dismiss();
                    }
                });

                dialogNumberPicker.show();

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String taskName = nameEditText.getText().toString();

                if (taskName.length() > 3) {
                    if (time.getTotalMinutes() > 0) {
                        popUpTaskListener.addNewTask(taskName, time.getTotalMinutes());
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.dismiss();
                    }else {
                        timeEditText.getBackground().mutate().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                        ShortCutsClass.toastMsg(context, "זמן המשימה לא יכול להיות 0 או פחות");
                    }
                } else {
                    nameEditText.getBackground().mutate().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    ShortCutsClass.toastMsg(context, "שם המשימה קצר מדי וצריך להכיל לפחות 4 תווים");
                }
            }
        });

        exitImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.dismiss();
            }
        });


        dialog.show();

    }




    public static void openPopUpEditTask(Context context, PopUpTaskListener popUpTaskListener, Task task, int position, boolean isTask){

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.add_task_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_xy));

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.70);
        dialog.getWindow().setLayout(width, height);


        TextView taskNameTextView = dialogView.findViewById(R.id.my_tasks_text_view_add_task_pop_up);
        TextView headTextTimeTextView = dialogView.findViewById(R.id.time_line_task_name_text_view_add_task_pop_up);
        LinearLayout exitImgView = dialogView.findViewById(R.id.x_img_view_add_task_pop_up);
        Button saveBtn = dialogView.findViewById(R.id.save_btn_add_task_pop_up);
        EditText nameEditText = dialogView.findViewById(R.id.task_name_edit_text_add_task_pop_up);
        EditText timeEditText = dialogView.findViewById(R.id.task_time_edit_text_add_task_pop_up);



        taskNameTextView.setText(task.getTaskName());
        final Time time;


        if (isTask) {
            nameEditText.setText(task.getTaskName());
            time = ShortCutsClass.getTimeSeparate(task.getTaskTime());
            timeEditText.setText(ShortCutsClass.getTimeFormatted(time));
            headTextTimeTextView.setText("כמה זמן דרוש לי לביצוע המשימה?");
        } else {
            nameEditText.setText(task.getSubTasks().get(position).getSubTaskName());
            time = ShortCutsClass.getTimeSeparate(task.getSubTasks().get(position).getSubTaskTime());
            timeEditText.setText(ShortCutsClass.getTimeFormatted(time));
            headTextTimeTextView.setText("כמה זמן דרוש לי לביצוע תת-המשימה?");
        }


                timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialogNumberPicker = new Dialog(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.my_pick_number_dialog, null);
                dialogNumberPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogNumberPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNumberPicker.getWindow().setGravity(Gravity.BOTTOM);
                dialogNumberPicker.setContentView(dialogView);
                dialogView.getLayoutParams().width = (int) context.getResources().getDisplayMetrics().widthPixels;

                dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_to_top));


                LinearLayout exitLinearLayout = dialogView.findViewById(R.id.x_img_view_my_pick_number_dialog);
                Button saveBtn = dialogView.findViewById(R.id.save_btn_my_pick_number_dialog_pop_up);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker_my_pick_number_dialog);
                timePicker.setCurrentMinute(time.getMinute());
                timePicker.setCurrentHour(time.getHour());
                timePicker.setIs24HourView(true);

                exitLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_to_bottom));
                        dialogNumberPicker.dismiss();
                    }
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        time.setHour(timePicker.getCurrentHour());
                        time.setMinute(timePicker.getCurrentMinute());

                        String s = String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute());

                        timeEditText.setText(s);

                        dialogView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_to_bottom));
                        dialogNumberPicker.dismiss();
                    }
                });



                dialogNumberPicker.show();

            }
        });




        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String taskName = nameEditText.getText().toString();

                if (taskName.length() > 3) {
                    if (time.getTotalMinutes() > 0) {

                        Task newTask = new Task(taskName, time.getTotalMinutes());
                        popUpTaskListener.editTask(newTask, position);
                        dialog.dismiss();
                    }else {
                        timeEditText.getBackground().mutate().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                        ShortCutsClass.toastMsg(context, "זמן המשימה לא יכול להיות 0 או פחות");
                    }
                } else {
                    nameEditText.getBackground().mutate().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    ShortCutsClass.toastMsg(context, "שם המשימה הינו קצר מדי");
                }

            }
        });

        exitImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }



    public static void openDeletePopUp(Context context, PopUpTaskListener popUpTaskListener, int position, String text){

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_task_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.40);
        dialog.getWindow().setLayout(width, height);

        TextView lineTextView = dialogView.findViewById(R.id.head_line_task_name_text_view_delete_task_pop_up);
        lineTextView.setText(text);

        LinearLayout exitBtn = dialogView.findViewById(R.id.x_img_view_delete_task_pop_up);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button yesBtn = dialogView.findViewById(R.id.yes_delete_task_pop_up);
        Button noBtn = dialogView.findViewById(R.id.no_delete_task_pop_up);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpTaskListener.deleteTask(position);
                dialog.dismiss();
            }
        });


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    public static void openFeedbackPopUp(Context context, DoneTaskListener doneTaskListener){

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.feedback_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialogView.getLayoutParams().width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);


        LinearLayout exitBtn =  dialogView.findViewById(R.id.x_img_view_feedback_pop_up);
        EditText feedbackEditText = dialogView.findViewById(R.id.feedback_edit_text_pop_up_feedback);
        Button doneBtn = dialogView.findViewById(R.id.save_btn_pop_up_feedback);
        ImageView grinningImgView, smilingImgView, naturalImgView, rollingEyesImgView, frowningImgView;
        grinningImgView = dialogView.findViewById(R.id.grinning_img_view_pop_up_feedback);
        smilingImgView = dialogView.findViewById(R.id.smiling_img_view_pop_up_feedback);
        naturalImgView = dialogView.findViewById(R.id.natural_img_view_pop_up_feedback);
        rollingEyesImgView = dialogView.findViewById(R.id.rolling_eyes_img_view_pop_up_feedback);
        frowningImgView = dialogView.findViewById(R.id.frowning_img_view_pop_up_feedback);

        ImageView[] imageViews = {grinningImgView, smilingImgView, naturalImgView, rollingEyesImgView, frowningImgView};
        final int[] rateFace = {0};

        grinningImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIcon(imageViews, view.getId());
                rateFace[0] = 1;
            }
        });
        smilingImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIcon(imageViews, view.getId());
                rateFace[0] = 2;
            }
        });
        naturalImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIcon(imageViews, view.getId());
                rateFace[0] = 3;
            }
        });
        rollingEyesImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIcon(imageViews, view.getId());
                rateFace[0] = 4;
            }
        });
        frowningImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIcon(imageViews, view.getId());
                rateFace[0] = 5;
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneTaskListener.onDoneTaskClicked(false, "", rateFace[0]);
                dialog.dismiss();
            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (feedbackEditText.getText().toString().length() < 10){
                    ShortCutsClass.toastMsg(context, "אני בטוח שיש לך משהו ארוך יותר לכתוב ;)");
                    return;
                }

                if (rateFace[0] == 0){
                    ShortCutsClass.toastMsg(context, "יש לבחור אייקון הממחיש את חוויתך");
                    return;
                }

                doneTaskListener.onDoneTaskClicked(true, feedbackEditText.getText().toString(), rateFace[0]);
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    public static void exitPlayTaskPopUp(Context context, PlayTaskListener playTaskListener, boolean exitBack){

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.exit_play_task_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        
        dialogView.getLayoutParams().width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);

        LinearLayout exitBtn = dialogView.findViewById(R.id.x_img_view_exit_play_task_pop_up);
        Button yesBtn = dialogView.findViewById(R.id.yes_exit_play_task_pop_up);
        Button noBtn = dialogView.findViewById(R.id.no_exit_play_task_pop_up);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTaskListener.exitPlayTaskYesBtn(exitBack);
                dialog.dismiss();
            }
        });

        dialog.show();

    }




    public static void userNamePopUp(Context context, UserNameListener userNameListener){

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.name_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialogView.getLayoutParams().width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);

        LinearLayout exitBtn = dialogView.findViewById(R.id.x_img_view_exit_name_pop_up);
        Button saveBtn = dialogView.findViewById(R.id.save_btn_name_pop_up);
        EditText nameEditText = dialogView.findViewById(R.id.edit_text_name_pop_up);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameListener.namePopUpSaveClicked("");
                dialog.dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameEditText.getText().toString();

                if (name.length() == 0 || name.equals(" ")){
                    ShortCutsClass.toastMsg(context, "אנא הכנס ערך הגיוני");
                    return;
                }

                userNameListener.namePopUpSaveClicked(name);
                dialog.dismiss();

            }
        });

        dialog.show();

    }





    private static void selectedIcon(ImageView[] imageViews, int id) {

        for (ImageView imageView : imageViews) {

            if (id == imageView.getId()) {
                imageView.setAlpha(0.5f);
            } else imageView.setAlpha(1f);

        }

    }


    public static void openWaitPopUp(Context context, WaitListener waitListener) {

        final Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.please_wait_pop_up, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        TextView waitTextView = dialogView.findViewById(R.id.dots_wait_text_view_pop_up);
        waitListener.OnWaitListener(waitTextView, dialog);

        dialog.show();

    }






}
