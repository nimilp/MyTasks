package com.mytasks.db.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mytasks.db.constants.SQLConstants;

/**
 * Created by nimilpeethambaran on 8/22/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "my_tasks.db";
    private static int DB_VERSION = 2;
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //onCreate(db);
       // db.execSQL("drop table tasks");
        db.execSQL(SQLConstants.TASK.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table tasks");
        db.execSQL(SQLConstants.TASK.CREATE_QUERY);
    }
}
