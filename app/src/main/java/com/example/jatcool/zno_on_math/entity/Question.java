package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.ArrayList;

public class Question extends Entity {
    @SerializedName("theme")
    @Expose
    private Theme theme;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("variants")
    @Expose
    private List<String> variants;
    @SerializedName("correct")
    @Expose
    private String correct;

    public Question(Long id,Theme theme, String text, List<String> variants, String correct) {
        this.setId(id);
        this.theme = theme;
        this.text = text;
        this.variants = variants;
        this.correct = correct;
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

    public List<String> getVariants() {
        return variants;
    }

    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }



}
