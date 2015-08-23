package com.mytasks.bo;

/**
 * Created by nimilpeethambaran on 8/21/15.
 */
public class TaskBO {

    private int id;
    private String name;
    private String desc;
    private String comments;
    private String date;
    private boolean remind;
    private int daysToRemind;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
