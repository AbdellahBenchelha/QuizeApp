package com.example.quizapp.model;

public class user {
    private  String userName;
    private  String userEmail;
    private  String userLogin;
    private  String userpassword;

    public user(String userName, String userEmail, String userLogin, String userpassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userLogin = userLogin;
        this.userpassword = userpassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
}
