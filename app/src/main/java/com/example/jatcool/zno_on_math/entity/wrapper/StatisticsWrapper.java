package com.example.jatcool.zno_on_math.entity.wrapper;

import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBStatistics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatisticsWrapper {
    @SerializedName("statisticTest")
    @Expose
    private DBStatistics testResult;
    @SerializedName("questionArray")
    @Expose
    private List<DBResultQuestion> questionsResult;

    public StatisticsWrapper(DBStatistics testResult, List<DBResultQuestion> questionsResult) {
        this.testResult = testResult;
        this.questionsResult = questionsResult;
    }
}
