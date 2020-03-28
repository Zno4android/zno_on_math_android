package com.example.jatcool.zno_on_math.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Answer {
    @SerializedName("text")
    @Expose
    private List<String> text;
    @SerializedName("correct")
    @Expose
    private boolean correct;

    public Answer(List<String> text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
