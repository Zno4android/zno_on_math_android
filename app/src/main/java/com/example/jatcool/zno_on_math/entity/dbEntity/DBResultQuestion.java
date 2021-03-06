package com.example.jatcool.zno_on_math.entity.dbEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBResultQuestion {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("questionId")
    @Expose
    private String questionId;
    @SerializedName("result")
    @Expose
    private boolean result;
    @SerializedName("date")
    @Expose
    private String dateStr;

    public DBResultQuestion(String token, String questionId, boolean result, Date date) {
        this.token = token;
        this.questionId = questionId;
        this.result = result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateStr = simpleDateFormat.format(date);
    }
}
