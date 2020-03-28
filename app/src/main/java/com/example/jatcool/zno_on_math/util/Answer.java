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

    public Answer() {
    }

    public Answer(List<String> text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public void checkCorrect(List<String> correct, List<String> userAnswer) {
        for (int i = 0; i < correct.size(); i++) {
            if (!correct.get(i).equals(userAnswer.get(i))) {
                this.correct = false;
                return;
            }
        }

        this.correct = true;
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
