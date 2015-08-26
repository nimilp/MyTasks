package com.mytasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.mytasks.bo.TaskBO;
import com.mytasks.constants.MyTaskConstants;
import com.mytasks.db.TaskHDAO;
import com.mytasks.utils.DateUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class ModifyTask extends AppCompatActivity {

    private DatePickerListener datePickerCallBack = new DatePickerListener();
    private Calendar calendar = Calendar.getInstance();
    private Button taskDate;
    private Spinner daySpinner;
    private CheckBox remindMein;
    private CheckBox recurBb;
    private EditText name;
    private EditText desc;
    private EditText id;
    private EditText comment;
    private TaskHDAO dao;
    private FloatingActionButton fab;

    //public AddTask(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dao == null) {
            dao = new TaskHDAO(this);
        }


        setContentView(R.layout.modify_task);
        id = (EditText) findViewById(R.id.taskId);
        name = (EditText) findViewById(R.id.taskName);
        desc = (EditText) findViewById(R.id.taskDesc);
        comment = (EditText) findViewById(R.id.taskCmt);
        recurBb = (CheckBox) findViewById(R.id.recurringCb);
        taskDate = (Button) findViewById(R.id.taskDate);

        //taskDate.so(false);
        final int currYear = calendar.get(Calendar.YEAR);
        final int currDate = calendar.get(Calendar.DAY_OF_MONTH);
        final int currMonth = calendar.get(Calendar.MONTH);

        //logic that brings up the date picker.
        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //invoked only for down
              //  if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), datePickerCallBack, currYear, currMonth, currDate) {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int month, int day) {

                            //force set to current day
                            if (year < calendar.get(Calendar.YEAR)) {
                                view.updateDate(currYear, currMonth, currDate);
                            }
                            if (month < currMonth && year == currYear) {
                                view.updateDate(currYear, currMonth, currDate);
                            }
                            if (day < currDate && currMonth == month && year == currYear) {
                                view.updateDate(currYear, currMonth, currDate);
                            }
                            // return view;
                        }
                    };
                    datePickerDialog.show();
                    //return true;
                //}
                //return false;
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long taskId = Long.valueOf(String.valueOf(id.getText()));
                dao.deleteTask(taskId);

                setResult(MyTaskConstants.DELETE_SUCCESSFUL_RESULT);
                finish();
            }
        });
        //set values
        long taskId = getIntent().getLongExtra(MyTaskConstants.TASK_ID, -1);
        TaskBO task = dao.getTask(taskId);
        setTask(task);
    }

    /**
     * sets the values
     */
    private void setTask(TaskBO task) {
        id.setText(String.valueOf(task.getId()));
        name.setText(task.getName());
        desc.setText(task.getDesc());
        comment.setText(task.getComments());
        taskDate.setText(task.getDate());
        remindMein.setChecked(task.isRemind());
        recurBb.setChecked(task.isRecur());
        if (task.isRemind()) {
            daySpinner.setSelection(task.getDaysToRemind());
        }
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
            boolean retVal = validate();
            if (!retVal) {
                updateTask();
                setResult(MyTaskConstants.UPDATE_SUCCESSFUL_RESULT);
                finish();
            }
            return retVal;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validate() {
        boolean hasError = false;

        if (name.getText() == null || String.valueOf(name.getText()).trim().length() == 0) {
            name.setError(getResources().getString(R.string.task_name_required));
            hasError = true;
        }
        if (taskDate.getText() == null || String.valueOf(taskDate.getText()).trim().length() == 0) {
            taskDate.setError(getResources().getString(R.string.taskdate_required));
            hasError = true;
        }

        try{
        if(recurBb.isChecked() && DateUtils.getDate(String.valueOf(taskDate.getText())).before(calendar.getTime())){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Date date = DateUtils.getDate(String.valueOf(taskDate.getText()));
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);;
            cal.roll(Calendar.MONTH,1);
            builder.setTitle(getResources().getString(R.string.expired_warning_title));
            builder.setMessage(MessageFormat.format(getResources().getString(R.string.expired_message),DateUtils.parseDate(cal.getTime())));
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                        taskDate.setText(DateUtils.parseDate(cal.getTime()));
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    taskDate.setError(getResources().getString(R.string.expired_date));
                    dialog.cancel();
                }
            });
            builder.create().show();
            hasError = true;
        }}catch (ParseException e){
            Log.e("ModifyTask", e.getMessage());
        }
        return hasError;
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            taskDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        }
    }

    private void updateTask() {
        TaskBO taskBO = new TaskBO();
        taskBO.setId(Long.valueOf(String.valueOf(id.getText())));
        taskBO.setName(String.valueOf(name.getText()));
        taskBO.setDesc(String.valueOf(desc.getText()));
        taskBO.setComments(String.valueOf(comment.getText()));
        taskBO.setDate(String.valueOf(taskDate.getText()));
        taskBO.setRemind(remindMein.isChecked());
        taskBO.setRecur(recurBb.isChecked());
        taskBO.setDaysToRemind(daySpinner.getSelectedItemPosition());
        taskBO.setLastChangedDate(DateUtils.parseDate(calendar.getTime()));
        dao.updateTask(taskBO);
        setResult(Activity.RESULT_OK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //on
}
