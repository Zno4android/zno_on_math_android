package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Statistics {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("test")
    @Expose
    private Test test;
    @SerializedName("result")
    @Expose
    private double result;
    @SerializedName("date")
    @Expose
    private Date date;

    public Statistics() {
    }

    public Statistics(User user, Test test, double result, Date date) {
        this.user = user;
        this.test = test;
        this.result = result;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Test getTest() {
        return test;
    }

    public double getResult() {
        return result;
    }

    public Date getDate() {
        return date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "user:" + user +
                ", test:" + test +
                ", result:" + result +
                ", date:" + date +
                '}';
    }
}
