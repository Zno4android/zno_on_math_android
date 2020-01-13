package com.example.jatcool.zno_on_math.entity.dbEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBResultQuestion {
    @SerializedName("token")
    @Expose
    private int token;
    @SerializedName("testID")
    @Expose
    private String testID;
    @SerializedName("result")
    @Expose
    private boolean result;
    @SerializedName("date")
    @Expose
    private String dateStr;

    public DBResultQuestion(int token, String testID, boolean result, Date date) {
        this.token = token;
        this.testID = testID;
        this.result = result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateStr = simpleDateFormat.format(date);
    }
}
