package com.mytasks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.mytasks.R;
import com.mytasks.bo.TaskBO;

import java.util.List;

/**
 * Created by nimilpeethambaran on 8/21/15.
 */
public class TaskListAdapter extends BaseExpandableListAdapter{

    List<TaskBO> tasks;
    Context context;

    public TaskListAdapter(Context context, List<TaskBO> tasks){
        this.tasks = tasks;
        this.context = context;
    }


    @Override
    public int getGroupCount() {
        return tasks.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tasks.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return tasks.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((TaskBO)getGroup(groupPosition)).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getGroupId(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.task_group,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.taskName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(tasks.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.task_children,parent,false);
            viewHolder = new ChildViewHolder();
            viewHolder.desc = (TextView) convertView.findViewById(R.id.descTxt);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.cmtTxt);
            viewHolder.recur = (TextView) convertView.findViewById(R.id.recurText);
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateTxt);
            viewHolder.days = (TextView) convertView.findViewById(R.id.daysTxt);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.desc.setText(tasks.get(groupPosition).getDesc());
        viewHolder.comment.setText(tasks.get(groupPosition).getComments());
        viewHolder.recur.setText(tasks.get(groupPosition).isRecur() ? "True" : "False");
        viewHolder.date.setText(tasks.get(groupPosition).getDate());
        if(tasks.get(groupPosition).isRemind()) {
            ( (TableRow) convertView.findViewById(R.id.secretRow)).setVisibility(View.VISIBLE);
            viewHolder.days.setText(context.getResources().getStringArray(R.array.noOfDays)[tasks.get(groupPosition).getDaysToRemind()]);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder{
        TextView name;

    }

    private class ChildViewHolder{
        TextView desc;
        TextView comment;
        TextView date;
        TextView days;
        TextView recur;

    }




    public void setTasks(List<TaskBO> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }
}
