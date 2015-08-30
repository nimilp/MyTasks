package com.mytasks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mytasks.R;
import com.mytasks.bo.TaskBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nimilpeethambaran on 8/30/15.
 */
public class TaskListSimpleAdaptor extends BaseAdapter {

    private Context context;
    private List<TaskBO> tasks;
    private List<TaskBO> original;

    public TaskListSimpleAdaptor(Context context, List<TaskBO> tasks) {

       // super(context, R.layout.list_tasks_layout, tasks);

        this.context = context;
        this.tasks = tasks;
        this.original = tasks;
        //addAll(tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_tasks_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.logo = (TextView) convertView.findViewById(R.id.logo);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.taskName);
            viewHolder.taskDesc = (TextView) convertView.findViewById(R.id.taskDesc);
            viewHolder.taskDate = (TextView) convertView.findViewById(R.id.taskDate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TaskBO task = getItem(position);

        viewHolder.logo.setText(task.getLogo());
        if(task.isOverdue()){
            viewHolder.logo.getBackground().setAlpha(50);
        }else{
            viewHolder.logo.getBackground().setAlpha(255);
        }
        viewHolder.taskName.setText(task.getName());
        viewHolder.taskDesc.setText(task.getDesc());
        viewHolder.taskDate.setText(task.getDate());
        return convertView;
    }

    private class ViewHolder {
        TextView logo;
        TextView taskName;
        TextView taskDesc;
        TextView taskDate;
    }

    @Override
    public int getCount() {
        if(tasks!=null && !tasks.isEmpty()){
            return tasks.size();
        }
        return 0;
    }

    @Override
    public TaskBO getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    public void filter(String filter) {
        ArrayList<TaskBO>  newList = null;
        if(filter==null || filter.trim().length()==0){
            tasks = original;
            notifyDataSetInvalidated();
        }else{
            newList = new ArrayList<>(10);
            for(TaskBO task : original){
                if(task.contains(filter)){
                    newList.add(task);
                }
            }

            tasks = newList;
            notifyDataSetChanged();
        }
    }

    /**
     * method called when any modification happens on the list
     * @param tasks
     */
    public void setTasks(List<TaskBO> tasks){
        this.tasks = tasks;
        this.original = tasks;
        notifyDataSetChanged();
    }
}
