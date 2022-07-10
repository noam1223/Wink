package com.noam.wink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.wink.R;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.uihelper.PopUps;
import com.noam.wink.helper.interfaces.PopUpTaskListener;
import com.noam.wink.model.Task;

public class SubTasksAdapter extends RecyclerView.Adapter<SubTasksAdapter.SubTasksAdapterViewHolder> {

    Context context;
    PopUpTaskListener popUpTaskListener;
    Task task;

    boolean shake = false;

    public SubTasksAdapter(Context context, Task task, PopUpTaskListener popUpTaskListener) {
        this.context = context;
        this.popUpTaskListener = popUpTaskListener;
        this.task = task;
    }


    public void setShake(boolean shake) {
        this.shake = shake;
    }

    public boolean isShake() {
        return shake;
    }


    @NonNull
    @Override
    public SubTasksAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setBackground(ContextCompat.getDrawable(context, R.drawable.sub_task_item_background));
        lp.leftMargin = 20;
        lp.rightMargin = 20;
        layoutView.setLayoutParams(lp);
        return new SubTasksAdapterViewHolder(layoutView);
    }



    @Override
    public void onBindViewHolder(@NonNull final SubTasksAdapterViewHolder holder, int position) {

        holder.taskNameTextView.setText(task.getSubTasks().get(position).getSubTaskName());
        holder.taskTimeTextView.setText(ShortCutsClass.getTimeFormatted(ShortCutsClass.getTimeSeparate(task.getSubTasks().get(position).getSubTaskTime())));

        holder.playImgView.setVisibility(View.GONE);
        holder.subTasksNumTextView.setVisibility(View.GONE);

        holder.deleteImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUps.openDeletePopUp(context, popUpTaskListener, position, "האם ברצונך למחוק את תת-המשימה?");
            }
        });

        holder.editImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUps.openPopUpEditTask(context, popUpTaskListener, task, position, false);
            }
        });


        if (shake)
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.shaking));


    }



    @Override
    public int getItemCount() {
        return task.getSubTasks().size();
    }



    class SubTasksAdapterViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout constraintLayoutParent;
        TextView taskNameTextView, taskTimeTextView, subTasksNumTextView;
        ImageView playImgView, deleteImgView, editImgView;

        public SubTasksAdapterViewHolder(@NonNull View itemView) {
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
