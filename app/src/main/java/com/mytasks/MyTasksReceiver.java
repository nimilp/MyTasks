package com.mytasks;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.TimeUtils;
import android.util.Log;

import com.mytasks.bo.TaskBO;
import com.mytasks.db.TaskHDAO;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MyTasksReceiver extends BroadcastReceiver {

    private Calendar calendar = Calendar.getInstance();
    private TaskHDAO dao;
    private String name = null;
    private String myName = MyTasksReceiver.class.getSimpleName();
    final static String GROUP = "MyTasks";
    private long MIILISECONDS_IN_A_DAY = (1000 * 60 * 60 * 24);

    public MyTasksReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        dao = new TaskHDAO(context);
        List<TaskBO> tasks = dao.getTasks();
        if (tasks != null && tasks.size() > 0) {
            for (TaskBO taskBO : tasks) {

                if (name == null) {
                    Log.d(myName, "App name is empty");
                    name = context.getResources().getString(R.string.app_name);
                }

                int dateDiff = 0;
                if(taskBO.getDateVal()!=null) {
                    if (calendar.after(taskBO.getDateVal())) {
                        dateDiff = (int) ((calendar.getTimeInMillis() - taskBO.getDateVal().getTime()) / MIILISECONDS_IN_A_DAY);
                    } else {
                        dateDiff = (int) ((taskBO.getDateVal().getTime() - calendar.getTimeInMillis()) / MIILISECONDS_IN_A_DAY);
                    }


                    if (taskBO.isRemind()) {
                        NotificationCompat.Builder not = new NotificationCompat.Builder(context)
                                .setContentText(taskBO.getDesc())
                                .setContentTitle(taskBO.getName())
                                .setSmallIcon(R.mipmap.ic_action_mt)
                                .setGroup(GROUP)
                                .setGroupSummary(true);

                        if (taskBO.getDaysToRemind() >= dateDiff) {

                            not.setCategory(Notification.CATEGORY_ALARM);
                        } else {

                            not.setCategory(Notification.CATEGORY_ERROR)
                                    .setSubText(MessageFormat.format(context.getResources().getString(R.string.task_overdue), taskBO.getName(), dateDiff))
                            ;

                        }

                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(taskBO.getId(), not.build());
                    }
                }
            }
        }
    }

}
