package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question extends Entity {
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("variants")
    @Expose
    private List<String> variants;
    @SerializedName("correct")
    @Expose
    private String correct;

    public Question(String id, String theme, String type, String text, List<String> variants, String correct) {
        this.setId(id);
        this.theme = theme;
        this.type = type;
        this.text = text;
        this.variants = variants;
        this.correct = correct;
    }

    public Question(String theme, String type, String text, List<String> variants, String correct) {
        this.theme = theme;
        this.type = type;
        this.text = text;
        this.variants = variants;
        this.correct = correct;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "{" +
                "theme:" + theme +
                ", text:'" + text + '\'' +
                ", variants:" + variants +
                ", correct:'" + correct + '\'' +
                '}';
    }
}
