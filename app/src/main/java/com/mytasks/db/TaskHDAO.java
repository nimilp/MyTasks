package com.mytasks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import com.mytasks.bo.TaskBO;
import com.mytasks.db.connection.DBHelper;
import static com.mytasks.db.constants.SQLConstants.TASK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nimilpeethambaran on 8/21/15.
 */
public class TaskHDAO implements Serializable{
    private static final long serialVersionUID = 0L;

    private DBHelper dbHelper;
    private Context context;

    public TaskHDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public List<TaskBO> getTasks() {
        ArrayList<TaskBO> taskList = null;
        Cursor query = dbHelper.getReadableDatabase().query(TASK.TABLE_NAME, TASK.COLUMNS, null, null, null, null, null);
        if (query != null && query.getCount() > 0) {
            //query.moveToFirst();
            taskList = new ArrayList<>(query.getCount());
            while(query.moveToNext()) {
                TaskBO task = new TaskBO();
                task.setId(query.getInt(0));
                task.setName(query.getString(1));
                task.setDesc(query.getString(2));
                task.setComments(query.getString(3));
                task.setDate(query.getString(4));
                task.setRemind(query.getInt(5) == 0);
                task.setRecur(query.getInt(6) == 0);
                task.setDaysToRemind(query.getInt(7));

                taskList.add(task);
            }
            dbHelper.close();

        }
        return taskList;
    }

    public void insertTask(TaskBO task){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues(8);
        values.put(TASK.COLUMNS[1], task.getName());
        values.put(TASK.COLUMNS[2],task.getDesc());
        values.put(TASK.COLUMNS[3],task.getComments());
        values.put(TASK.COLUMNS[4], task.getDate());
        values.put(TASK.COLUMNS[5],task.isRecur()?0:1);
        values.put(TASK.COLUMNS[6],task.isRemind()?0:1);
        values.put(TASK.COLUMNS[7],task.getDaysToRemind());
        db.insert(TASK.TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        //values.put(TASK.COLUMNS[8],new Date());
    }
}
