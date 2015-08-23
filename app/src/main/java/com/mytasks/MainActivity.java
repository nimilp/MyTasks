package com.mytasks;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mytasks.adapters.TaskListAdapter;
import com.mytasks.bo.TaskBO;
import com.mytasks.db.TaskHDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private TaskHDAO dao;
    private List<TaskBO> tasks;
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
            listView.setAdapter(new TaskListAdapter(this,tasks));
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
                startActivity(new Intent(v.getContext(),AddTask.class));
            }
        });
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


}
