package com.noam.wink.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.wink.R;
import com.noam.wink.activities.FinishTaskActivity;
import com.noam.wink.activities.MainActivity;
import com.noam.wink.activities.PreviewTaskActivity;
import com.noam.wink.activities.SubTasksActivity;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.interfaces.PopUpTaskListener;
import com.noam.wink.helper.singleton.OnBoardingSingletonProgress;
import com.noam.wink.helper.uihelper.PopUps;
import com.noam.wink.helper.singleton.SingletonUser;
import com.noam.wink.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder> {

    Context context;
    List<Task> tasks;
    PopUpTaskListener popUpTaskListener;


    public TaskAdapter(Context context, List<Task> tasks, PopUpTaskListener popUpTaskListener) {
        this.context = context;
        this.tasks = tasks;
        this.popUpTaskListener = popUpTaskListener;
    }


    @NonNull
    @Override
    public TaskAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 20;
        lp.rightMargin = 20;
        layoutView.setLayoutParams(lp);
        return new TaskAdapterViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull final TaskAdapterViewHolder holder, int position) {

        //TODO: CHECK THIS OUT
        holder.setIsRecyclable(false);

        holder.taskNameTextView.setText(tasks.get(position).getTaskName());
        holder.taskTimeTextView.setText(ShortCutsClass.getTimeFormatted(ShortCutsClass.getTimeSeparate(tasks.get(position).getTaskTime())));


        if (tasks.get(position).getSubTasks() != null)
            holder.subTasksNumTextView.setText(tasks.get(position).getSubTasks().size() + " תתי משימות");

        if (tasks.get(position).isDone()) {
            holder.constraintLayoutParent.setAlpha(.5f);
            holder.playImgView.setVisibility(View.INVISIBLE);

            holder.constraintLayoutParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FinishTaskActivity.class);
                    SingletonUser.getInstance().setPositionProgress(position);
                    context.startActivity(intent);
                }
            });
        } else {


            if (OnBoardingSingletonProgress.getInstance().isOnProgress()) {
                if (position == 0) {
//                    holder.constraintLayoutParent.setBackgroundTintList(Color.parseColor("#FF9EE6FE"));
                    holder.constraintLayoutParent.setBackground(ContextCompat.getDrawable(context, R.drawable.task_item_onboarding));
                    holder.constraintLayoutParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((MainActivity) context).continueOnBoarding(1);
                        }
                    });

                    holder.deleteImgView.setClickable(false);
                    holder.deleteImgView.setEnabled(false);
                    holder.playImgView.setClickable(false);
                    holder.playImgView.setEnabled(false);
                    holder.editImgView.setClickable(false);
                    holder.editImgView.setEnabled(false);
                }
            } else {


                holder.constraintLayoutParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SingletonUser.getInstance().setPositionProgress(position);
                        Intent intent = new Intent(context, SubTasksActivity.class);
                        context.startActivity(intent);
                    }
                });


                holder.deleteImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopUps.openDeletePopUp(context, popUpTaskListener, position, "האם ברצונך למחוק את המשימה?");
                    }
                });


                if (tasks.get(position).getTimeSubTasks() == tasks.get(position).getTaskTime()) {
                    holder.playImgView.setVisibility(View.VISIBLE);
                } else holder.playImgView.setVisibility(View.INVISIBLE);


                holder.playImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                Task task = tasks.get(position);
                        SingletonUser.getInstance().setPositionProgress(position);

                        Intent intent = new Intent(context, PreviewTaskActivity.class);
//                intent.putExtra("task", task);
                        context.startActivity(intent);

                    }
                });


                holder.editImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopUps.openPopUpEditTask(context, popUpTaskListener, tasks.get(position), position, true);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class TaskAdapterViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayoutParent;
        TextView taskNameTextView, taskTimeTextView, subTasksNumTextView;
        ImageView playImgView, deleteImgView, editImgView;

        public TaskAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayoutParent = itemView.findViewById(R.id.constrain_layout_parent_task_item);
            taskNameTextView = itemView.findViewById(R.id.task_name_text_view_task_item);
            taskTimeTextView = itemView.findViewById(R.id.task_time_text_view_task_item);
            subTasksNumTextView = itemView.findViewById(R.id.sub_tasks_num_text_view_task_item);
            playImgView = itemView.findViewById(R.id.play_img_view_task_item);
            deleteImgView = itemView.findViewById(R.id.garbage_img_view_task_item);
            editImgView = itemView.findViewById(R.id.three_dots_img_view_task_item);

        }

    }
}
