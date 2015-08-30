package com.mytasks;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.mytasks.bo.TaskBO;
import com.mytasks.db.TaskHDAO;
import com.mytasks.utils.DateUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyTasksReceiver extends BroadcastReceiver {

    private Calendar calendar = Calendar.getInstance();
    private TaskHDAO dao;
    private String name = null;
    private static final String TAG = MyTasksReceiver.class.getSimpleName();
    final static private String GROUP = "MyTasks";
    private long MIILISECONDS_IN_A_DAY = (1000 * 60 * 60 * 24);

    public MyTasksReceiver() {
       // Log.i(myName,"Task receiver invoked by constructor");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "invoking the receiver");
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dao = new TaskHDAO(context);
        NotificationCompat.Builder not;
        ArrayList<String> builder = null;
        List<TaskBO> tasks = dao.getTasksWithReminder();
        int counter = 0;
        if (tasks != null && tasks.size() > 0) {
            for (TaskBO taskBO : tasks) {

                if (name == null) {
                    name = context.getResources().getString(R.string.app_name);
                }

                int dateDiff;
                try{
                Date taskDate = DateUtils.getDate(taskBO.getDate());
                if (taskDate != null) {


//                    if (calendar.before(taskDate)) {
//                        dateDiff = (int) ((calendar.getTimeInMillis() - taskDate.getTime()) / MIILISECONDS_IN_A_DAY);
//                    } else {
                        dateDiff = (int) ((taskDate.getTime() - calendar.getTimeInMillis()) / MIILISECONDS_IN_A_DAY);
//                    }


                    if (taskBO.isRemind()) {
                        ++counter;
                        if (builder == null) {
                            Log.d(TAG, "initializing the notification builder");
                            builder = new ArrayList<>(tasks.size());
                        }
                        Log.d(TAG,"Task days "+taskBO.getDaysToRemind()+", dateDiff "+dateDiff);

                        if (taskBO.getDaysToRemind() >= dateDiff && dateDiff >-1) {

                            builder.add(MessageFormat.format(context.getResources().getString(R.string.task_due_message), taskBO.getName(), dateDiff));
                        } else if (dateDiff<=0){

                            builder.add(MessageFormat.format(context.getResources().getString(R.string.task_overdue), taskBO.getName(), dateDiff));
                        }


                    }
                }}catch (ParseException e){
                    Log.e(TAG,e.getMessage());
                }
            }
        }
        //Log.d(myName, builder.toString());
        if (builder != null && !builder.isEmpty()) {
            Intent start = new Intent(context,Splash.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,start,PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (String s : builder) {
                inboxStyle.addLine(s);
            }
            inboxStyle.setBigContentTitle(name)
                    .setSummaryText(MessageFormat.format(context.getResources().getString(R.string.pending_tasks_with_counter),counter ));
            not = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
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
