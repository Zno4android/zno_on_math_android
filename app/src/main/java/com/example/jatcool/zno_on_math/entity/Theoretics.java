package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Theoretics extends Entity {
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("image")
    @Expose
    String image;
    @SerializedName("tests")
    @Expose
    private List<Test> tests;
    @SerializedName("files")
    @Expose
    private List<String> files;

    public Theoretics() {
    }

    public Theoretics(String id, String theme, String text, String name, String image, List<Test> tests, List<String> files) {
        this.setId(id);
        this.theme = theme;
        this.text = text;
        this.tests = tests;
        this.name = name;
        this.image = image;
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
