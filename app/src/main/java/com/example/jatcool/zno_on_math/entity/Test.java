package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Test extends Entity {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("theme")
    @Expose
    private Theme theme;
    @SerializedName("questions")
    @Expose
    private List<Question> questions;

    public Test(Long id, String name, Theme theme, List<Question> questions) {
        this.setId(id);
        this.name = name;
        this.theme = theme;
        this.questions = questions;
    }

    public Test(String name, Theme theme, List<Question> questions) {
        this.name = name;
        this.theme = theme;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
