package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Test extends Entity {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("questions")
    @Expose
    private List<Question> questions;
    @SerializedName("owner")
    @Expose
    private String user;

    public Test(String id, String name, String theme, List<Question> questions) {
        this.setId(id);
        this.name = name;
        this.theme = theme;
        this.questions = questions;
    }

    public Test(String name, String theme, List<Question> questions) {
        this.name = name;
        this.theme = theme;
        this.questions = questions;
    }

    public Test(String name, String theme, List<Question> questions, String user) {
        this.name = name;
        this.theme = theme;
        this.questions = questions;
        this.user = user;
    }

    public Test() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", theme:" + theme +
                ", questions:" + questions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Test test = (Test) o;
        return this.getId().equals(test.getId());
    }

}
