package com.example.jatcool.zno_on_math.entity.wrapper;

import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatisticsWrapper {
    @SerializedName("statisticTest")
    @Expose
    private Statistics testResult;
    @SerializedName("questionArray")
    @Expose
    private List<DBResultQuestion> questionsResult;

    public StatisticsWrapper(Statistics testResult, List<DBResultQuestion> questionsResult) {
        this.testResult = testResult;
        this.questionsResult = questionsResult;
    }
}
