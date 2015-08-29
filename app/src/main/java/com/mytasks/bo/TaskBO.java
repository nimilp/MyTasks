package com.mytasks.bo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nimilpeethambaran on 8/21/15.
 */
public class TaskBO {


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
     * @param query
     * @return
     */
    public boolean contains(String query){


        if(query==null ){
            return false;
        }
        query = query.toLowerCase();
        return name.toLowerCase().contains(query) ||
                desc.toLowerCase().contains(query) ||
                comments.toLowerCase().contains(query) ||
                date.toLowerCase().contains(query);
    }
}
