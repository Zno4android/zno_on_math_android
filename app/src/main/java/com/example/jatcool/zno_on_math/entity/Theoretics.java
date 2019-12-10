package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Theoretics extends Entity {
    @SerializedName("theme")
    @Expose
    private Theme theme;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tests")
    @Expose
    private List<Test> tests;

    public Theoretics(Long id,Theme theme, String text, List<Test> tests) {
        this.setId(id);
        this.theme = theme;
        this.text = text;
        this.tests = tests;
    }
    public Theoretics(Theme theme, String text, List<Test> tests) {
        this.theme = theme;
        this.text = text;
        this.tests = tests;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
