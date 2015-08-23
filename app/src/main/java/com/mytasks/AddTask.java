package com.mytasks;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.mytasks.bo.TaskBO;
import com.mytasks.db.TaskHDAO;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    private DatePickerListener datePickerCallBack = new DatePickerListener();
    private Calendar calendar = Calendar.getInstance();
    private EditText taskDate;
    private Spinner daySpinner;
    private CheckBox remindMein;
    private CheckBox recurBb;
    private EditText name;
    private EditText desc;
    private EditText comment;
    private TaskHDAO dao;

    //public AddTask(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(dao==null){
            dao = new TaskHDAO(this);
        }
        setContentView(R.layout.add_task);
        name = (EditText) findViewById(R.id.taskName);
        desc = (EditText) findViewById(R.id.taskDesc);
        comment = (EditText) findViewById(R.id.taskCmt);
        recurBb = (CheckBox) findViewById(R.id.recurringCb);
        taskDate = (EditText) findViewById(R.id.taskDate);

        //taskDate.so(false);
        taskDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), datePickerCallBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        remindMein = (CheckBox) findViewById(R.id.remindCb);
        remindMein.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daySpinner.setVisibility(View.VISIBLE);
                } else {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        });
        //spinner
        daySpinner = (Spinner) findViewById(R.id.days);
        daySpinner.setAdapter(new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.noOfDays)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
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
        if (id == R.id.save) {
            validate();
            insertTask();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validate() {
        boolean hasError = false;

        if (name.getText() == null || String.valueOf(name.getText()).trim().length() == 0) {
            name.setError(getResources().getString(R.string.task_name_required));
        }

        return hasError;
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            taskDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
        }
    }

    private void insertTask(){
        TaskBO taskBO = new TaskBO();
        taskBO.setName(String.valueOf(name.getText()));
        taskBO.setDesc(String.valueOf(desc.getText()));
        taskBO.setComments(String.valueOf(comment.getText()));
        taskBO.setDate(String.valueOf(taskDate.getText()));
        taskBO.setRemind(remindMein.isChecked());
        taskBO.setRecur(recurBb.isChecked());
        taskBO.setDaysToRemind(daySpinner.getSelectedItemPosition());
        dao.insertTask(taskBO);


    }
}
