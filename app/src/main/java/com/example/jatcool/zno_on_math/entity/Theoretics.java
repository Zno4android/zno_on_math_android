package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Theoretics extends Entity {
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("themeName")
    @Expose
    String themeName;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("image")
    @Expose
    String image;
    @SerializedName("tests")
    @Expose
    private List<Test> tests;

    public Theoretics(String id, String theme, String text, String themeName, String image, List<Test> tests) {
        this.setId(id);
        this.theme = theme;
        this.text = text;
        this.tests = tests;
        this.themeName = themeName;
        this.image = image;
    }

    public Theoretics(String theme, String text, List<Test> tests) {
        this.theme = theme;
        this.text = text;
        this.tests = tests;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
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
