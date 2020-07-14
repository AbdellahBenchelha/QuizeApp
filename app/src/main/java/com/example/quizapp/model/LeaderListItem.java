package com.example.quizapp.model;

import java.util.Date;

public class LeaderListItem {
    private String score;

    private int userId;

    private Date date;

    public LeaderListItem(String score, Date date, String photo, String quize) {
        this.score = score;
        this.date = date;
        this.photo = photo;
        this.quize = quize;
    }

    private String photo;

    private String quize;

    public String getScore(){
        return score;
    }

    public int getUserId(){
        return userId;
    }

    public Date getDate(){
        return date;
    }

    public String getPhoto(){
        return photo;
    }

    public String getQuize(){
        return quize;
    }
}
