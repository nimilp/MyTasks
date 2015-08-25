package com.mytasks.db.constants;

/**
 * Created by nimilpeethambaran on 8/23/15.
 */
public class SQLConstants {

    public static class TASK {
        public static String TABLE_NAME = "tasks";
        public static String[] COLUMNS = {"task_id", //0
                "task_name", //1
                "task_desc",//2
                "task_comment",//3
                "task_date",//4
                "recur",//5
                "remind_me",//6
                "remind_days",//7
                "last_changed_ts"//8
        };

        public static String CREATE_QUERY = "" +
                "create table " + TABLE_NAME + "(" +
                COLUMNS[0] + " INTEGER PRIMARY KEY," +
                COLUMNS[1] + " TEXT," +
                COLUMNS[2] + " TEXT," +
                COLUMNS[3] + " TEXT," +
                COLUMNS[4] + " TEXT," +
                COLUMNS[5] + " INTEGER," +
                COLUMNS[6] + " INTEGER," +
                COLUMNS[7] + " INTEGER," +
                COLUMNS[8] + " TEXT" +
                ")";
        public static String FILTER_BY_ID = " task_id=?";
    }

}
