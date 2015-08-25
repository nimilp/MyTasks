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
        if(dao == null){
            dao = new TaskHDAO(getApplicationContext());
        }
        tasks = dao.getTasks();
        if(tasks!=null && !tasks.isEmpty()) {
            listViewAdapter = new TaskListAdapter(this,tasks);
            listView.setAdapter(listViewAdapter);
            listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if(previous!=-1 && groupPosition!=previous){
                        listView.collapseGroup(previous);
                    }
                    previous = groupPosition;
                }
            });
        }else{
            Toast.makeText(this,R.string.no_tasks,Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddTask.class);
                startActivityForResult(intent, 0);
            }
        });

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

    private class Refresh implements RefreshList{
        @Override
        public void refresh() {

//            listView.getAdapter().n
        }
    }

    private void startMyBroadCast(){
        long now = System.currentTimeMillis();
        long time = 500;
        Intent myIntent = new Intent("com.mytasks.PENDING_INTENT");
        myIntent.setClass(this,MyTasksReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, now + time, time, pendingIntent);
//this.reg
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
//            listView.
           listViewAdapter.setTasks(dao.getTasks());
            Toast.makeText(this,R.string.task_inserted_successfully,Toast.LENGTH_SHORT).show();
        }
    }
}
