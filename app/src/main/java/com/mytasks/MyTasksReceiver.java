package com.mytasks;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.util.TimeUtils;
import android.util.Log;

import com.mytasks.bo.TaskBO;
import com.mytasks.db.TaskHDAO;

import java.text.MessageFormat;
import java.util.ArrayList;
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
        Log.i(myName,"Task receiver invoked by constructor");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(myName, "invoking the receiver");
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dao = new TaskHDAO(context);
        NotificationCompat.Builder not = null;
        ArrayList<String> builder = null;
        List<TaskBO> tasks = dao.getTasks();
        int counter = 0;
        if (tasks != null && tasks.size() > 0) {
            for (TaskBO taskBO : tasks) {

                if (name == null) {
                    name = context.getResources().getString(R.string.app_name);
                }

                int dateDiff = 0;
                if (taskBO.getDateVal() != null) {
                    ++counter;

                    if (calendar.before(taskBO.getDateVal())) {
                        dateDiff = (int) ((calendar.getTimeInMillis() - taskBO.getDateVal().getTime()) / MIILISECONDS_IN_A_DAY);
                    } else {
                        dateDiff = (int) ((taskBO.getDateVal().getTime() - calendar.getTimeInMillis()) / MIILISECONDS_IN_A_DAY);
                    }


                    if (taskBO.isRemind()) {
                        if (builder == null) {
                            Log.d(myName, "initializing the notification builder");
                            builder = new ArrayList<>(tasks.size());
                            ;

                        }

                        if (taskBO.getDaysToRemind() >= dateDiff) {

                            builder.add(MessageFormat.format(context.getResources().getString(R.string.task_due_message), taskBO.getName(), dateDiff));
                        } else {

                            builder.add(MessageFormat.format(context.getResources().getString(R.string.task_overdue), taskBO.getName(), dateDiff));
                        }


                    }
                }
            }
        }
        Log.d(myName, builder.toString());
        if (builder != null) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (String s : builder) {
                inboxStyle.addLine(s);
            }
            inboxStyle.setBigContentTitle(name)
                    .setSummaryText(MessageFormat.format(context.getResources().getString(R.string.pending_tasks_with_counter),counter ));
            not = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setGroup(GROUP)
                    .setGroupSummary(true)
                    .setContentTitle(name)
                    .setContentText(context.getResources().getString(R.string.pending_tasks))
                    .setStyle(inboxStyle)
                    .setNumber(counter)
                    .setCategory(Notification.CATEGORY_EVENT)
                    .setSmallIcon(R.mipmap.ic_action_mt);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            Notification notificationCompat = not.build();
            manager.notify(1, notificationCompat);
        }
    }

}
