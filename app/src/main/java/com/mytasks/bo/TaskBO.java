package com.mytasks.bo;

import android.util.Log;

import com.mytasks.utils.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nimilpeethambaran on 8/21/15.
 */
public class TaskBO {

    private static final String TAG = "TaskBO";
private Calendar calendar =Calendar.getInstance();
    private String logo;
    private long id;
    private String name;
    private String desc;
    private String comments;
    private String date;
    private boolean remind;
    private int daysToRemind;
    private boolean recur;
    private String lastChangedDate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        String[] split = name.split(" ");
        if(split!=null && split.length>0){
            logo = String.valueOf( split[0].charAt(0)).toUpperCase();
            if(split.length>1){
                logo = logo+String.valueOf(split[1].charAt(0)).toUpperCase();
            }
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public int getDaysToRemind() {
        return daysToRemind;
    }

    public void setDaysToRemind(int daysToRemind) {
        this.daysToRemind = daysToRemind;
    }

    public boolean isRecur() {
        return recur;
    }

    public void setRecur(boolean recur) {
        this.recur = recur;
    }

    public void setLastChangedDate(String date){
        this.lastChangedDate = date;
    }

    public String getLastChangedDate(){

       return lastChangedDate;
    }

    /**
     * Util method for searching anything inside the task
     * @param query filter
     * @return return whether contains the query
     */
    public boolean contains(String query){


        if(query==null ){
            return false;
        }
        query = query.toLowerCase();
        return name.toLowerCase().contains(query) ||
                desc.toLowerCase().contains(query) ||
                //comments.toLowerCase().contains(query) ||
                date.toLowerCase().contains(query);
    }

    public String getLogo(){
        return logo;
    }

    public boolean isOverdue(){
        boolean isOverdue = false;
        try {
            Date taskDate = DateUtils.getDate(date);
            isOverdue = (calendar.getTimeInMillis()-taskDate.getTime())>0L;
        } catch (ParseException e) {
            Log.e(TAG,e.getMessage());
           // return false;
        }
        return isOverdue;
    }
}
