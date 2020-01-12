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
}
