package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

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
    @SerializedName("questions")
    @Expose
    private List<Question> questions;
    @SerializedName("testID")
    @Expose
    private String testId;
    @SerializedName("userID")
    @Expose
    private String userId;

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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
