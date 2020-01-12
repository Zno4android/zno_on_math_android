package com.example.jatcool.zno_on_math.entity;

import com.example.jatcool.zno_on_math.util.Answer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ResultQuestions {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("question")
    @Expose
    private Question question;
    @SerializedName("answer")
    @Expose
    private Answer answer;
    @SerializedName("date")
    @Expose
    private Date date;

    public ResultQuestions(User user, Question question, Answer answer, Date date) {
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Date getDate() {
        return date;
    }
}
