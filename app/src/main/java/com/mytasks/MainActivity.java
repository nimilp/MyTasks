package com.mytasks;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mytasks.adapters.TaskListAdapter;
import com.mytasks.bo.TaskBO;
import com.mytasks.constants.MyTaskConstants;
import com.mytasks.db.TaskHDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private TaskHDAO dao;
    private List<TaskBO> tasks;
    private TaskListAdapter listViewAdapter;
    private int previous = -1;
    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().show();
        setContentView(R.layout.activity_main);
        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        listView = (ExpandableListView) findViewById(R.id.taskList);
       //[ listView.requestFocus();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.mnu_search));

        if (searchView != null) {
            Log.d("MainActvity", "Got Search View");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconified(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (listViewAdapter != null && !listViewAdapter.isEmpty()) {
                    listViewAdapter.filter(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (listViewAdapter != null) {
                    listViewAdapter.filter(newText);
                    return true;
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        *set snackbar
         */
        Snackbar snackbar = Snackbar.make(mainLayout, "", Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.primaryDark));
        ((TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        //snackbar ready

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
                } else {
                    listViewAdapter.setTasks(tasks);
                }

                snackbar.setText(R.string.task_inserted_successfully).show();
                break;
            case MyTaskConstants.UPDATE_SUCCESSFUL_RESULT:
                listViewAdapter.setTasks(dao.getTasks());
                snackbar.setText(R.string.task_updated_successfully);
                //snackbar.s
                snackbar.show();
                //  Toast.makeText(this, R.string.task_updated_successfully, Toast.LENGTH_SHORT).show();
                break;
            case MyTaskConstants.DELETE_SUCCESSFUL_RESULT:
                listViewAdapter.setTasks(dao.getTasks());
                snackbar.setText(R.string.task_deleted_successfully).show();
                break;
        }

    }
}
