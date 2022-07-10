package com.noam.wink.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noam.wink.R;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.model.SubTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinishSubTaskAdapter extends ArrayAdapter<SubTask> {

    private List<SubTask> subTasks;
    private Context context;


    public FinishSubTaskAdapter(Context context, int resourceId,
                                List<SubTask> subTasks) {
        super(context, resourceId, subTasks);
        this.subTasks = subTasks;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }



    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =inflater.inflate(R.layout.finished_task_item, parent, false);
        TextView subTaskName = (TextView)convertView.findViewById(R.id.sub_task_name_text_view_finished_task_item);
        TextView subTaskTime = (TextView)convertView.findViewById(R.id.sub_task_time_text_view_finished_task_item);
        TextView subTaskFinishedTime = (TextView)convertView.findViewById(R.id.sub_task_time_finished_text_view_finished_task_item);

        subTaskFinishedTime.setText(ShortCutsClass.getHourString(subTasks.get(position).getDoneSubTaskTime()));
        subTaskName.setText(subTasks.get(position).getSubTaskName());
        subTaskTime.setText(ShortCutsClass.getTimeFormatted(ShortCutsClass.getTimeSeparate(subTasks.get(position).getSubTaskTime())));



        if (subTasks.get(position).getDoneSubTaskTime() >= subTasks.get(position).getSubTaskTime() * 60000 * 0.8 && subTasks.get(position).getDoneSubTaskTime() <= subTasks.get(position).getSubTaskTime() * 60000 * 1.2){
            subTaskFinishedTime.setTextColor(Color.parseColor("#53A92B"));
        }else subTaskFinishedTime.setTextColor(Color.parseColor("#EA596E"));



        return convertView;
    }



}
