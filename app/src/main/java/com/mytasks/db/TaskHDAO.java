package com.mytasks.db;

import android.content.Context;

import com.mytasks.bo.TaskBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nimilpeethambaran on 8/21/15.
 */
public class TaskHDAO {

    private Context context;
    public TaskHDAO(Context context){
        this.context = context;
    }

    public List<TaskBO> getTasks(){
//        ArrayList<TaskBO> tasks = new ArrayList<>(10);
//        for(int i =0;i<30;i++){
//            TaskBO bo = new TaskBO();
//            bo.setId(i);
//            bo.setName("Name "+i);
//            bo.setComments(i+"");
//            bo.setDesc(bo.getComments());
//            tasks.add(bo);
//        }
        return null;
    }
}
