package com.example.quizapp.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private  String question;
    private String[] options=null;
    private  String image;
    private int answerNr;

    public Question(String question,String[] options, int answerNr,String image) {
        this.question=question;
        this.options = options;
        this.image=image;
        this.answerNr = answerNr;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }
}
