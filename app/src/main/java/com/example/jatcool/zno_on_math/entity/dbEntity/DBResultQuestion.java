package com.example.jatcool.zno_on_math.entity.dbEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DBResultQuestion {
    @SerializedName("questionID")
    @Expose
    private String questionId;
    @SerializedName("result")
    @Expose
    private boolean result;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
