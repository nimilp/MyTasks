package com.mytasks;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mytasks.adapters.TaskListAdapter;
import com.mytasks.bo.TaskBO;
import com.mytasks.constatns.MyTaskConstants;
import com.mytasks.db.TaskHDAO;
import com.mytasks.implementation.RefreshList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private TaskHDAO dao;
    private List<TaskBO> tasks;
    private TaskListAdapter listViewAdapter;
    private int previous = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        listView = (ExpandableListView) findViewById(R.id.taskList);
        if (dao == null) {
            dao = new TaskHDAO(getApplicationContext());
        }
        tasks = dao.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            listViewAdapter = new TaskListAdapter(this, tasks);
            listView.setAdapter(listViewAdapter);
            listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (previous != -1 && groupPosition != previous) {
                        listView.collapseGroup(previous);
                    }
                    previous = groupPosition;
                }
            });
            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    Intent intent = new Intent(v.getContext(), ModifyTask.class);
                    TaskBO taskBO = (TaskBO) ((TaskListAdapter) parent.getExpandableListAdapter()).getGroup(groupPosition);
                    intent.putExtra(MyTaskConstants.TASK_ID, id);
                    startActivityForResult(intent, MyTaskConstants.UPDATE_REQUEST);
                    return true;
                }
            });
        } else {
            Toast.makeText(this, R.string.no_tasks, Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddTask.class);
                startActivityForResult(intent, MyTaskConstants.INSERT_REQUEST);
            }
        });

        //invoke the broadcast
        startMyBroadCast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Method that invokes the broadcast
     */
    private void startMyBroadCast() {
        long now = System.currentTimeMillis();
        long time = 1000;
        Intent myIntent = new Intent(MyTaskConstants.PENDING_INTENT_NAME);
        myIntent.setClass(this, MyTasksReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_FIFTEEN_MINUTES, time, pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            switch (resultCode) {
                case MyTaskConstants.INSERT_SUCCESSFUL_RESULT:

                    tasks = dao.getTasks();
                    /*
                    initial insertion listViewAdapter will be null.
                    initialize it and set to listview
                     */
                    if (listViewAdapter == null) {

                        listViewAdapter = new TaskListAdapter(this, tasks);
                        listView.setAdapter(listViewAdapter);
                    }else{
                        listViewAdapter.setTasks(tasks);
                    }

                    Toast.makeText(this, R.string.task_inserted_successfully, Toast.LENGTH_SHORT).show();
                    break;
                case MyTaskConstants.UPDATE_SUCCESSFUL_RESULT:
                    listViewAdapter.setTasks(dao.getTasks());
                    Toast.makeText(this, R.string.task_updated_successfully, Toast.LENGTH_SHORT).show();
                    break;
                case MyTaskConstants.DELETE_SUCCESSFUL_RESULT:
                    listViewAdapter.setTasks(dao.getTasks());
                    Toast.makeText(this, R.string.task_deleted_successfully, Toast.LENGTH_SHORT).show();
                    break;
//                default:
//                    super.onActivityResult(requestCode,resultCode,data);
            }

    }
}
